package com.semicolon.dhaiban.presentation.favorites

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.FavoriteProduct
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageFavoritesUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class FavoritesScreenModel(
    private val manageFavoritesUseCase: ManageFavoritesUseCase,
    private val localConfigurationUseCase: LocalConfigurationUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase
) :
    BaseScreenModel<FavoritesScreenUiState, FavoritesScreenUiEffect>(FavoritesScreenUiState()),
    FavoritesScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getUserData()
        getFavoriteProducts()
        getCountOfUnreadNotification()
    }

    private fun getCountOfUnreadNotification() {
        _state.update { it.copy(isLoading = true) }
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
            onError = {  }
        )
    }

    private fun getFavoriteProducts() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageFavoritesUseCase.getFavoriteProducts() },
            onSuccess = ::onGetFavoritesSuccess,
            onError = {}
        )
    }

    private fun removeFromFavorite(productId: Int) {
        tryToExecute(
            function = { productId to manageFavoritesUseCase.removeProductFromFavorite(productId) },
            onSuccess = ::onRemoveFromFavorite,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetFavoritesSuccess(favoriteProducts: List<FavoriteProduct>) {
        _state.update { uiState ->
            uiState.copy(
                products = favoriteProducts.map { it.toFavoriteProductUiState() },
                isLoading = false
            )
        }
    }

    private fun onRemoveFromFavorite(result: Pair<Int, String>) {
        if (result.second.isNotEmpty()) {
            _state.update { uiState ->
                uiState.copy(products = uiState.products.filter { it.id != result.first })
            }
        }
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

    private fun getUserData() {
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

    fun updateCurrencySymbol(currencySymbol: String) {
        _state.update { it.copy(currencySymbol = currencySymbol) }
    }

    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toFavoriteCurrencyUiState()) }
    }

    override fun onClickUpButton() {
        sendNewEffect(FavoritesScreenUiEffect.OnNavigateToProfileScreen)
    }

    override fun onClickProduct(productId: Int) {
        sendNewEffect(FavoritesScreenUiEffect.OnNavigateToProductDetails(productId))
    }

    override fun onClickFavorite(productId: Int) {
        removeFromFavorite(productId)
    }

    override fun onClickNotification() {
        sendNewEffect(FavoritesScreenUiEffect.OnNavigateToNotificationScreen)
    }
}