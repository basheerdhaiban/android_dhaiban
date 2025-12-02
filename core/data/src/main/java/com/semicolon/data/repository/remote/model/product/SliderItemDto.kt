package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class SliderItemDto(
    val description: String?,
    val id: Int?,
    @SerializedName("offer_group_id") val offerGroupId: Int?,
    val photo: String?,
    val title: String?,
    val type: String?,
    @SerializedName("type_id") val typeId: Int?
)