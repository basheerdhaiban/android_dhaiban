package com.semicolon.dhaiban.presentation.brandproducts

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.usecase.ManageFavoritesUseCase
import com.semicolon.domain.usecase.ManageProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BrandProductsScreenModel(
    brandId: Int,
    private val manageProductUseCase: ManageProductUseCase,
    private val manageFavoritesUseCase: ManageFavoritesUseCase
) :
    BaseScreenModel<BrandProductsScreenUiState, BrandProductsScreenUiEffect>(
        BrandProductsScreenUiState()
    ), BrandProductsScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _brandProductsState: MutableStateFlow<PagingData<BrandProductUiState>> =
        MutableStateFlow(PagingData.empty())
    val brandProductsState =
        _brandProductsState.asStateFlow()

    init {
        getCategoryProducts(brandId)
    }

    private fun getCategoryProducts(
        brandId: Int
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, isProductsLoading = true) }
            try {
                manageProductUseCase.getProducts(brandId = brandId)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _brandProductsState.value =
                            pagingData.map { it.toBrandProductUiState() }
                        _state.update { it.copy(isLoading = false) }
                        delay(3000)
                        _state.update { it.copy(isProductsLoading = false) }
                    }
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
            }
        }
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
        _state.update { it.copy(currency = appCurrencyUiState.toBrandCurrencyUiState()) }
    }

    private fun onChangeFavoriteStateSuccess(result: Pair<Int, String>) {
        val productId = result.first
        val message = result.second
        if (message.isNotEmpty()) {
            viewModelScope.launch {
                _brandProductsState.emit(_brandProductsState.value.map {
                    it.copy(
                        isFavourite = if (it.id == productId) it.isFavourite.not()
                        else it.isFavourite
                    )
                })
            }
        }
    }

    override fun onClickBackButton() {
        sendNewEffect(BrandProductsScreenUiEffect.OnNavigateToBrandScreen)
    }

    override fun onClickProductFavorite(
        productId: Int,
        isFavorite: Boolean
    ) {
        changeFavoriteState(isFavorite, productId)
    }

    override fun onClickProduct(productId: Int) {
        sendNewEffect(BrandProductsScreenUiEffect.OnNavigateToProductScreen(productId))
    }

    override fun onClickNotification() {
        sendNewEffect(BrandProductsScreenUiEffect.OnNavigateToNotificationScreen)
    }
}