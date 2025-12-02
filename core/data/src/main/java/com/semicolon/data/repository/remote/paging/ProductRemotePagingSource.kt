package com.semicolon.data.repository.remote.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.repository.ProductRemoteSource
import com.semicolon.domain.entity.Product

class ProductRemotePagingSource(
    private val productRemoteSource: ProductRemoteSource,
    private val countryId: String? = null,
    private val offerGroupId: Int? = null,
    private val color: Int? = null,
    private val brandId: Int? = null,
    private val sortBy: Int? = null,
    private val searchQuery: String? = null,
    private val minPrice: Int? = null,
    private val maxPrice: Int? = null,
    private val categoryId: Int? = null,
    private val sellerId: Int? = null,
    private val attributes: Map<Int, List<String>>? = null
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        Log.d(
            "ProductRemotePagingSource",
            "$color ,$brandId ,$sortBy,$sellerId ,$searchQuery,$maxPrice, $minPrice,$categoryId,$sellerId,${attributes?.values}"
        )
        return try {
            val currentPage = params.key ?: 1
            val response = productRemoteSource.getProducts(
                countryId = countryId,
                offerGroupId = offerGroupId,
                page = currentPage,
                color = color,
                brandId = brandId,
                sortBy = sortBy,
                searchQuery = searchQuery,
                minPrice = minPrice,
                maxPrice = maxPrice,
                categoryId = categoryId,
                sellerId = sellerId,
                attributes = attributes
            ).data
            val responseData = mutableListOf<Product>()
            responseData.addAll(response!!.products.toEntity())
            LoadResult.Page(
                data = responseData.toList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.pagination?.nextPageUrl == null)
                    null
                else
                    currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}

class ProductRemotePagingSource2(
    private val productRemoteSource: ProductRemoteSource,
    private val countryId: String,
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        Log.d("ProductRemotePagingSource", "load Data")
        return try {
            val currentPage = params.key ?: 1
            val response = productRemoteSource.getRecentProducts(
                page = currentPage,
                countryId = countryId
            ).data
            val responseData = mutableListOf<Product>()
            responseData.addAll(response!!.products.toEntity())
            LoadResult.Page(
                data = responseData.toList(),
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (response.pagination?.nextPageUrl == null)
                    null
                else
                    currentPage.plus(1)
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
