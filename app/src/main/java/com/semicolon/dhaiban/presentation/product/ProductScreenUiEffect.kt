package com.semicolon.dhaiban.presentation.product

sealed interface ProductScreenUiEffect {
    data object OnNavigateBack : ProductScreenUiEffect
    data class OnNavigateToProductDetails(val productId: Int): ProductScreenUiEffect
    data object OnVariantNotAvailable: ProductScreenUiEffect
    data class OnAddedToCart(val state: Boolean): ProductScreenUiEffect
    data object OnNeedVariant: ProductScreenUiEffect
    data class OnShowMessage(val message: String): ProductScreenUiEffect
    data object OnNavigateToWelcomeScreen : ProductScreenUiEffect
    data object OnNavigateToCartScreen : ProductScreenUiEffect
    data class OnUpdateCartCount(val cartCount: Int) : ProductScreenUiEffect
}