package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AllColor(
    @SerializedName("color_code")val colorCode: String?,
    val id: Int?
)