package com.semicolon.dhaiban.presentation.search

sealed interface SearchScreenUiEffect {
    data object OnNavigateBack : SearchScreenUiEffect
    data class OnNavigateToProductDetailsScreen(val productId: Int) : SearchScreenUiEffect
    data object OnNavigateToNotificationScreen : SearchScreenUiEffect
}