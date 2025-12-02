package com.semicolon.data.repository.remote.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.semicolon.data.repository.remote.mapper.toUnreadOfNotifcationModel
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.make_as_read.Data
import com.semicolon.data.repository.remote.model.notification.FcmTokenResponse
import com.semicolon.data.repository.remote.paging.UnreadUserNotificationPagingSource
import com.semicolon.data.repository.remote.paging.UserNotificationsPagingSource
import com.semicolon.domain.entity.UnreadOfNotifcationModel
import com.semicolon.domain.entity.notification.NotificationModel
import com.semicolon.domain.repository.NotificationRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.patch
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.formUrlEncode
import io.ktor.utils.io.InternalAPI
import kotlinx.coroutines.flow.Flow

class NotificationRepositoryImp(
    client: HttpClient,
    private val notificationsRemoteSource: NotificationsRemoteSource
) : BaseRepository(client),
    NotificationRepository {
    companion object {
        const val FCM_TOKEN = "fcm-token"
        const val TOKEN = "token"
        const val UPDATED = "updated"
        private const val PAGE_SIZE = 10
    }

//    @OptIn(InternalAPI::class)
    @OptIn(InternalAPI::class)
    override suspend fun sendToken(token: String): Boolean {
        val formData = Parameters.build {
            append(TOKEN, token)
        }
        val result = tryToExecute<BaseResponse<FcmTokenResponse>> {
            client.patch(FCM_TOKEN) {
                contentType(ContentType.Application.FormUrlEncoded)
                body = formData.formUrlEncode()
            }
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.message == UPDATED
    }

    override fun getNotifications(): Flow<PagingData<NotificationModel>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = 2, initialLoadSize = 10),
            pagingSourceFactory = {
                UserNotificationsPagingSource(notificationsRemoteSource)
            }
        ).flow
    }

    override fun getUnReadNotifications(): Flow<PagingData<NotificationModel>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = 2, initialLoadSize = 10),
            pagingSourceFactory = {
                UnreadUserNotificationPagingSource(notificationsRemoteSource)
            }
        ).flow
    }
    override suspend fun markNotificationAsRead(notificationId: String): Boolean {
        val result = tryToExecute<BaseResponse<Data>> {
            client.get("user/mark_notification_as_read/$notificationId")
        }
        return result.data?.message==UPDATED

    }
    override suspend fun getCountOfUnreadNotification(): UnreadOfNotifcationModel? {
        val result = tryToExecute<BaseResponse<com.semicolon.data.repository.remote.count_of_unreadnotification.Data>> {
            client.get("user/unread_notification_count")
        }
        return result.data?.toUnreadOfNotifcationModel()

    }

}