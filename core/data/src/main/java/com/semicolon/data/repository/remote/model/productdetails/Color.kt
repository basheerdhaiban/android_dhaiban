package com.semicolon.data.repository.remote.model.productdetails

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class Color(
    @SerializedName("color_code") val colorCode: String?,
val id: Int?
)