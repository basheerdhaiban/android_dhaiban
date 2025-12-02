package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerializedName("advanced_product") val advancedProduct: String?,
    @SerializedName("avg_rating") val averageRating: Double?,
    val country: String?,
    @SerializedName("current_stock") val currentStock: Int?,
    val discount: Int?,
    @SerializedName("discount_type") val discountType: String?,
    val id: Int?,
    @SerializedName("label_color") val labelColor: String?,
    @SerializedName("label_text") val labelText: String?,
    val new: Boolean?,
    val photo: String?,
    @SerializedName("product_date") val productDate: String?,
    @SerializedName("short_desc") val shortDescription: String?,
    val title: String?,
    @SerializedName("unit_price") val unitPrice: Double?,
    @SerializedName("user_favorite") val userFavorite: Boolean?,
    @SerializedName("price_lower") val priceLower: String? =null,
    @SerializedName("price_higher") val priceHigher: String?=null,
)