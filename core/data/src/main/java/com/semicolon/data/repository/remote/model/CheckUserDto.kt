package com.semicolon.data.repository.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class CheckUserDto(
    @SerializedName("code") val code: Int? = null,
    @SerializedName("error") val error: String? = null,
)
