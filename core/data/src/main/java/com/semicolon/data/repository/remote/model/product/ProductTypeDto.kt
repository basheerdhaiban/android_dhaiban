package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProductTypeDto(
    val active: Int?,
    val id: Int?,
    @SerializedName("logo_mob") val logoImage: String?,
    val title: String?
)