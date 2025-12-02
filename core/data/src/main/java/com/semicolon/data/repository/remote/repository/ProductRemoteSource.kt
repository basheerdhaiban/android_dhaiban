package com.semicolon.data.repository.remote.repository

import android.util.Log
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.product.ProductData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url


class ProductRemoteSource(private val client: HttpClient) {
    companion object {
        const val LISTING_PRODUCTS = "listingProducts"
        const val RECENT_PRODUCTS_HOME = "recentProductsHome"
        const val PAGE = "page"
        const val COUNTRY_ID = "country_id"
        const val OFFER_GROUP_ID = "offer_group_id"
        const val COLOR = "color"
        const val BRAND_ID = "brand_id"
        const val SORT_BY = "sort_by"
        const val SEARCH_QUERY = "search_query"
        const val MIN_PRICE = "min_price"
        const val MAX_PRICE = "max_price"
        const val CATEGORY_ID = "category_id"
        const val BASE_ATTRIBUTE = "attribute_"
        const val SELLER_ID = "seller_id"
        const val RATE_ORDER = "rateOrder"

    }


    suspend fun getProducts(
//        countryId: String?,
        offerGroupId: Int?,
        page: Int,
        color: Int?,
        brandId: Int?,
        sortBy: Int?,
        searchQuery: String?,
        minPrice: Int?,
        maxPrice: Int?,
        categoryId: Int?,
        countryId: String?,
        sellerId: Int?,
        attributes: Map<Int, List<String>>?
    ): BaseResponse<ProductData> {
        return client.get {
            url(LISTING_PRODUCTS)
            parameter(PAGE, page)
            parameter(OFFER_GROUP_ID, offerGroupId)
            parameter(COLOR, color)
            parameter(BRAND_ID, brandId)
            parameter(SORT_BY, sortBy)
            parameter(SEARCH_QUERY, searchQuery)
            parameter(MIN_PRICE, minPrice)
            parameter(MAX_PRICE, maxPrice)
            parameter(COUNTRY_ID, countryId)
            parameter(CATEGORY_ID, categoryId)
            parameter(SELLER_ID, sellerId)
            attributes?.forEach { entry ->
                var filterIds = ""
                entry.value.forEachIndexed { index, id ->
                    filterIds +=
                        if (index != entry.value.lastIndex)
                            "$id, "
                        else
                            id
                }
                Log.e("Attribute", BASE_ATTRIBUTE + entry.key)
                Log.e("values", filterIds)
                parameter(BASE_ATTRIBUTE + entry.key, filterIds)
            }
        }.body()
    }

    suspend fun getRecentProducts(
        page: Int,
        countryId: String? = null
    ): BaseResponse<ProductData> {
        return client.get {
            url(RECENT_PRODUCTS_HOME)
            parameter(PAGE, page)
//            parameter(COUNTRY_ID, countryId)
        }.body()
    }
}