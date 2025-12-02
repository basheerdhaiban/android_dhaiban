package com.semicolon.data.repository.remote.model.cart

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CartProductDto(
    @SerializedName("advanced_product") val advancedProduct: String?,
    @SerializedName("current_stock") val currentStock: Int?,
    val discount: Double?,
    @SerializedName("discount_type") val discountType: String?,
    val id: Int?,
    val owner: String?,
    val photo: String?,
    @SerializedName("seller_id") val sellerId: Int?,
    @SerializedName("short_desc") val shortDesc: String?,
    val title: String?
)