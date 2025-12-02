package com.semicolon.data.repository.remote.model.refund

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class RefundReasonItemDto(
    @SerializedName("id") val reasonId: Int?,
    @SerializedName("resone") val reason: String?
)