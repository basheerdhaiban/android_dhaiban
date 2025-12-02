package com.semicolon.dhaiban.presentation.faqdetails

sealed interface FaqDetailsUiEffect {
    data object OnNavigateBack : FaqDetailsUiEffect
    data object OnNavigateToNotificationScreen : FaqDetailsUiEffect
}