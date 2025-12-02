package com.semicolon.dhaiban.presentation.product

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.cart.CartAddressUiState
import com.semicolon.dhaiban.presentation.cart.toCartAddressUiState
import com.semicolon.dhaiban.presentation.cart.toCartItemUiState
import com.semicolon.dhaiban.presentation.home.UserDataUiState
import com.semicolon.domain.entity.ChoiceItemModel
import com.semicolon.domain.entity.productdetails.CartDataModel
import com.semicolon.domain.entity.productdetails.VariantPrice
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageCartUseCase
import com.semicolon.domain.usecase.ManageFavoritesUseCase
import com.semicolon.domain.usecase.ManageProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductScreenModel(
    private val productId: Int,
    private var variant: String,
    private val manageProductUseCase: ManageProductUseCase,
    private val manageFavoritesUseCase: ManageFavoritesUseCase,
    private val manageCartUseCase: ManageCartUseCase,
    private val localConfigurationUseCase: LocalConfigurationUseCase
) :
    BaseScreenModel<ProductScreenUiState, ProductScreenUiEffect>(
        ProductScreenUiState()
    ), ProductScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    private val _count = MutableStateFlow(_state.value.items)
    private val count = _count.asStateFlow()




    init {
        getCartProducts ()
        getCurrencyId()
        getDetails(productId)
        getRecommendedProducts(productId)
        getProductReviews(productId)
    }

    fun updateCurrencySymbol(symbol: String) {
        _state.update { it.copy(currencySymbol = symbol) }
    }


    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toProductCurrencyUiState()) }
    }


    private fun getDetails(productId: Int, fromDetailsScreen: Boolean = false) {
        if (!fromDetailsScreen)
            _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageProductUseCase.getProductDetails(productId).toEntity() },
            onSuccess = ::onGetDetailsSuccess,
            onError = {
                Log.e("error", it.message.toString())
            }
        )
    }

    private fun getProductReviews(productId: Int) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = {
                manageProductUseCase
                    .getProductReviews(productId)
                    .reviews.map { it.toReviewUiState() }
            },
            onSuccess = ::onGetProductReviewsSuccess,
            onError = {}
        )
    }

    private fun getRecommendedProducts(productId: Int) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = {
                manageProductUseCase.getRecommendedProducts(productId)
                    .map { it.toProductUiState() }
            },
            onSuccess = ::onGetRecommendedProductsSuccess,
            onError = {
                Log.e("error", it.message.toString())
            }
        )
    }

    private fun getVariantPrice(productId: Int, colorId: Int?, variantList: List<Int>) {
        tryToExecute(
            function = {
                manageProductUseCase.getVariantPrice(productId, colorId, variantList)
            },
            onSuccess = ::onGetVariantPriceSuccess,
            onError = {
                //TODO handle error if variant come as string not object or i mean got error
//                sendNewEffect(ProductScreenUiEffect.OnVariantNotAvailable)
            }
        )
    }

    private fun changeFavoriteState(productState: Boolean, productId: Int) {
        tryToExecute(
            function = {
                if (productState) {
                    manageFavoritesUseCase.removeProductFromFavorite(productId)
                } else {
                    manageFavoritesUseCase.addProductToFavorite(productId)
                }
            },
            onSuccess = ::onChangeFavoriteStateSuccess,
            onError = {}
        )
    }
    fun getCartProducts() {
Log.d("getCartProducts product screen Model " ,count.value.count().toString())
        tryToExecute(
            function = { manageCartUseCase.getCartProducts() },
            onSuccess = ::onGetCartProductsSuccess,
            onError = { exception ->
                _state.update { it.copy(isLoading = false) }
                Log.e("CartError", exception.message.toString())
            }
        )

    }
    private fun onGetCartProductsSuccess(cartDataModel: CartDataModel) {


        _state.update { uiState ->
            uiState.copy(

                items = cartDataModel.cartProducts.map { it.toCartItemUiState() },

            )
        }
    }

    private fun addToCart(productId: Int, quantity: Int, variant: String) {
        print("currencyId: " + _state.value.currencyId)
        val variantList = _state.value.productDetails.choiceOptions.map { choiceOptionUiState ->
            val choice =
                choiceOptionUiState.options.find { it.id == choiceOptionUiState.selectedOptionId }
            ChoiceItem(
                choice!!.id,
                choice.title,
                choiceOptionUiState.id,
                choiceOptionUiState.choiceTitle
            )
        }
        _state.update { it.copy(addToCartLoadingState = true) }
        if (_state.value.userData.isAuthenticated) {
            tryToExecute(
                function = {
                    manageCartUseCase.addToCart(
                        identifier =
                        if (_state.value.productDetails.identifier == 0L)
                            null
                        else
                            _state.value.productDetails.identifier,
                        productId = productId,
                        quantity = quantity,
                        variant = variant,
                        currencyId = _state.value.currencyId,
                        colorId = if (_state.value.productDetails.selectedColorId == 0) null
                        else
                            _state.value.productDetails.selectedColorId,
                        variantList = variantList.map {
                            ChoiceItemModel(
                                it.id,
                                it.title,
                                it.parentId,
                                it.parentTitle
                            )
                        }
                    ) to quantity
                },
                onSuccess = ::onAddToCartSuccess,
                onError = { exception ->
                    Log.e("Error", exception.toString())
                    if (exception.message.toString() == "You are not logged in") {
                        _state.update { it.copy(showDialog = true) }
                    }
                    _state.update { it.copy(addToCartLoadingState = false) }
                }
            )
        } else {
            _state.update { it.copy(showDialog = true, addToCartLoadingState = false) }
        }

    }

    private fun getCurrencyId() {
        tryToExecute(
            function = { localConfigurationUseCase.getCurrencyId() },
            onSuccess = { currencyId ->
                _state.update { it.copy(currencyId = currencyId) }
            },
            onError = {}
        )
    }

    private fun onChangeFavoriteStateSuccess(message: String) {
        if (message.isNotEmpty()) {
            _state.update { uiState ->
                uiState.copy(
                    productDetails = uiState.productDetails.copy(
                        isFavourite = uiState.productDetails.isFavourite.not()
                    )
                )
            }
        }
    }

    private fun onGetVariantPriceSuccess(variantPrice: VariantPrice) {
        _state.update { uiState ->
            uiState.copy(
                productDetails = uiState.productDetails.copy(
                    variantPrice = variantPrice.price,
                    stockCount = variantPrice.stockCount,
                    variantQuantity = variantPrice.stockCount
                )
            )
        }
        Log.e("variantQuantity", _state.value.productDetails.variantQuantity.toString())
    }

    private fun onGetRecommendedProductsSuccess(products: List<ProductUiState>) {
        _state.update { it.copy(isLoading = false, recommendedProducts = products) }
    }



    private fun onGetDetailsSuccess(productDetails: ProductDetailsUiState) {

        viewModelScope.launch(Dispatchers.IO) {
            val transcationCode = localConfigurationUseCase.getTransactionCode()
            val momoOrNot = localConfigurationUseCase.getIsTherePaymentMomo()
            Log.d("CartScreenModeltran", momoOrNot)
            _state.update { uiState ->
                uiState.copy(transcationCode =transcationCode, momo = momoOrNot)
            }

        }
        _state.update { it.copy(productDetails = productDetails) }
        if (_state.value.productDetails.cart.isNotEmpty()) {
            variant = _state.value.productDetails.cart.first().variant
        }
        if (variant.isEmpty())
            onClickColor(productDetails.colors.firstOrNull()?.id ?: 0)
        else {
            val listOfChoices = variant.split("-").map { it.toInt() }
            if (_state.value.productDetails.colors.isNotEmpty()) {
                _state.update { uiState ->
                    uiState.copy(
                        productDetails = uiState.productDetails.copy(
                            optionSelected = true,
                            selectedColorId = listOfChoices.first(),
                            choiceOptions =
                            uiState.productDetails.choiceOptions.mapIndexed { index, choiceOptionUiState ->
                                choiceOptionUiState.copy(selectedOptionId = listOfChoices[index + 1])
                            }
                        )
                    )
                }
            } else {
                _state.update { uiState ->
                    uiState.copy(
                        productDetails = uiState.productDetails.copy(
                            optionSelected = true,
                            choiceOptions =
                            uiState.productDetails.choiceOptions.mapIndexed { index, choiceOptionUiState ->
                                choiceOptionUiState.copy(selectedOptionId = listOfChoices[index])
                            }
                        )
                    )
                }
            }
            val selectedColorId = _state.value.productDetails.selectedColorId
            val variantList = _state.value.productDetails.choiceOptions.map { it.selectedOptionId }
            if (_state.value.productDetails.choiceOptions.isNotEmpty()) {
                print("onGetDetailsSuccess" + "Variant")
                getVariantPrice(
                    productId = productId,
                    colorId = if (selectedColorId == 0) null else selectedColorId,
                    variantList = variantList
                )
            }
            getIdentifierIfExistAndUpdateState()
            sendNewEffect(ProductScreenUiEffect.OnUpdateCartCount(_state.value.productDetails.cart.size))
        }

        viewModelScope.launch(Dispatchers.IO) {
            delay(10000)
        }
        _state.update { it.copy(isLoading = false, )}
    }

    private fun onGetProductReviewsSuccess(reviews: List<ReviewUiState>) {
        _state.update { it.copy(isLoading = false, productReviews = reviews) }
    }

    fun hideImages() {
        _state.update { it.copy(isExpandImagesSelected = false) }
    }

    override fun onClickSmallImage(imageId: Int) {
        _state.update { productScreenUiState ->
            productScreenUiState.copy(
                productDetails = productScreenUiState.productDetails.copy(
                    selectedImageId = imageId,
                    selectedImage = productScreenUiState.productDetails.productImages
                        .first { it.id == imageId }.imageUrl
                )
            )
        }
    }

    override fun onClickColor(colorId: Int) {
        _state.update { productScreenUiState ->
            productScreenUiState.copy(
                productDetails = productScreenUiState.productDetails.copy(
                    optionSelected = true,
                    selectedColorId = colorId,
                    choiceOptions = productScreenUiState.productDetails.choiceOptions
                        .map { choiceOptionUiState ->
                            val firstId = choiceOptionUiState.options.firstOrNull()?.id ?: 0
                            if (choiceOptionUiState.selectedOptionId == 0) { // if not selected select the first option
                                choiceOptionUiState.copy(
                                    selectedOptionId = firstId
                                )
                            } else choiceOptionUiState
                        }
                )
            )
        }
        val selectedColorId = _state.value.productDetails.selectedColorId
        val variantList = _state.value.productDetails.choiceOptions.map { it.selectedOptionId }
        println("onClickColor" + "variant " + variantList)
        getVariantPrice(
            productId = productId,
            colorId = if (selectedColorId == 0) null else selectedColorId,
            variantList = variantList
        )
        getIdentifierIfExistAndUpdateState()
    }

    override fun onClickUpButton() {
        sendNewEffect(ProductScreenUiEffect.OnNavigateBack)
    }

    override fun onClickRecommendedProduct(productId: Int) {
        sendNewEffect(ProductScreenUiEffect.OnNavigateToProductDetails(productId))
    }
    fun getDigitsAfterDecimal(num: Double): Int {
        val intValue = num.toInt() // Get the integer part
        val decimalPart = num - intValue // Subtract to get only the decimal part
        val decimalAsString = decimalPart.toString().substring(2) // Convert to string and remove "0."
        return decimalAsString.toInt()
    }
    override fun onClickOption(parentChoiceId: Int, optionId: Int) {
        _state.update { productScreenUiState ->
            productScreenUiState.copy(
                productDetails = productScreenUiState.productDetails.copy(
                    optionSelected = true,
                    choiceOptions = productScreenUiState.productDetails.choiceOptions
                        .map { choiceOptionUiState ->
                            val firstId = choiceOptionUiState.options.firstOrNull()?.id ?: 0
                            if (choiceOptionUiState.id == parentChoiceId) { //normal choice
                                choiceOptionUiState.copy(
                                    selectedOptionId = optionId
                                )
                            } else if (choiceOptionUiState.selectedOptionId == 0) { // if not selected select the first option
                                choiceOptionUiState.copy(
                                    selectedOptionId = firstId
                                )
                            } else choiceOptionUiState
                        },
                    selectedColorId =
                    if (_state.value.productDetails.selectedColorId == 0)
                        _state.value.productDetails.colors.firstOrNull()?.id ?: 0
                    else _state.value.productDetails.selectedColorId
                )
            )
        }
        val selectedColorId = _state.value.productDetails.selectedColorId
        val variantList = _state.value.productDetails.choiceOptions.map { it.selectedOptionId }
        print("onClickOption" + "Variant")
        getVariantPrice(
            productId = productId,
            colorId = if (selectedColorId == 0) null else selectedColorId,
            variantList = variantList
        )
        getIdentifierIfExistAndUpdateState()
    }

    private fun getIdentifierIfExistAndUpdateState() {
        val identifier = getVariantIdentifierIfExist()
        if (identifier != 0L) {
            val cartItem = _state.value.productDetails.cart.find { it.identifier == identifier }
            _state.update {
                it.copy(
                    productDetails = it.productDetails.copy(
                        identifier = identifier,
                        cartQuantity = cartItem!!.quantity
                    )
                )
            }
        } else {
            _state.update {
                it.copy(
                    productDetails = it.productDetails.copy(
                        identifier = 0L,
                        cartQuantity = 0
                    )
                )
            }
        }
        val currentVariant = getVariant()
        if (_state.value.productDetails.cart.firstOrNull { it.variant == currentVariant } == null) {
            _state.update { it.copy(isInCart = false) }
        } else {
            _state.update { it.copy(isInCart = true) }
        }
    }

    override fun onClickImage(visibility: Boolean) {
        _state.update { it.copy(isExpandImagesSelected = !visibility) }
    }

    override fun onClickProductFavorite(productId: Int, isFavorite: Boolean) {
        changeFavoriteState(isFavorite, productId)
    }

    override fun onCLickAddToCart(productId: Int, quantity: Int) {
        if (_state.value.productDetails.optionSelected.not()) {
            sendNewEffect(ProductScreenUiEffect.OnNeedVariant)
        } else {
            addToCart(productId, quantity, getVariant())

        }

    }

    override fun onClickCartIcon() {
        sendNewEffect(ProductScreenUiEffect.OnNavigateToCartScreen)
    }

    private fun getVariant(): String {
        val variant =
            if (_state.value.productDetails.selectedColorId != 0) StringBuilder()
                .append(_state.value.productDetails.selectedColorId)
                .append("-") else StringBuilder()
        _state.value.productDetails.choiceOptions.forEach {
            variant.append(it.selectedOptionId).append("-")
        }
        if (variant.lastOrNull() == '-')
            variant.deleteCharAt(variant.length - 1)
        return variant.toString()
    }

    private fun getVariantIdentifierIfExist(): Long {
        return if (_state.value.productDetails.cart.isNotEmpty()) {
            val currentVariant = getVariant()
            _state.value.productDetails.cart.find { it.variant == currentVariant }?.identifier ?: 0L
        } else {
            0L
        }
    }

    override fun showMessage(message: String) {
        sendNewEffect(ProductScreenUiEffect.OnShowMessage(message))
    }

    override fun onDismissAlert() {
        _state.update { it.copy(showDialog = false) }
    }

    override fun onClickLogin() {
        _state.update { it.copy(showDialog = false) }
        getCartProducts ()
        sendNewEffect(ProductScreenUiEffect.OnNavigateToWelcomeScreen)
    }

    override fun onCloseImageViewer() {
        _state.update { it.copy(isExpandImagesSelected = false) }
    }

    fun getUserData() {
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

    private fun onGetUserDataSuccess(userData: UserDataUiState) {
        _state.update {
            it.copy(
                userData = UserDataUiState(
                    userData.username.ifEmpty { "Guest" },
                    userData.imageUrl,
                    userData.isAuthenticated
                )
            )
        }
    }

    private fun onAddToCartSuccess(result: Pair<String, Int>) {
        if (result.first == "Added") {
            getDetails(productId, true)
            _state.update {
                it.copy(
                    addToCartLoadingState = false,
                    productDetails = it.productDetails.copy(cartQuantity = result.second),
                    showCartDialog = true
                )
            }
        } else
            sendNewEffect(ProductScreenUiEffect.OnAddedToCart(false))
    }

    override fun onDismissCartDialog() {
        _state.update { it.copy(showCartDialog = false) }
        getCartProducts ()
    }
}