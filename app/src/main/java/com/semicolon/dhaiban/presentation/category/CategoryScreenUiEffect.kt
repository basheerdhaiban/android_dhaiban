package com.semicolon.dhaiban.presentation.category


sealed interface CategoryScreenUiEffect {
    data object OnNavigateToHomeScreen : CategoryScreenUiEffect
    data class OnNavigateToSubCategoryScreen(val id: Int, val title: String) :
        CategoryScreenUiEffect

    data object OnNavigateToNotificationScreen : CategoryScreenUiEffect
}