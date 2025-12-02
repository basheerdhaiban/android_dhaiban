package com.semicolon.data.repository.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TokenDto(
    @SerializedName("token") val token: String
)
