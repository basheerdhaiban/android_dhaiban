package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val active: Int?,
    @SerializedName("created_at") val createdAt: String?,
    val id: Int?,
    @SerializedName("product_id") val productId: Int?,
    val rate: Double?,
    val review: String?,
    @SerializedName("updated_at") val updatedAt: String?,
    val user: ReviewUserDto?,
    @SerializedName("user_id") val userId: Int?
)