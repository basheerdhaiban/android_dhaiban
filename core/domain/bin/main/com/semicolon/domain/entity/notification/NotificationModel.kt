package com.semicolon.domain.entity.notification

data class NotificationModel(
    val notificationData: NotificationDataModel,
    val id: String,
    val readAt: String,
    val type: String,
    val userId: Int
)
