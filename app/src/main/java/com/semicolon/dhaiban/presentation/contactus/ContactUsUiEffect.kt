package com.semicolon.dhaiban.presentation.contactus

sealed interface ContactUsUiEffect {
    data object OnNavigateBack : ContactUsUiEffect
    data object OnNavigateToNotificationScreen : ContactUsUiEffect
    data object OnNavigateToCustomerScreen : ContactUsUiEffect
}