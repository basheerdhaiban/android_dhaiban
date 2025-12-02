package com.semicolon.data.repository.remote.model.category

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryData(
    @SerializedName("main_category") val mainCategories: List<MainCategoryDto>
)