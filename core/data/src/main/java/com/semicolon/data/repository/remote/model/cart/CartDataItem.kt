package com.semicolon.data.repository.remote.model.cart

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CartDataItem(
    @SerializedName("currency_code") val currencyCode: String?,
    @SerializedName("currency_id") val currencyId: Int?,
    val id: Int?,
    val identifier: Long?,
    val isUpdated: Int?,
    val price: Double?,
    val product: CartProductDto?,
    val quantity: Int?,
    @SerializedName("serialized_options") val serializedOptions: SerializedOptions?,
    val tax: Double?,
    val variant: String?
)