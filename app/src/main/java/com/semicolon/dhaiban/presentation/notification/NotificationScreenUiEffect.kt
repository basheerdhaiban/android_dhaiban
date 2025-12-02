package com.semicolon.dhaiban.presentation.notification

import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.orders.OrderUiState
import com.semicolon.dhaiban.presentation.orders.OrdersScreenUiEffect

sealed interface NotificationScreenUiEffect {
    data class OnNavigateToTrackOrder(val order: OrderUiState, val orderType: OrderType ,val id :Int) :
        NotificationScreenUiEffect
    data class OnNavigateToMessage(val id :Int) :
        NotificationScreenUiEffect
    data class OnNavigateTorPayment(val order: OrderUiState, val orderType: OrderType) :
        NotificationScreenUiEffect

    data class OnNavigateTorRefund(val order: OrderUiState, val orderType: OrderType ,val refundID:Int) :
        NotificationScreenUiEffect
    data object OnNavigateBack : NotificationScreenUiEffect
}