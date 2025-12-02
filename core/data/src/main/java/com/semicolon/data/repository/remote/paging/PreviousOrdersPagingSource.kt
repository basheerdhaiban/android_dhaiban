package com.semicolon.data.repository.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.semicolon.data.repository.remote.mapper.toOrderModel
import com.semicolon.data.repository.remote.repository.PreviousOrdersRemoteSource
import com.semicolon.domain.entity.orders.OrderModel

class PreviousOrdersPagingSource(private val previousOrdersRemoteSource: PreviousOrdersRemoteSource) :
    PagingSource<Int, OrderModel>() {

    override fun getRefreshKey(state: PagingState<Int, OrderModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, OrderModel> {
        return try {
            val currentPage = params.key ?: 1
            val response = previousOrdersRemoteSource.getPreviousOrders(currentPage).data
            val responseData = mutableListOf<OrderModel>()
            responseData.addAll(response!!.orders.data.map { it.toOrderModel() })
            LoadResult.Page(
                data = responseData.toList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.pagination!!.nextPageUrl == null) null else currentPage.plus(
                    1
                )
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}