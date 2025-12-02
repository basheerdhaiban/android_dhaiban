package com.semicolon.data.repository.remote.model.order

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OrderProductDto(
    @SerializedName("advanced_product")  val advancedProduct: String?,
    @SerializedName("coupon_discount") val couponDiscount: Int?,
    val id: Int?,
    val photo: String?,
    val price: Double?,
    @SerializedName("product_id") val productId: Int?,
    @SerializedName("product_owner") val productOwner: ProductOwner?,
    @SerializedName("product_refundable") val productRefundable: Boolean?,
    @SerializedName("product_short_desc") val productShortDesc: String?,
    @SerializedName("product_title") val productTitle: String?,
    val quantity: Int?,
    @SerializedName("serialized_options") val serializedOptions: SerializedOptions?,
    val tax: Double?,
    val rated: Int?
)
@Serializable
data class ProductOwner(
    @SerializedName("company_name")
    val companyName: String,
    val id: Long,
    @SerializedName("company_image")
    val image:String
)