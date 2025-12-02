package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.notification.NotificationDataDto
import com.semicolon.data.repository.remote.model.notification.NotificationDto
import com.semicolon.domain.entity.notification.NotificationDataModel
import com.semicolon.domain.entity.notification.NotificationModel

fun NotificationDataDto.toNotificationDataModel() = NotificationDataModel(
    deliveryStatus ?: "",
    refundStatus ?: "",
    message ?: "",
    orderId ?: 0,
    title ?: "",messageFrom?:"",productId?:0 ,refundId?:0 ,inboxId =inboxId?:0
)

fun NotificationDto.toNotificationModel() = NotificationModel(
    notificationData = this.data?.toNotificationDataModel() ?: NotificationDataModel(
        "",
        "",
        "",
        0,
        "","",0 ,0 ,0
    ),
    id ?: "",
    readAt ?: "",
    type ?: "",
    userId ?: 0
)