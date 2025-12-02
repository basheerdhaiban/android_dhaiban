package com.semicolon.dhaiban.presentation.orders

interface OrdersScreenInteractionListener {
    fun onClickUpButton()
    fun onClickOrderType(orderType: OrderType)
    fun onClickTrackOrder(orderUiState: OrderUiState, orderType: OrderType)
    fun onClickNotification()
}