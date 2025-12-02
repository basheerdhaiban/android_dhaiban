package com.semicolon.data.repository.remote.model.order

import com.google.gson.annotations.SerializedName

data class MakeOrderResponseData(
    val message: String,
    @SerializedName("order_id") val orderId: Int,
    val success: Boolean
)