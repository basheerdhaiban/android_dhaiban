package com.semicolon.dhaiban.presentation.faq

sealed interface FaqScreenUiEffect {
    data object OnNavigateBack : FaqScreenUiEffect
    data class OnNavigateToFaqDetails(val faqId: Int) : FaqScreenUiEffect
    data object OnNavigateToNotificationScreen : FaqScreenUiEffect
}