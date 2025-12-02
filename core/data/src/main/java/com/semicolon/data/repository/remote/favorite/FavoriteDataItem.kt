package com.semicolon.data.repository.remote.favorite

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDataItem(
    @SerializedName("advanced_product") val advancedProduct: String?,
    @SerializedName("avg_rating") val avgRating: Double?,
    val country: String?,
    @SerializedName("current_stock") val currentStock: Int?,
    val discount: Int?,
    @SerializedName("discount_type") val discountType: String?,
    val id: Int?,
    @SerializedName("label_color") val labelColor: String?,
    @SerializedName("label_text") val labelText: String?,
    @SerializedName("measruing_unit") val measuringUnit: String?,
    val new: Boolean?,
    val photo: String?,
    @SerializedName("product_date") val productDate: String?,
    @SerializedName("short_desc") val shortDesc: String?,
    val title: String?,
    @SerializedName("unit_price")  val unitPrice: Double?,
    @SerializedName("user_favorite") val userFavorite: Boolean?
)