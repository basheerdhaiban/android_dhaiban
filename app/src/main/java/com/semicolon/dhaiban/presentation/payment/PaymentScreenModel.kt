package com.semicolon.dhaiban.presentation.payment

import CartScreen
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.cart.CartItemUiState
import com.semicolon.dhaiban.presentation.cart.toCartItem
import com.semicolon.dhaiban.presentation.cart.toCartItemUiState
import com.semicolon.dhaiban.presentation.wallet.toWalletCurrencyUiState
import com.semicolon.dhaiban.utils.PaymentMethodsType
import com.semicolon.domain.entity.BalanceModel
import com.semicolon.domain.entity.PayTabsModel
import com.semicolon.domain.entity.PaymentModel
import com.semicolon.domain.entity.TransactionModel
import com.semicolon.domain.entity.orders.OrderResponseModel
import com.semicolon.domain.entity.productdetails.CartDataModel
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageCartUseCase
import com.semicolon.domain.usecase.ManageOrderUseCase
import com.semicolon.domain.usecase.ManageWalletUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.ceil

class PaymentScreenModel(
    private val subTotal: Double,
    private val promoCode: Double,
    private val taxes: Double,
    private val total: Double,
    private val currencySymbol: String,
    private val currencyId: Int,
    private val addressId: Int,
    private val orderId: Int,

    private val manageOrderUseCase: ManageOrderUseCase,
    private val manageWalletUseCase: ManageWalletUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val localPref: LocalConfigurationUseCase,


    ) : BaseScreenModel<PaymentScreenUiState, PaymentScreenUiEffect>(
    PaymentScreenUiState()
), PaymentScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    var outOfStock = mutableStateOf(false)
    val items = _state.value.cartItems
    private val _items: MutableStateFlow< List<CartItemUiState>> =
        MutableStateFlow(listOf())
    val item = _items.asStateFlow()
    companion object {
        val CASH = PaymentMethodsType.CASH
    }
    init {

        viewModelScope.launch(Dispatchers.IO) {
            getCartProducts()
            localPref.saveAddressIDOfCart(addressId)
            localPref.saveCurrentIDOfCart(currencyId)
            localPref.savePromoCodeOfCart(_state.value.promoCode)
            localPref.savePromoOfDiscountCart(promoCode)
            localPref.savePaidFromWalletOfCart(_state.value.amountFromWallet)
        }

//        getCurrentBalance()
        _state.update {
            it.copy(
                subTotal = subTotal,
                promoDiscount = promoCode,
                taxes = taxes,
                total = total,
                currencySymbol = currencySymbol,
                currencyId = currencyId,
                addressId = addressId,
                cartItems = items,
            )
        }
    }

    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toWalletCurrencyUiState()) }
    }

    fun onSuccsesOfPayment(paymentModel: PaymentModel?) {
        Log.d("onSuccsesOfPayment", "onSuccsesOfPayment")
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("kkkkkkkkkkkkkkkkkkkk",paymentModel?.transactionId.toString())
            localPref.saveTranscationCode(paymentModel?.transactionId?:"" )
            localPref.setIsTherePaymentMomo(PaymentMethodsType.MOMO.name)
        }


        onClickPaymentStatus(_state.value.paymentResponse, paymentModel?.transaction?.amount ?: 0.0,  )

    }

    fun onSuccsesOfTrackPaymentTranscation(paymentModel: TransactionModel) {
        _state.update {
            it.copy(


                amount = paymentModel.amount?.toInt() ?: 0,
                content = paymentModel.content ?: "",
                currencyOfPayment = paymentModel.currency ?: "",
                financialTransactionId = paymentModel.financialTransactionId ?: "",
                status = paymentModel.status ?: "",
                statusCode = paymentModel.statusCode ?: 0, isLoading = false,
                statusPaymentMomo = paymentModel.status ?: ""


            )


        }

    }

    private fun onGetCartProductsSuccess(cartDataModel: CartDataModel) {

//_items.value =cartDataModel.cartProducts.map {
//    it.toCartItemUiState()
//}
        _state.update { uiState ->
            uiState.copy(
                items = cartDataModel.cartProducts.map { it.toCartItemUiState() },
                isLoading = false,
                showPayTabsOptionInit = true

                )
        }
    }

   suspend fun getCartProducts() {
        Log.d("getCartProductspayment", "getCartProducts")
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageCartUseCase.getCartProducts() },
            onSuccess = ::onGetCartProductsSuccess,
            onError = { exception ->
                _state.update { it.copy(isLoading  = false) }
                Log.e("CartError", exception.message.toString())
            }
        )

    }

    private suspend fun makeOrder() {
        _state.update { it.copy(isLoading = true) }
//        if (state.value.paymentWay == "wallet") {
        tryToExecute(
            function = {
                manageOrderUseCase.makeOrder(
                    currencyId = _state.value.currencyId,
                    addressId = _state.value.addressId,
                    products = _state.value.items.map { it.toCartItem() },
                    couponCode = _state.value.promoCode,
                    wallet = 0.0,
                    couponDiscount = _state.value.promoDiscount,
                    paymentType = state.value.paymentWay.name
                )
            },
            onSuccess = ::onMakeOrderSuccess,
            onError = { exception ->
                _state.update { it.copy(isLoading = false) }
                Log.e("OrderError", exception.message.toString())
            }
        )

    }

    override fun onWalletBalanceTextChange(text: String) {
        updateState { it.copy(paidFromBalance = text) }
    }

    override fun onMomoPayAccountTextChange(text: String) {
        updateState { it.copy(numberOfAccountMomoPay = text) }
    }

    fun onPayTabsPaymentSuccess(payTabsModel: PayTabsModel?) {
        Log.d("onPayTabsPaymentSuccess", "PayTabsPaymentSuccess")
        _state.update { it.copy(isLoading = true, payTabsModel = payTabsModel,
            showPayTabsOptionInit = false, showPayTabsOptionSelected = false,
            showPayTabsOptionPaymentWebview = true) }
//        _state.update { it.copy(isLoading = false, orderUiState = orderResponse.toOrderUiState()) }
    }
    override fun paytabsInitiate(order_id: Int) {
        Log.d("paytabsInitiate", "paytabsInitiate")
        _state.update { it.copy(isLoading = true, orderId = order_id,
            showPayTabsOptionInit = false) }
        tryToExecute(
            function = { manageCartUseCase.paytabsInitiate(order_id) },
            onSuccess = ::onPayTabsPaymentSuccess,
            onError = { exception ->
                _state.update { it.copy(isLoading  = false, showPayTabsOptionSelected = false) }
                Log.e("paytabsInitiateError", exception.message.toString())
            }
        )
    }

    override fun paytabsCheckStatus(tran_ref: String?) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageCartUseCase.paytabsCheckStatus(tran_ref = tran_ref) },
            onSuccess = ::onPayTabsCheckStatusSuccess,
            onError = { exception ->
                _state.update { it.copy(isLoading  = false, showPayTabsOptionSelected = false) }
                Log.e("paytabsInitiateError", exception.message.toString())
            }
        )
    }
    fun onPayTabsCheckStatusSuccess(payTabsModel: PayTabsModel?) {
        Log.d("PayTabsChackStatus", "Status: ${payTabsModel?.status}")
        _state.update { it.copy(isLoading = false, payTabsModel = payTabsModel,
            showPayTabsOptionInit = false, showPayTabsOptionSelected = false,
            showPayTabsOptionPaymentWebview = false, showPayTabsStatus = true) }
    }

    fun makePayment() {
        Log.d("makePaymentscreenModel", "makePayment")
        _state.update { it.copy(isLoadingStatusPayment = true) }
        tryToExecute(


        function = {
                manageOrderUseCase.makePayment(
                    amount = ceil(state.value.total),
                    concurency = state.value.currencySymbol,
                    partyID = state.value.numberOfAccountMomoPay,
                    partyIDType = _state.value.momoWay,
                    payerNote = _state.value.items.map { it.product.title }.toString() ,
                    payerMessage = _state.value.items.map { it.product.title }.toString()
                )
            },
            onSuccess = ::onSuccsesOfPayment,
            onError = {     _state.update { it.copy(isLoadingStatusPayment = false) }}
        )
    }

    fun trackPaymentTransactionOfMomo() {
        var transactionOfMomoCode = ""
        viewModelScope.launch(Dispatchers.IO) {

            transactionOfMomoCode = localPref.getTransactionCode()
        }
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = {
                manageOrderUseCase.trackTranscationOfMomo(transactionOfMomoCode, amount = ceil(state.value.total),
                    concurency = state.value.currencySymbol,
                    partyID = state.value.numberOfAccountMomoPay,
                    partyIDType = _state.value.momoWay,
                    payerNote = _state.value.items.map { it.product.title }.toString() ,
                    payerMessage = _state.value.items.map { it.product.title }.toString())
            },
            onSuccess = ::onSuccsesOfTrackPaymentTranscation,
            onError = { Log.d("makePaymentscreenModelonError", it.message.toString()) }
        )
    }

    fun getCurrentBalance() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageWalletUseCase.getCurrentBalance() },
            onSuccess = ::onGetCurrentBalanceSuccess,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetCurrentBalanceSuccess(balance: BalanceModel) {
        _state.update { it.copy(balance = balance.balance, isLoading = false) }
    }

    private fun onMakeOrderSuccess(orderResponse: OrderResponseModel) {
//        _state.value.orderUiState = orderResponse.toOrderUiState()
        if (_state.value.paymentWay == PaymentMethodsType.PAYTABS) {
            paytabsInitiate(orderResponse.toOrderUiState().orderId)
        } else {
            _state.update { it.copy(isLoading = false, orderUiState = orderResponse.toOrderUiState()) }
        }
    }

    override fun onClickUpButton() {
        sendNewEffect(PaymentScreenUiEffect.OnNavigateBack)
    }

    override fun onClickPlaceOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            getCartProducts()
            checkIfProductOutOfStock()
            if (!outOfStock.value){
                Log.d("onClickPlaceOrder","notoutofstock")
                makeOrder()

            }
            Log.d("onClickPlaceOrder","outofstock")

        }
    }
