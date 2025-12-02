package com.semicolon.data.repository.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerializedName("success") val success: Boolean,
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: T? = null,
    @SerializedName("errors") val errors: Map<String, String>? = null,
)