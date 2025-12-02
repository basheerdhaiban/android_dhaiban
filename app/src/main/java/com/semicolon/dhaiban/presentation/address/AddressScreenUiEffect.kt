package com.semicolon.dhaiban.presentation.address

sealed interface AddressScreenUiEffect {
    data object OnNavigateBackToCart : AddressScreenUiEffect
    data object OnNavigateToMap : AddressScreenUiEffect
    data class OnNavigateToCart(val address: AddressUiState) : AddressScreenUiEffect
    data class OnNavigateToChangeAddress(val address: AddressUiState) : AddressScreenUiEffect
    data object OnNavigateToNotificationScreen : AddressScreenUiEffect
}