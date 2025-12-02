package com.semicolon.data.repository.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Language(
    val code: String,
    @SerializedName("exchange_rate") val exchangeRate: Double,
    val id: Int,
    val name: String,
    val symbol: String
)