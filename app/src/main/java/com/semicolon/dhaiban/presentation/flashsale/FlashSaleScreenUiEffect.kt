package com.semicolon.dhaiban.presentation.flashsale

sealed interface FlashSaleScreenUiEffect {
    data object OnNavigateToHomeScreen: FlashSaleScreenUiEffect
    data class OnNavigateToProductScreen(val productId: Int) : FlashSaleScreenUiEffect
    data object OnNavigateToNotificationScreen : FlashSaleScreenUiEffect
}