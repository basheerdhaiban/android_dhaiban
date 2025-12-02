package com.semicolon.data.repository.remote.model.order

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OrdersResponseData(
    @SerializedName("data") val data: List<OrderDto>
)