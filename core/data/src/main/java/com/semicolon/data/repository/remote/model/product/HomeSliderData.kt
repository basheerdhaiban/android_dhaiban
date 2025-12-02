package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class HomeSliderData(
    @SerializedName("homeslider") val homeSliderItems: List<SliderItemDto>
)
@Serializable
data class CategorySliderData(
    @SerializedName("slider") val homeSliderItems: List<SliderItemDto>
)

