package com.semicolon.dhaiban.presentation.notification

import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.orders.OrderUiState

interface NotificationScreenInteractionListener {
    fun onClickUpButton()
    fun onClickNotificationType(notificationType: NotificationType)
    fun updateRefundList(refundList: List<NotificationUiState>)
    fun onClickTrackOrder(orderUiState: OrderUiState, orderType: OrderType ,id :Int,notificationID: String)
    fun onClickRefund(orderUiState: OrderUiState, orderType: OrderType,notificationID: String ,refundId:Int)
    fun onClickPayment(orderUiState: OrderUiState, orderType: OrderType ,id :Int,notificationID: String)
    fun onClickMessage(orderUiState: OrderUiState, orderType: OrderType ,id :Int,notificationID: String)
}