package com.semicolon.dhaiban.presentation.cart

import android.util.Log
import androidx.compose.ui.util.fastCbrt
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.app.toStringResource
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.dhaiban.presentation.sharedUiState.toUiState
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.ChoiceItemModel
import com.semicolon.domain.entity.CouponModel
import com.semicolon.domain.entity.ProductOFCoupon
import com.semicolon.domain.entity.productdetails.CartDataModel
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageCartUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.ManageOrderUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CartScreenModel(

    private val cartAddress: CartAddressUiState,
    private val manageCartUseCase: ManageCartUseCase,
    private val localConfigurationUseCase: LocalConfigurationUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase,
    private val manageOrderUseCase: ManageOrderUseCase,
    private val localPref: LocalConfigurationUseCase,
    private val authentication: UserAuthenticationUseCase,
    private val userConfiguration: LocalConfigurationUseCase,

    ) :
    BaseScreenModel<CartScreenUiState, CartScreenUiEffect>(CartScreenUiState()),
    CartScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    private val _countOfCart: MutableStateFlow<Int> =
        MutableStateFlow(0)
    val countOfCart = _countOfCart.asStateFlow()
    private val _message: MutableStateFlow<String> =
        MutableStateFlow("")
    val message = _message.asStateFlow()

    init {
        if (cartAddress.addressId != 0 && cartAddress.addressName != "") {
            _state.update { it.copy(cartAddress = cartAddress) }
        }

        getCartProducts()
        getCurrencySymbol()
        getUserData()
        getCurrencyId()
        getCartProducts()
        getCountOfUnreadNotification()
    }
    private fun getData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            function = { authentication.getAppConfig().toUiState() },
            onSuccess = ::onGetListSuccess,
            onError = ::onError
        )
    }



    private fun saveLocalStringFile(langCode: String) {
        tryToExecute(
            function = { localConfigurationUseCase.saveLocalizationLanguage("", langCode) },
            onSuccess = {},
            onError = {}
        )
    }
    private fun getLatestStringFile() {
        viewModelScope.launch(Dispatchers.IO) {
            userConfiguration.getStrings().collectLatest { strings ->
                _state.update {
                    it.copy(
                        stringRes = strings.toStringResource(),
                        isLoading = false,
                        layoutDirection = userConfiguration.getLayoutDirection()
                    )
                }
            }
        }
    }
    private fun onGetUserDataSuccess(userData: com.semicolon.dhaiban.presentation.profile.UserDataUiState) {
        _state.update {
            it.copy(
                userData = UserDataUiState(
                    username = userData.username.ifEmpty { "Guest" },

                    isAuthenticated = userData.isAuthenticated
                )
            )
        }
    }
    private fun saveLocalStringFile(url: String, langCode: String) {
        tryToExecute(
            function = { userConfiguration.saveLocalizationLanguage(url, langCode) },
            onSuccess = ::onSaveLocalStringFileSuccess,
            onError = {}
        )
    }
    private fun onSaveLocalStringFileSuccess(unit: Unit) {
        getLatestStringFile()
        viewModelScope.launch { userConfiguration.saveStringResourceState(true) }
    }


    fun getDefaultData() {
        tryToExecute(
            function = { authentication.getAppConfig().toUiState() },
            onSuccess = ::onGetListSuccess,
            onError = ::onError
        )
    }
    private fun onError(exception: Exception) {
        _state.update { it.copy(isLoading = false, errorMessage = "Network Error") }
    }

    private fun initiateLocalStrings() {

        _state.update { it.copy(isLoading = true) }

        screenModelScope.launch(Dispatchers.IO) {
            val currentLangCode = "ltr"
//                async { userConfiguration.getUserLanguage() }.await()
            val currentLayoutDirection =
                async { userConfiguration.getLayoutDirection() }.await()
            Log.d("currentLayoutDirectionf",currentLayoutDirection)

            val defaultLangCode =
                _state.value.appConfig.languages.find { it.id == _state.value.appConfig.defaultLang.id }?.code
                    ?: "en"
            val defaultLanguageUrl =
                _state.value.appConfig.defaultLang.url
            val defaultLayoutDirection = "ltr"
//                _state.value.appConfig.languages.find { it.id == _state.value.appConfig.defaultLang.id }?.dir
//                    ?: "ltr"
            Log.d("currentLayoutDirectionl",defaultLayoutDirection)
            val appConfig = _state.value.appConfig
            Log.d("appConfig", appConfig.toString())
            Log.d("defaultLangId", appConfig.defaultLang.id.toString())

// Log the languages list
            appConfig.languages.forEach { lang ->
                Log.d("language", "id: ${lang.id}, dir: ${lang.dir}")
            }
            val isStringResSaved = async { userConfiguration.readStringResourceState() }

            if (isStringResSaved.await()) {
                getLatestStringFile()
            } else {
                saveLocalStringFile(
                    url = defaultLanguageUrl,
                    langCode = currentLangCode.ifEmpty { defaultLangCode })
            }
            userConfiguration.saveLayoutDirection(currentLayoutDirection.ifEmpty { defaultLayoutDirection })
            _state.update {
                it.copy(
                    layoutDirection = currentLayoutDirection.ifEmpty { defaultLayoutDirection },
                )
            }
        }
    }
    private fun onGetListSuccess(appConfig: AppConfigUiState) {
        Log.e("AppConfigAppScreenModel", appConfig.defaultLang.toString())
        updateState { it.copy(appConfig = appConfig) }
        initiateLocalStrings()

    }

    private fun getCountOfUnreadNotification() {
        _state.update { it.copy(isLoading = true, errorMessage = "") }
        tryToExecute(
            function = { manageNotificationUseCase.getUnReadNotificationCount() },
            onSuccess = {
                if (it != null) {
                    _state.update { screenUiState ->
                        screenUiState.copy(
                            isLoading = false,
                            countOfUnreadMessage = it.notifications
                        )
                    }
                }

            },
            onError = {}
        )
    }

    fun getUserData() {
        viewModelScope.launch {
            Log.d("getUserData",localConfigurationUseCase.getUserToken().toString())

        }
        tryToExecute(
            function = {
                UserDataUiState(
                    username = localConfigurationUseCase.getUserName(),
                    isAuthenticated = localConfigurationUseCase.getUserToken().isNotEmpty()
                )
            },
            onSuccess = ::onGetUserDataSuccess,
            onError = {}
        )

    }

    fun updateCurrencySymbol(symbol: String) {
        _state.update { it.copy(currencySymbol = symbol) }
    }

    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toCartCurrencyUiState()) }
    }

    private fun onGetUserDataSuccess(userData: UserDataUiState) {
        _state.update {
            it.copy(
                userData = UserDataUiState(
                    userData.username,
                    userData.isAuthenticated
                )
            )
        }
    }

    fun getCartProducts() {
        Log.d("getCartProducts", "getCartProducts")
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            localPref.saveIsFromCart(true)

        }
        tryToExecute(
            function = { manageCartUseCase.getCartProducts() },
            onSuccess = ::onGetCartProductsSuccess,
            onError = { exception ->
                _state.update { it.copy(isLoading = false) }
                Log.e("CartError", exception.message.toString())
                viewModelScope.launch {
                    localPref.saveIsFromCart(false)

                }

            }
        )

    }

    fun getDiscountCoupon(coupon: String, products: List<ProductOFCoupon>) {
        viewModelScope.launch(Dispatchers.IO) {
            localPref.saveIsFromCart(true) }
        _state.update { it.copy(isLoadingForCoupon = true , isLoading = true) }
        tryToExecute(
            function = {
                manageOrderUseCase.makeDiscountCoupon(
                    coupon, products

                )
            },
            onSuccess = ::onSuccessDiscountCoupon,
            onError = { exception ->
                viewModelScope.launch {
                    localPref.saveIsFromCart(false)

                }
                _state.update { it.copy(isLoadingForCoupon = false, isLoading = false) }
                Log.e("OrderError", exception.message.toString())
            }
        )
    }

    private fun onSuccessDiscountCoupon(coupon: CouponModel) {
        Log.d("onSuccessDiscountCoupon", coupon.discount.toString())
        viewModelScope.launch {
            localPref.saveIsFromCart(flag = false)

        }
        _state.update {
            it.copy(
                products = coupon.products.map { it.toProductUiState() },
                isLoadingForCoupon = false,
                notValidAmountForCoupoan = coupon.products.any { it.discount?:0.0 > it.price?:0.0 },
                discount = if(coupon.products.any { it.discount?:0.0 > it.price?:0.0 }) 0.0 else coupon.discount,
                discountOfCoupoan = coupon.discount,
                message = coupon.message,
                shouldShowCouponToast = true , isLoading = false
            )
        }


    }

    fun getNumberOfItemInCart(cartDataModel: CartDataModel): Int {
        val count = cartDataModel.cartProducts.count()
        Log.d("getNumberOfItemInCart", count.toString())
        _countOfCart.value = count
        return count
    }

    private fun removeFromCart(id: Int) {
        viewModelScope.launch {
            localPref.saveIsFromCart(true)

        }
        tryToExecute(
            function = { manageCartUseCase.removeFromCart(id) },
            onSuccess = ::onDeleteFromCartSuccess,
            onError = {
                viewModelScope.launch {
                    localPref.saveIsFromCart(false)

                }
                sendNewEffect(CartScreenUiEffect.OnFailedDelete)
            }
        )

    }

    private fun editCartItem(
        identifier: Long,
        serializedOptionsUiState: SerializedOptionsUiState,
        variant: String,
        productId: Int,
        quantity: Int
    ) {
        viewModelScope.launch {
            localPref.saveIsFromCart(true)

        }
        tryToExecute(
            function = {
                manageCartUseCase.addToCart(
                    identifier = identifier,
                    quantity = quantity,
                    currencyId = _state.value.currencyId,
                    productId = productId,
                    variant = variant,
                    colorId = if (serializedOptionsUiState.color == 0) null else serializedOptionsUiState.color,
                    variantList = serializedOptionsUiState.choices.map {
                        ChoiceItemModel(
                            it.id,
                            it.title,
                            it.parentId,
                            it.parentTitle
                        )
                    }
                )
            },
            onSuccess = {
                viewModelScope.launch {
                    localPref.saveIsFromCart(false)

                }
                _state.update {
                    it.copy(items = it.items.map { cartItem ->
                        if (cartItem.identifier == identifier) {
                            // Update the quantity for the specific item
                            cartItem.copy(quantity = quantity)
                        } else {
                            // Keep other items unchanged
                            cartItem
                        }
                    })
                }
                val product =state.value.items.map { it.toCouponModel() }
                if (state.value.promoCode.isNotBlank()) {
                    getDiscountCoupon(
                        state.value.promoCode, product
                    )
                }
            },
            onError = {
                viewModelScope.launch {
                    localPref.saveIsFromCart(false)

                }
                Log.e("error", it.message.toString())
            }
        )

    }

    private fun getCurrencyId() {
        viewModelScope.launch {
            localPref.saveIsFromCart(true)

        }
        tryToExecute(
            function = { localConfigurationUseCase.getCurrencyId() },
            onSuccess = { currencyId -> _state.update { it.copy(currencyId = currencyId) }
                viewModelScope.launch {
                    localPref.saveIsFromCart(false)

                }
                        },
            onError = {     viewModelScope.launch {
                localPref.saveIsFromCart(false)

            }}
        )

    }

    private fun getCurrencySymbol() {
        tryToExecute(
            function = { localConfigurationUseCase.getUserCurrency() },
            onSuccess = { currencySymbol -> _state.update { it.copy(currencySymbol = currencySymbol) } },
            onError = {}
        )

    }

    override suspend fun onClickDelete(cartItemId: Int) {
        removeFromCart(cartItemId)
        Log.d("onClickDelete", state.value.promoCode)
        delay(3000)
        Log.d("onClickDeletedelay", state.value.promoCode)

        if (state.value.promoCode != "") {
            getDiscountCoupon(state.value.promoCode, state.value.productsOFCoupon)

        }
    }

    override fun onCLickPlus(
        identifier: Long,
        serializedOptionsUiState: SerializedOptionsUiState,
        variant: String,
        productId: Int,
        quantity: Int,
        availableStockState: Boolean
    ) {
        if (availableStockState) {
            editCartItem(identifier, serializedOptionsUiState, variant, productId, quantity + 1)
        } else {
            _state.update { it.copy(errorMessage = "Not Available") }
            sendNewEffect(CartScreenUiEffect.OnShowMessage)
        }
    }

    override fun onCLickMinus(
        identifier: Long,
        serializedOptionsUiState: SerializedOptionsUiState,
        variant: String,
        productId: Int,
        quantity: Int
    ) {
        if (quantity > 1)
            editCartItem(identifier, serializedOptionsUiState, variant, productId, quantity - 1)
    }

    override fun onClickAddress() {
        sendNewEffect(CartScreenUiEffect.OnNavigateToAddress)
    }

    override fun onClickContinueToPayment() {
        if (_state.value.cartAddress.addressId != 0 && _state.value.cartAddress.addressName != "") {
            sendNewEffect(CartScreenUiEffect.OnNavigateToPaymentScreen)
        } else {
            _state.update { it.copy(errorMessage = "Address Needed") }
            sendNewEffect(CartScreenUiEffect.OnShowMessage)
        }
    }

    override fun onClickAddMore() {
        sendNewEffect(CartScreenUiEffect.OnNavigateToHome)
    }

    override fun onClickCartItem(productId: Int, variant: String) {
        sendNewEffect(CartScreenUiEffect.OnNavigateToProductScreen(productId, variant))
    }

    private fun onDeleteFromCartSuccess(cartItems: List<CartItem>) {
        viewModelScope.launch {
            localPref.saveIsFromCart(false)

        }
        _state.update { uiState -> uiState.copy(items = cartItems.map { it.toCartItemUiState() }) }
        sendNewEffect(CartScreenUiEffect.OnUpdateCartCount(cartItems.size))
    }

    private fun onGetCartProductsSuccess(cartDataModel: CartDataModel) {
        getNumberOfItemInCart(cartDataModel = cartDataModel)
        viewModelScope.launch(Dispatchers.IO) {
            localPref.saveIsFromCart(false)

            val transcationCode = localPref.getTransactionCode()
            val momoOrNot = localPref.getIsTherePaymentMomo()

            Log.d("CartScreenModeltran", momoOrNot)
            _state.update { uiState ->
                uiState.copy(transcationCode =transcationCode, momo = momoOrNot)
            }

        }

        _state.update { uiState ->
            uiState.copy(
                discount = 0.0,
                isLoading = false,
                items = cartDataModel.cartProducts.map { it.toCartItemUiState() },
                cartAddress =
                if (cartAddress.addressId != 0 && cartAddress.addressName != "") CartAddressUiState(
                    cartAddress.addressId,
                    cartAddress.addressName
                ) else cartDataModel.defaultAddress.toCartAddressUiState()
            )
        }
    }

    override fun onClickNotification() {
        sendNewEffect(CartScreenUiEffect.OnNavigateToNotificationScreen)
    }
}