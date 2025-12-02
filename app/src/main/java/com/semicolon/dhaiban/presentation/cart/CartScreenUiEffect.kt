package com.semicolon.dhaiban.presentation.cart

sealed interface CartScreenUiEffect {
    data object OnFailedDelete : CartScreenUiEffect

    data object OnNavigateToAddress : CartScreenUiEffect

    data object OnNavigateToHome : CartScreenUiEffect

    data class OnNavigateToProductScreen(val productId: Int, val variant: String) :
        CartScreenUiEffect

    data object OnShowMessage : CartScreenUiEffect

    data object OnNavigateToPaymentScreen : CartScreenUiEffect

    data class OnUpdateCartCount(val cartCount: Int) : CartScreenUiEffect

    data object OnNavigateToNotificationScreen : CartScreenUiEffect
}