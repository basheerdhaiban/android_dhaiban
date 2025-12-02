package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class VariantPriceDto(
    @SerializedName("in_stock")val inStock: Int?,
    @SerializedName("measruing_unit")val measuringUnit: String?,
    val price: Double?,
    @SerializedName("product_id")val productId: Int?,
    val variant: String?,
    val weight: Int?
)