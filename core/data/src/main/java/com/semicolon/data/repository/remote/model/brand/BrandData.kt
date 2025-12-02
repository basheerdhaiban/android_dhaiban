package com.semicolon.data.repository.remote.model.brand

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BrandData(
    @SerializedName("featured_brands") val featuredBrands: List<BrandDto>
)
