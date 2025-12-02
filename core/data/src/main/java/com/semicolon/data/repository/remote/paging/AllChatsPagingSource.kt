package com.semicolon.data.repository.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.semicolon.data.repository.remote.mapper.toNewMessagesDataModel
import com.semicolon.data.repository.remote.mapper.toinboxDataModel
import com.semicolon.data.repository.remote.repository.MessageRemoteSource
import com.semicolon.domain.entity.ChatModel
import com.semicolon.domain.entity.InboxsModel
import java.lang.Error

class AllChatsPagingSource(val messageRemoteSource: MessageRemoteSource,

) :
PagingSource<Int, InboxsModel>() {
    override fun getRefreshKey(state: PagingState<Int, InboxsModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, InboxsModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = messageRemoteSource.getAllInboxes(

                page = currentPage
            ).data
            val responseData = mutableListOf<InboxsModel>()
            responseData.addAll(response!!.inbox.map {
                it.toinboxDataModel()
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
            Log.d("AllChatsPagingSource", Error(e).toString())
            PagingSource.LoadResult.Error(e)
        }
    }
}