suspend fun  checkIfProductOutOfStock(){
    for (item in _state.value.items) {
        Log.d("for in qunantity", "for in qunantity")

        if (
            item.product.currentStock < 1
        ) {
            outOfStock.value = true
            Log.d("quantity out of stock", "quantity out of stock")
        }

    }
}
    override fun onClickTrackOrder(orderId: Int) {
        _state.update { it.copy(orderUiState = OrderUiState()) }
        sendNewEffect(PaymentScreenUiEffect.OnNavigateToTrackOrder(orderId))
    }

    override fun onClickWallet(isWalletSelected: Boolean) {
        _state.update { uiState -> uiState.copy(
            showWallatDialog = true,
            isPayTabSelected = false,
            isMomoPaySelected = false,
            isWalletSelected = isWalletSelected)
        }
    }

    override fun onClickMomoPay(isMomoPaySelected: Boolean) {
        _state.update { uiState -> uiState.copy(
                showMomoPayDialog = true,
                isWalletSelected = false,
                isPayTabSelected = false,
                isMomoPaySelected = isMomoPaySelected
            )
        }
    }

    override fun onClickPayTabs(isPayTabSelected: Boolean) {
        _state.update { uiState -> uiState.copy(
            showPayTabsOptionSelected = true,
            isWalletSelected = false,
            isMomoPaySelected = false,
            isPayTabSelected = isPayTabSelected,
            paymentWay = PaymentMethodsType.PAYTABS
            )
        }
    }

    override fun onCheckCash(isCheck: Boolean) {
        if (isCheck) {
            _state.update { uiState ->
                uiState.copy(paymentWay = PaymentMethodsType.CASH)
            }
        } else {
            _state.update { uiState ->
                uiState.copy(paymentWay = PaymentMethodsType.NONE)
            }

        }
        _state.update { uiState ->
            uiState.copy(cashCheckBox = isCheck)
        }
    }

    fun handleCheckBoxOfMomo(
        isCheckMobileNumber: Boolean = false,
        isCheckPartyCode: Boolean = false,
        isCheckEmail: Boolean = false
    ) {
        if (isCheckMobileNumber) {

            _state.update { uiState ->
                uiState.copy(
                    emailCheckBox = false,
                    partyCodeCheckBox = false,
                    mobileNumberCheckBox = true,
                    momoWay = "MSISDN"
                )
            }
        } else if (isCheckPartyCode) {
            _state.update { uiState ->
                uiState.copy(
                    emailCheckBox = false,
                    mobileNumberCheckBox = false,
                    partyCodeCheckBox = true,
                    momoWay = "PARTY_CODE"
                )
            }
        } else {
            _state.update { uiState ->
                uiState.copy(
                    emailCheckBox = true,
                    partyCodeCheckBox = false,
                    mobileNumberCheckBox = false,
                    momoWay = "EMAIL"
                )
            }

        }


    }

    override fun onClickBackToHome() {
        _state.update { it.copy(orderUiState = OrderUiState()) }
        sendNewEffect(PaymentScreenUiEffect.OnNavigateToHome)
    }

    override fun onClickAddNewCredit() {
        _state.update { it.copy(addCreditState = true) }
    }

    override fun onClickDismissAddCredit() {
        _state.update { it.copy(addCreditState = false) }
    }

    override fun onClickExpandDebitCards() {
        _state.update { it.copy(expandDebitCards = it.expandDebitCards.not()) }
    }

    override fun onClickNotification() {
        sendNewEffect(PaymentScreenUiEffect.OnNavigateToNotificationScreen)
    }

    override fun onClickPaymentStatus(status: StatusPaymentType, amount: Double) {
        _state.update { it.copy(orderUiState = OrderUiState()) }
        sendNewEffect(PaymentScreenUiEffect.OnNavigateToStatusPaymentScreen(amount, status,_state.value.orderUiState.orderId,))
    }

    override fun onDismissWalletDailog() {
        _state.update { it.copy(showWallatDialog = false, isWalletSelected = false, paymentWay = PaymentMethodsType.NONE) }

    }

    override fun onDismissMomoPayDailog() {
        _state.update { it.copy(showMomoPayDialog = false, isMomoPaySelected = false, paymentWay = PaymentMethodsType.NONE) }
    }

    override fun onDismissPayTabsView() {
        _state.update { it.copy(showPayTabsOptionSelected = false, isPayTabSelected = false, paymentWay = PaymentMethodsType.NONE) }
    }

    override fun onChosseWalletAsPayment() {
        _state.update {
            it.copy(
                paymentWay = PaymentMethodsType.WALLET,
                amountFromWallet = it.paidFromBalance.toDouble()
            )
        }


    }

    override fun onChosseCashAsPayment() {
        _state.update { it.copy(paymentWay = PaymentMethodsType.CASH) }
    }

    override fun deletePayment() {
        _state.update { it.copy(paymentWay = PaymentMethodsType.NONE) }
    }
}