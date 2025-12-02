package com.semicolon.domain.repository

import com.semicolon.domain.entity.FilterDataModel
import com.semicolon.domain.entity.SliderItem
import com.semicolon.domain.entity.SubCategory

interface SubCategoryRepository {
    suspend fun getSubCategories(categoryId: Int): List<SubCategory>

    suspend fun getFilterData(categoryId: Int, country:String): FilterDataModel

    suspend fun getCategorySlider(categoryId: Int) : List<SliderItem>

}