package com.semicolon.data.repository.remote.model.productdetails

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailsDto(
    @SerializedName("advanced_product") val advancedProduct: String?,
    val brand: Brand?,
    @SerializedName("choice_options") val choiceOptions: List<ChoiceOption>?,
    val colors: List<Color>?,
    val country: String?,
    @SerializedName("current_stock") val currentStock: Int?,
    val description: String?,
    val discount: Double?,
    @SerializedName("discount_type") val discountType: String?,
    val id: Int?,
    @SerializedName("label_color") val labelColor: String?,
    @SerializedName("label_text") val labelText: String?,
    @SerializedName("measruing_unit") val measuringUnit: String?,
    val photo: String?,
    val photos: List<Photo>?,
    @SerializedName("price_higher") val priceHigher: Double?,
    @SerializedName("price_lower") val priceLower: Double?,
    val rating: Double?,
    @SerializedName("rating_count") val ratingCount: Int?,
    @SerializedName("serialized_specs") val serializedSpecs: Map<String?, String?>?,
    @SerializedName("short_desc") val shortDescription: String?,
    val tags: List<String>?,
    val title: String?,
    val seller :String?,
    @SerializedName("user_favorite") val userFavorite: Boolean?,
    @SerializedName("variant_product")  val variantProduct: Int?,
    val cart: List<CartDtoItem>
)