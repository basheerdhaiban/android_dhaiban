package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class TotalRate(
    @SerializedName("avg_rating") val avgRating: Double,
    @SerializedName("rating_count") val ratingCount: Double,
    @SerializedName("rating_count_1") val ratingCount1: Double,
    @SerializedName("rating_count_2") val ratingCount2: Double,
    @SerializedName("rating_count_3") val ratingCount3: Double,
    @SerializedName("rating_count_4") val ratingCount4: Double,
    @SerializedName("rating_count_5") val ratingCount5: Double
)