package com.semicolon.dhaiban.presentation.brand

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.Brand
import com.semicolon.domain.usecase.ManageBrandUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class BrandScreenModel(private val manageBrandUseCase: ManageBrandUseCase) :
    BaseScreenModel<BrandScreenUiState, BrandScreenUiEffect>(
        BrandScreenUiState()
    ), BrandScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getAllBrands()
    }

    private fun getAllBrands() {
        _state.update { it.copy(isLoading = true, errorMessage = "") }
        tryToExecute(
            function = { manageBrandUseCase.getFeaturedBrands() },
            onSuccess = ::onGetBrandsSuccess,
            onError = ::onError
        )

    }

    private fun onGetBrandsSuccess(brands: List<Brand>) {
        _state.update { uiState ->
            uiState.copy(
                isLoading = false,
                brands = brands.map { it.toBrandUiState() })
        }
    }

    private fun onError(exception: Exception) {
        _state.update { it.copy(isLoading = false, errorMessage = "Network Error") }
    }

    override fun onClickBackButton() {
        sendNewEffect(BrandScreenUiEffect.OnNavigateToHomeScreen)
    }

    override fun onClickBrand(brandId: Int, brandTitle: String) {
        sendNewEffect(
            BrandScreenUiEffect.OnNavigateToBrandProducts(
                brandName = brandTitle,
                brandId = brandId
            )
        )
    }

    override fun onClickNotification() {
        sendNewEffect(BrandScreenUiEffect.OnNavigateToNotificationScreen)
    }
}