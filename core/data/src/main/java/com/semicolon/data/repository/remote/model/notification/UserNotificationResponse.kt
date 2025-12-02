package com.semicolon.data.repository.remote.model.notification

data class UserNotificationResponse(
    val notifications: List<NotificationDto>,
    val pagination: Pagination
)