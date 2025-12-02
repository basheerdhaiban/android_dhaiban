package com.semicolon.data.repository.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.semicolon.data.repository.remote.mapper.toNewMessagesDataModel
import com.semicolon.data.repository.remote.repository.MessageRemoteSource
import com.semicolon.domain.entity.ChatModel


class SendMessagePagingSource(
    private val messageRemoteSource: MessageRemoteSource,
    private val inboxId: Int,
    private val message: String
) :
    PagingSource<Int, ChatModel>() {
    override fun getRefreshKey(state: PagingState<Int, ChatModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ChatModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = messageRemoteSource.sendMessage(
                inboxId = inboxId,
                message = message,
                page = currentPage
            ).data
            val responseData = mutableListOf<ChatModel>()
            responseData.addAll(response!!.messages.map {
                it.toNewMessagesDataModel()
            })
            LoadResult.Page(
                data = responseData.toList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response?.pagination?.next_page_url == null)
                    null
                else
                    currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}