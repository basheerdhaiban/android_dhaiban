package com.semicolon.dhaiban.presentation.orders

sealed interface OrdersScreenUiEffect {
    data object OnNavigateBack : OrdersScreenUiEffect
    data class OnNavigateToTrackOrder(val order: OrderUiState, val orderType: OrderType) :
        OrdersScreenUiEffect
    data object OnNavigateToNotificationScreen : OrdersScreenUiEffect
}