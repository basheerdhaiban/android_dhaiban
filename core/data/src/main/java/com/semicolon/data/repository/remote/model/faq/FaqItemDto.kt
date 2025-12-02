package com.semicolon.data.repository.remote.model.faq

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class FaqItemDto(
    @SerializedName("description") val answer: String?,
    val id: Int?,
    @SerializedName("title") val question: String?
)