package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProductData(
    @SerializedName("all_colors") val allColors: List<AllColor>? = null,
    val attributes: List<Attribute>? = null,
    @SerializedName("brand_id") val brandId: String? = null,
    @SerializedName("category_id") val categoryId: String? = null,
    @SerializedName("max_price") val maxPrice: String? = null,
    @SerializedName("min_price") val minPrice: String? = null,
    @SerializedName("original_max_price") val originalMaxPrice: Double? = null,
    @SerializedName("original_min_price") val originalMinPrice: Double? = null,
    val pagination: Pagination? = null,
    val products: List<ProductDto>,
    val query: String? = null, //not sure
    @SerializedName("selected_attributes") val selectedAttributes: List<SelectedAttribute>?  = null,
    @SerializedName("selected_color") val selectedColor: Int? = null, //not sure
    @SerializedName("seller_id") val sellerId: String? = null, //not sure
    @SerializedName("sort_by") val sortBy: String? = null
)