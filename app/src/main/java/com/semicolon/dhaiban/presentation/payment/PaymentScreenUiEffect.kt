package com.semicolon.dhaiban.presentation.payment

import com.semicolon.dhaiban.presentation.orders.OrdersScreenUiEffect

sealed interface PaymentScreenUiEffect {
    data object OnNavigateBack : PaymentScreenUiEffect
    data object OnNavigateToHome : PaymentScreenUiEffect
    data class OnNavigateToTrackOrder(val orderId: Int) : PaymentScreenUiEffect
    data object OnNavigateToNotificationScreen : PaymentScreenUiEffect
    data class OnNavigateToStatusPaymentScreen(val amount: Double ,val status:StatusPaymentType,val orderID:Int ) : PaymentScreenUiEffect
}