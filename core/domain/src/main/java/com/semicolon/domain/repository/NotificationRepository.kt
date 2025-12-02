package com.semicolon.domain.repository

import androidx.paging.PagingData
import com.semicolon.domain.entity.UnreadOfNotifcationModel
import com.semicolon.domain.entity.notification.NotificationModel
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {
    suspend fun sendToken(token: String): Boolean
    fun getNotifications(): Flow<PagingData<NotificationModel>>
    suspend fun markNotificationAsRead(notificationId: String): Boolean
    suspend fun getCountOfUnreadNotification(): UnreadOfNotifcationModel?
    fun getUnReadNotifications(): Flow<PagingData<NotificationModel>>
}