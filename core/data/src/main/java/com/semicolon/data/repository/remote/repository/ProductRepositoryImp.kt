package com.semicolon.data.repository.remote.repository

import android.content.res.Resources.NotFoundException
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.mapper.toProductDetailsModel
import com.semicolon.data.repository.remote.mapper.toProductTypesEntity
import com.semicolon.data.repository.remote.mapper.toReviewsDataModel
import com.semicolon.data.repository.remote.mapper.toVariantPrice
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.product.HomeSliderData
import com.semicolon.data.repository.remote.model.product.ProductData
import com.semicolon.data.repository.remote.model.product.ProductSearchData
import com.semicolon.data.repository.remote.model.product.ProductTypesData
import com.semicolon.data.repository.remote.model.product.ReviewsData
import com.semicolon.data.repository.remote.model.product.VariantPriceDto
import com.semicolon.data.repository.remote.model.productdetails.ProductDetailsData
import com.semicolon.data.repository.remote.paging.ProductRemotePagingSource
import com.semicolon.data.repository.remote.paging.ProductRemotePagingSource2
import com.semicolon.domain.entity.Product
import com.semicolon.domain.entity.ProductType
import com.semicolon.domain.entity.SliderItem
import com.semicolon.domain.entity.productdetails.ProductDetailsModel
import com.semicolon.domain.entity.productdetails.ReviewsDataModel
import com.semicolon.domain.entity.productdetails.VariantPrice
import com.semicolon.domain.repository.ProductRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.content.TextContent
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import kotlinx.coroutines.flow.Flow

class ProductRepositoryImp(
    client: HttpClient,
    private val productRemoteSource: ProductRemoteSource
) : BaseRepository(client), ProductRepository {

    companion object {
        const val HOME_SLIDER = "homeSlider"
        const val BEST_SALE_PRODUCTS_HOME = "bestSaleProductsHome"
        const val RECENT_PRODUCTS_HOME = "recentProductsHome"
        const val HOME_OFFERS_SLIDER = "homeOffersSlider"
        const val VIEW_PRODUCT = "viewProduct"
        const val RELATED_PRODUCTS = "relatedProducts"
        const val GET_PRODUCT_REVIEWS = "getProductReviews"
        const val VARIANT_PRICE = "variantPrice"
        const val SEARCH = "searchForProducts"
        const val SEARCH_BY_IMAGE = "getProductByImage"
        const val QUERY = "search_query"
        const val ID = "id"
        const val COLOR = "color"
        const val CHOICE = "choice"

        const val COUNTRY_ID = "country_id"
        private const val PAGE_SIZE = 4

    }

    override suspend fun getSliderProducts(): List<SliderItem> {
        val result = tryToExecute<BaseResponse<HomeSliderData>> {
            client.get(HOME_SLIDER)
        }.data ?: throw NotFoundException()

        return result.homeSliderItems.toEntity()
    }

    override suspend fun getSalesProducts(): List<Product> {
        val result = tryToExecute<BaseResponse<ProductData>> {
            client.get(BEST_SALE_PRODUCTS_HOME)
        }.data ?: throw NotFoundException()
        return result.products.toEntity()
    }

    override suspend fun getProductTypes(): List<ProductType> {
        val result = tryToExecute<BaseResponse<ProductTypesData>> {
            client.get(HOME_OFFERS_SLIDER)
        }.data ?: throw NotFoundException()
        return result.productTypes.toProductTypesEntity()
    }

    override suspend fun getNewProducts(countryId:String): List<Product> {
        val result = tryToExecute<BaseResponse<ProductData>> {
            client.get(RECENT_PRODUCTS_HOME){
                header("Accept", "application/json")
                parameter(COUNTRY_ID, countryId)
            }
        }.data ?: throw NotFoundException()
        return result.products.toEntity()
    }


    override suspend fun getNewProducts1(countryId: String): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 10, prefetchDistance = 10, initialLoadSize = 4),
            pagingSourceFactory = {
//                ProductRemotePagingSource2(
                ProductRemotePagingSource(
                    productRemoteSource,
                    countryId = countryId
                )
            }
        ).flow
    }


    override fun getProducts(
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
    ): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE, prefetchDistance = 2, initialLoadSize = 4),
            pagingSourceFactory = {
                ProductRemotePagingSource(
                    productRemoteSource,
                    countryId,
                    offerGroupId,
                    color,
                    brandId,
                    sortBy,
                    searchQuery,
                    minPrice,
                    maxPrice,
                    categoryId,
                    sellerId,
                    attributes
                )
            }
        ).flow
    }

    override suspend fun getProductDetails(productId: Int): ProductDetailsModel {
        val result = tryToExecute<BaseResponse<ProductDetailsData>> {
            client.get("${VIEW_PRODUCT}/$productId")
        }.data ?: throw NotFoundException()
        return result.productDetails.toProductDetailsModel()
    }

    override suspend fun getRelatedProducts(productId: Int): List<Product> {
        val result = tryToExecute<BaseResponse<ProductData>> {
            client.get("${RELATED_PRODUCTS}/$productId")
        }.data ?: throw NotFoundException()
        return result.products.toEntity()
    }

    override suspend fun getProductReviews(productId: Int): ReviewsDataModel {
        val result = tryToExecute<BaseResponse<ReviewsData>> {
            client.get("${GET_PRODUCT_REVIEWS}/$productId")
        }.data ?: throw NotFoundException()
        return result.toReviewsDataModel()
    }

    override suspend fun getVariantPrice(
        productId: Int,
        colorId: Int?,
        variantList: List<Int>
    ): VariantPrice {
        val jsonObject = JsonObject().apply {
            addProperty(ID, productId)
            add(COLOR, colorId?.let { Gson().toJsonTree(it) } ?: Gson().toJsonTree(null))
            add(CHOICE, Gson().toJsonTree(variantList.map { variantId ->
                mapOf(ID to variantId)
            }))
        }
        val dataString = jsonObject.toString()

//        val dataString = Gson().toJson(
//            mapOf(
//                ID to productId,
//                COLOR to colorId,
//                CHOICE to variantList.map { variantId ->
//                    mapOf(ID to variantId)
//                }
//            )
//        )
        val result = tryToExecute<BaseResponse<VariantPriceDto>> {
            client.post(VARIANT_PRICE) {
                setBody(TextContent(dataString, ContentType.Application.Json))
            }
        }.data ?: throw NotFoundException()
        return result.toVariantPrice()
    }

    override suspend fun searchForProducts(query: String?, imageByteArray: ByteArray?): List<Product> {
        val result = tryToExecute<BaseResponse<ProductSearchData>> {
            client.get {
                url(SEARCH)
                parameter(QUERY, query)
            }
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.products.map { it.toEntity() }
    }

    override suspend fun searchForProductsByImage(byteArray: ByteArray?): List<Product> {
        val result = tryToExecute<BaseResponse<ProductSearchData>> {
            client.submitFormWithBinaryData(
                url = SEARCH_BY_IMAGE,
                formData = formData {
                    if (byteArray != null) {
                        append("image", byteArray, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=image.png")
                        }
                        )
                    }
                })
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.products.map { it.toEntity() }

    }
}