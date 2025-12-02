package com.semicolon.dhaiban.presentation.paymentstatus

sealed interface PaymentScreenStatusUiEffect {
    data object OnNavigateBack : PaymentScreenStatusUiEffect
    data object OnNavigateToHome : PaymentScreenStatusUiEffect
    data class OnNavigateToTrackOrder(val orderId: Int) : PaymentScreenStatusUiEffect
    data object OnNavigateToNotificationScreen : PaymentScreenStatusUiEffect
    data class OnNavigateToStatusPaymentScreen(val amount: String ,val status:StatusPaymentType) : PaymentScreenStatusUiEffect


}