package com.semicolon.data.repository.remote.model.subcategory

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SubCategoryData(@SerializedName("sub_with_sub_category") val subCategories: List<SubCategoryDto>)
