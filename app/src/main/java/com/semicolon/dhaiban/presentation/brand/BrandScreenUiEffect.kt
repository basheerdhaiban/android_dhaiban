package com.semicolon.dhaiban.presentation.brand

sealed interface BrandScreenUiEffect {
    data object OnNavigateToHomeScreen: BrandScreenUiEffect

    data class OnNavigateToBrandProducts(val brandName: String, val brandId: Int): BrandScreenUiEffect
    data object OnNavigateToNotificationScreen :BrandScreenUiEffect
}