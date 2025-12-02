package com.semicolon.data.repository.remote.model.order

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class PreviousOrdersData(
    @SerializedName("order") val orders: OrdersResponseData,
    val pagination: Pagination? = null
)
