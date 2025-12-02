package com.semicolon.data.repository.remote.model.refund

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentRefundRequestsResponse(
    @SerializedName("data") val data: List<RefundDto>
)