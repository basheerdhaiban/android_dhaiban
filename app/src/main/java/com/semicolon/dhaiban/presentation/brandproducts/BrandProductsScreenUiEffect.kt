package com.semicolon.dhaiban.presentation.brandproducts

sealed interface BrandProductsScreenUiEffect {
    data object OnNavigateToBrandScreen : BrandProductsScreenUiEffect

    data class OnNavigateToProductScreen(val productId: Int) : BrandProductsScreenUiEffect
    data object OnNavigateToNotificationScreen : BrandProductsScreenUiEffect
}