package com.semicolon.dhaiban.presentation.home

import com.semicolon.dhaiban.presentation.paymentstatus.PaymentScreenStatusUiEffect


sealed interface HomeScreenUiEffect {
    data object OnNavigateToCategoriesScreen : HomeScreenUiEffect
    data object OnNavigateToBrandsScreen : HomeScreenUiEffect
    data object OnNavigateToFlashSaleScreen : HomeScreenUiEffect
    data class OnNavigateToProductDetailsScreen(val productId: Int) : HomeScreenUiEffect
    data class OnNavigateToSubCategoryScreen(val productId: Int, val categoryTitle: String) : HomeScreenUiEffect
    data object OnNotifyDataUpdated : HomeScreenUiEffect
    data class OnNavigateToBrandProductsScreen(val brandId: Int, val brandTitle: String): HomeScreenUiEffect
    data object OnNavigateToProfileScreen : HomeScreenUiEffect
    data object OnNavigateToSearchScreen : HomeScreenUiEffect
    data object OnNavigateToHome : HomeScreenUiEffect
    data class OnNavigateToTrackOrder(val orderId: Int) : HomeScreenUiEffect
}