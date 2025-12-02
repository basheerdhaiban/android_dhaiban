package com.semicolon.data.repository.remote.repository

import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.notification.UserNotificationResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class NotificationsRemoteSource(private val client: HttpClient) {
    companion object {
        const val NOTIFICATIONS = "user/notification"
        const val PAGE = "page"
        const val UNREADNOTIFICATIONS = "user/unreadNotifications"

    }

    suspend fun getUserNotifications(page: Int): BaseResponse<UserNotificationResponse> {
        return client.get(NOTIFICATIONS) {
            parameter(PAGE, page)
        }.body()
    }

    suspend fun getUnReadNotifications(page: Int): BaseResponse<UserNotificationResponse> {
        return client.get(UNREADNOTIFICATIONS) {
            parameter(PAGE, page)
        }.body()
    }
}