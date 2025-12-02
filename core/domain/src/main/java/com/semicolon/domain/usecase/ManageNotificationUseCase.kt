package com.semicolon.domain.usecase

import com.semicolon.domain.repository.NotificationRepository

class ManageNotificationUseCase(private val repository: NotificationRepository) {
    suspend fun sendFcmToken(token: String) = repository.sendToken(token)
    fun getNotifications() = repository.getNotifications()
    fun getUnReadNotification() = repository.getUnReadNotifications()
    suspend fun markNotificationsAsRead (notificationId:String)=repository.markNotificationAsRead(notificationId)
    suspend fun getUnReadNotificationCount () = repository.getCountOfUnreadNotification() }