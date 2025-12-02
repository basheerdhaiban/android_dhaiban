package com.semicolon.dhaiban.presentation.paymentstatus

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.cart.toCartAddressUiState
import com.semicolon.dhaiban.presentation.cart.toCartItem
import com.semicolon.dhaiban.presentation.cart.toCartItemUiState
import com.semicolon.dhaiban.presentation.home.HomeScreenModel
import com.semicolon.dhaiban.presentation.payment.PaymentScreenUiEffect
import com.semicolon.dhaiban.presentation.paymentstatus.toOrderUiState
import com.semicolon.dhaiban.presentation.wallet.toWalletCurrencyUiState
import com.semicolon.dhaiban.utils.PaymentMethodsType
import com.semicolon.domain.entity.BalanceModel
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PaymentScreenStatusModel(

    private val manageOrderUseCase: ManageOrderUseCase,
    private val manageWalletUseCase: ManageWalletUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val localPref: LocalConfigurationUseCase,


    ) : BaseScreenModel<PaymentStatusScreenUiState, PaymentScreenStatusUiEffect>(
    PaymentStatusScreenUiState()
), PaymentScreenstatusInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    val items = _state.value.cartItems

    init {
        getCartProducts()

        trackPaymentTransactionOfMomo()
    }

    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toWalletCurrencyUiState()) }
    }



    fun onSuccsesOfTrackPaymentTranscation(paymentModel: TransactionModel) {
        viewModelScope.launch(Dispatchers.IO) {
            if (paymentModel.status == "PENDING") {
                Log.d("PENDING","PENDING")

                localPref.setIsTherePaymentMomo(PaymentMethodsType.MOMO.name)
                _state.update {
                    it.copy(
                        isLoading = false,
                        amount = paymentModel.amount?.toInt() ?: 0,
                        content = paymentModel.content ?: "",
                        currencyOfPayment = paymentModel.currency ?: "",
                        financialTransactionId = paymentModel.financialTransactionId ?: "",
                        status = paymentModel.status ?: "",
                        statusCode = paymentModel.statusCode ?: 0,
                        statusPaymentMomo = paymentModel.status ?: ""


                    )


                }
            } else if (paymentModel.status == "SUCCESSFUL") {
                Log.d("SUCCESSFUL","SUCCESSFUL")

                makeOrder()
                localPref.setIsTherePaymentMomo("SUCCESSFUL")
                _state.update {
                    it.copy(

                        amount = paymentModel.amount?.toInt() ?: 0,
                        content = paymentModel.content ?: "",
                        currencyOfPayment = paymentModel.currency ?: "",
                        financialTransactionId = paymentModel.financialTransactionId ?: "",
                        status = paymentModel.status ?: "",
                        statusCode = paymentModel.statusCode ?: 0,
                        statusPaymentMomo = paymentModel.status ?: ""


                    )


                }

            } else {
                Log.d("Other","Other")

                localPref.setIsTherePaymentMomo("Rejected")
                _state.update {
                    it.copy(
                        isLoading = false,
                        amount = paymentModel.amount?.toInt() ?: 0,
                        content = paymentModel.content ?: "",
                        currencyOfPayment = paymentModel.currency ?: "",
                        financialTransactionId = paymentModel.financialTransactionId ?: "",
                        status = paymentModel.status ?: "",
                        statusCode = paymentModel.statusCode ?: 0,
                        statusPaymentMomo = paymentModel.status ?: ""


                    )


                }

            }
        }



    }









    fun trackPaymentTransactionOfMomo() {

        viewModelScope.launch(Dispatchers.IO) {

            val transactionOfMomoCode = localPref.getTransactionCode()
Log.d("ssssssssss",transactionOfMomoCode)
            _state.update { it.copy(isLoading = true) }
            tryToExecute(
                function = {
                    manageOrderUseCase.trackTranscationOfMomo(transactionOfMomoCode)
                },
                onSuccess = ::onSuccsesOfTrackPaymentTranscation,
                onError = {
                    _state.update { it.copy(isLoading = false) }

                    Log.d("makePaymentscreenModelonError", it.message.toString())
                }
            )
        }
    }





    override fun onClickUpButton() {
        sendNewEffect(PaymentScreenStatusUiEffect.OnNavigateBack)
    }

    fun  getCartProducts() {
        Log.d("getCartProducts", "getCartProducts")

        tryToExecute(function = { manageCartUseCase.getCartProducts() },
            onSuccess = ::onGetCartProductsSuccess,
            onError = { exception ->

                Log.e("CartError", exception.message.toString())
            })

    }
    private fun onGetCartProductsSuccess(cartDataModel: CartDataModel) {


        _state.update { uiState ->
            uiState.copy(
                discount = 0.0,
                isLoading = false,
                items = cartDataModel.cartProducts.map { it.toCartItemUiState() },
                cartAddress = cartDataModel.defaultAddress.toCartAddressUiState()
            )
        }
    }


    private fun makeOrder() {
        Log.d("MakeOrderHomeScreen","MakeOrderHomeScreen")
        _state.update { it.copy(isLoading = false) }
        tryToExecute(
            function = {

                manageOrderUseCase.makeOrder(
                    currencyId = localPref.getCurrencyId(),
                    addressId = localPref.getAddressIdOfCart(),
                    products = _state.value.items.map { it.toCartItem() },
                    couponCode = localPref.getPromoCodeFromCart(),
                    wallet = localPref.getPaidFromWallet(),

                    localPref.getPromoOfDiscountCart(),
                    paymentType = PaymentMethodsType.MOMO.name,
                    paymentStatus = true
                )
            },
            onSuccess = ::onMakeOrderSuccess,
            onError = { exception ->
                _state.update { it.copy(isLoading = false) }
                Log.e("OrderError", exception.message.toString())
            }
        )
    }


    private fun onMakeOrderSuccess(orderResponse: OrderResponseModel) {
        _state.update { uiState ->
            uiState.copy( isLoading = false , orderUiState = orderResponse.toOrderUiState())
        }
    }



    override fun onClickBackToHome() {
        _state.update { it.copy(orderUiState = OrderUiState()) }
        sendNewEffect(PaymentScreenStatusUiEffect.OnNavigateToHome)
    }

    override fun onClickTrackOrder(orderId: Int) {
        _state.update { it.copy(orderUiState =OrderUiState()) }
        sendNewEffect(PaymentScreenStatusUiEffect.OnNavigateToTrackOrder(orderId))    }


    override fun onClickNotification() {
        sendNewEffect(PaymentScreenStatusUiEffect.OnNavigateToNotificationScreen)
    }

}