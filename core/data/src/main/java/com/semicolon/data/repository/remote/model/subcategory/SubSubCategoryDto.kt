package com.semicolon.data.repository.remote.model.subcategory

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SubSubCategoryDto(
    @SerializedName("category_id") val categoryId: Int?,
    val id: Int?,
    @SerializedName("logo_mob") val imageUrl: String?,
    val title: String?
)