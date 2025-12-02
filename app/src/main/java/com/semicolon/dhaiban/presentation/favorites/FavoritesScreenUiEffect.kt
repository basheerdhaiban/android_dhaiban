package com.semicolon.dhaiban.presentation.favorites

sealed interface FavoritesScreenUiEffect {
    data object OnNavigateToProfileScreen: FavoritesScreenUiEffect
    data class OnNavigateToProductDetails(val productId: Int): FavoritesScreenUiEffect
    data object OnNavigateToNotificationScreen : FavoritesScreenUiEffect
}