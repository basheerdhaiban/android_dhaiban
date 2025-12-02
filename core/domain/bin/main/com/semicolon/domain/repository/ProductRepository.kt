package com.semicolon.domain.repository

import androidx.paging.PagingData
import com.semicolon.domain.entity.Product
import com.semicolon.domain.entity.ProductType
import com.semicolon.domain.entity.SliderItem
import com.semicolon.domain.entity.productdetails.ProductDetailsModel
import com.semicolon.domain.entity.productdetails.ReviewsDataModel
import com.semicolon.domain.entity.productdetails.VariantPrice
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getSliderProducts(): List<SliderItem>
    suspend fun getSalesProducts(): List<Product>
    suspend fun getNewProducts(countryId:String): List<Product>
    suspend fun getNewProducts1(countryId:String): Flow<PagingData<Product>>
    suspend fun getProductTypes(): List<ProductType>
    fun getProducts(
        offerGroupId: Int?,
        countryId: String?,
        color: Int?,
        brandId: Int?,
        sortBy: Int?,
        searchQuery: String?,
        minPrice: Int?,
        maxPrice: Int?,
        categoryId: Int?,
        sellerId: Int?,
        attributes: Map<Int, List<String>>?
    ): Flow<PagingData<Product>>
    suspend fun getProductDetails(productId:Int): ProductDetailsModel

    suspend fun getRelatedProducts(productId: Int): List<Product>

    suspend fun getProductReviews(productId: Int): ReviewsDataModel

    suspend fun getVariantPrice(productId: Int, colorId: Int?, variantList: List<Int>): VariantPrice

    suspend fun searchForProducts(query: String?, imageByteArray: ByteArray?) : List<Product>

    suspend fun searchForProductsByImage(byteArray: ByteArray?) : List<Product>
}