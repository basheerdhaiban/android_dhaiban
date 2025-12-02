package com.semicolon.dhaiban.presentation.customer_service

sealed interface CustomerServiceUiEffect {
    data object OnNavigateBack : CustomerServiceUiEffect
    data object OnNavigateToNotificationScreen : CustomerServiceUiEffect
}