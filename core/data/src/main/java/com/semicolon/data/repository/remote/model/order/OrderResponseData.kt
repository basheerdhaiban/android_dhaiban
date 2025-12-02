package com.semicolon.data.repository.remote.model.order

import com.google.gson.annotations.SerializedName

data class OrderResponseData(
    @SerializedName("data") val data: OrderDto )