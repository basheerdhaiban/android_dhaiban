package com.semicolon.dhaiban.presentation.flashsale

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.Product
import com.semicolon.domain.usecase.ManageFavoritesUseCase
import com.semicolon.domain.usecase.ManageProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class FlashSaleScreenModel(
    private val manageProductUseCase: ManageProductUseCase,
    private val manageFavoritesUseCase: ManageFavoritesUseCase
) :
    BaseScreenModel<FlashSaleScreenUiState, FlashSaleScreenUiEffect>(
        FlashSaleScreenUiState()
    ), FlashSaleScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getSaleProducts()
    }


    private fun getSaleProducts() {
        _state.update { it.copy(isLoading = true, errorMessage = "") }
        tryToExecute(
            function = { manageProductUseCase.getSaleProducts() },
            onSuccess = ::onGetSaleProductsSuccess,
            onError = ::onError
        )

    }

    private fun changeFavoriteState(productState: Boolean, productId: Int) {
        tryToExecute(
            function = {
                if (productState) {
                    productId to manageFavoritesUseCase.removeProductFromFavorite(productId)
                } else {
                    productId to manageFavoritesUseCase.addProductToFavorite(productId)
                }
            },
            onSuccess = ::onChangeFavoriteStateSuccess,
            onError = {}
        )
    }

    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toSaleCurrencyUiState()) }
    }

    private fun onGetSaleProductsSuccess(saleProducts: List<Product>) {
        _state.update { homeScreenUiState ->
            homeScreenUiState.copy(
                products = saleProducts.map { it.toSalesUiState() },
                isLoading = false
            )
        }
    }

    private fun onChangeFavoriteStateSuccess(result: Pair<Int, String>) {
        val productId = result.first
        val message = result.second
        if (message.isNotEmpty()) {
            _state.update { homeScreenUiState ->
                homeScreenUiState.copy(
                    products = homeScreenUiState.products.map {
                        if (it.id == productId)
                            it.copy(isFavourite = it.isFavourite.not())
                        else
                            it
                    }
                )
            }
        }
    }

    private fun onError(exception: Exception) {
        _state.update { it.copy(isLoading = false, errorMessage = "Network Error") }
    }

    override fun onClickBackButton() {
        sendNewEffect(FlashSaleScreenUiEffect.OnNavigateToHomeScreen)
    }

    override fun onClickProductFavorite(
        productId: Int,
        isFavorite: Boolean
    ) {
        changeFavoriteState(isFavorite, productId)
    }

    override fun onClickProduct(productId: Int) {
        sendNewEffect(FlashSaleScreenUiEffect.OnNavigateToProductScreen(productId))
    }

    override fun onClickNotification() {
        sendNewEffect(FlashSaleScreenUiEffect.OnNavigateToNotificationScreen)
    }
}