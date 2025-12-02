package com.semicolon.domain.entity.notification

data class NotificationDataModel(
    val deliveryStatus: String,
    val refundStatus: String,
    val message: String,
    val orderId: Int,
    val title: String,
    val messageFrom: String,
    val productID: Int,
    val refundId: Int,
    val inboxId:Int


)
