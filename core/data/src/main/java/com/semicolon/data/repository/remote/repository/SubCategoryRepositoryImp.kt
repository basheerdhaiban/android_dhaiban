package com.semicolon.data.repository.remote.repository

import android.content.res.Resources.NotFoundException
import android.util.Log
import com.semicolon.data.repository.remote.mapper.toAttributeData
import com.semicolon.data.repository.remote.mapper.toColorData
import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.mapper.toSubCategoryData
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.product.CategorySliderData
import com.semicolon.data.repository.remote.model.product.HomeSliderData
import com.semicolon.data.repository.remote.model.product.ProductData
import com.semicolon.data.repository.remote.model.subcategory.SubCategoryData
import com.semicolon.domain.entity.FilterDataModel
import com.semicolon.domain.entity.SliderItem
import com.semicolon.domain.entity.SubCategory
import com.semicolon.domain.repository.SubCategoryRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class SubCategoryRepositoryImp(client: HttpClient) : SubCategoryRepository, BaseRepository(client) {

    companion object {
        const val SUB_WITH_SUB_CATEGORY = "subWithSubCategory"
        const val LISTING_PRODUCTS = "listingProducts"
        const val CATEGORY = "category"
        const val CATEGORY_ID = "category_id"
        const val COUNTRY_ID = "country_id"
    }

    override suspend fun getSubCategories(categoryId: Int): List<SubCategory> {
        val result = tryToExecute<BaseResponse<SubCategoryData>> {
            client.get("$SUB_WITH_SUB_CATEGORY/$categoryId")
        }.data ?: throw NotFoundException()
        return result.subCategories.toSubCategoryData()
    }

    override suspend fun getFilterData(categoryId: Int, country:String): FilterDataModel {
        val result = tryToExecute<BaseResponse<ProductData>> {
            client.get {
                url(LISTING_PRODUCTS)
                parameter(COUNTRY_ID, country)
                parameter(CATEGORY_ID, categoryId)
            }
        }.data ?: throw NotFoundException()
        return FilterDataModel(
            attributes = result.attributes?.toAttributeData() ?: listOf(),
            colors = result.allColors?.toColorData() ?: listOf(),
            minPrice = result.originalMinPrice?: 0.0,
            maxPrice = result.originalMaxPrice ?: 1500.0
        )
    }


    override suspend fun getCategorySlider(categoryId: Int): List<SliderItem> {
        Log.d("categoryId",categoryId.toString())
        val result = tryToExecute<BaseResponse<CategorySliderData>> {
            client.get {
                url("slider")
                parameter(CATEGORY,categoryId)
                parameter(CATEGORY_ID,categoryId)
            }
        }
        if (result.data == null) {
            Log.d("getCategorySliderException","getCategorySliderException")
            throw Exception(result.message)
        }
        return result.data.homeSliderItems.toEntity()
    }
}