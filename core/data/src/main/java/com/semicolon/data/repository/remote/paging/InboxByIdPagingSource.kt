package com.semicolon.data.repository.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.semicolon.data.repository.remote.mapper.toMessageItemModel
import com.semicolon.data.repository.remote.mapper.toinboxDataModel
import com.semicolon.data.repository.remote.repository.MessageRemoteSource
import com.semicolon.domain.entity.InboxsModel
import com.semicolon.domain.entity.MessagesItemModel
import java.lang.Error

class InboxByIdPagingSource (val messageRemoteSource: MessageRemoteSource,val inboxId:Int

                             ) :
    PagingSource<Int, MessagesItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, MessagesItemModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MessagesItemModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = messageRemoteSource.getInboxById(inboxId,

                page = currentPage
            ).data
            val responseData = mutableListOf<MessagesItemModel>()
            response!!.messages?.let {
                responseData.addAll(it.map { it.toMessageItemModel() ?: MessagesItemModel() }
                )
            }
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