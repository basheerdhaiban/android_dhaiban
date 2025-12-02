package com.semicolon.dhaiban.presentation.subCategory

sealed interface SubCategoryScreenUiEffect {
    data object OnNavigateToCategories : SubCategoryScreenUiEffect
    data class OnNavigateToProductDetailsScreen(val productId: Int) : SubCategoryScreenUiEffect
    data object OnNavigateToSearchScreen : SubCategoryScreenUiEffect
    data object OnNavigateToNotificationScreen : SubCategoryScreenUiEffect
}