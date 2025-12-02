package com.semicolon.data.repository.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.semicolon.data.repository.remote.mapper.toNotificationModel
import com.semicolon.data.repository.remote.repository.NotificationsRemoteSource
import com.semicolon.domain.entity.notification.NotificationModel


class UnreadUserNotificationPagingSource(private val notificationsRemoteSource: NotificationsRemoteSource) :
    PagingSource<Int, NotificationModel>() {
    override fun getRefreshKey(state: PagingState<Int, NotificationModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NotificationModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = notificationsRemoteSource.getUnReadNotifications(
                page = currentPage
            ).data
            val responseData = mutableListOf<NotificationModel>()
            responseData.addAll(response!!.notifications.map { it.toNotificationModel() })
            LoadResult.Page(
                data = responseData.toList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.pagination.nextPageUrl == null)
                    null
                else
                    currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}