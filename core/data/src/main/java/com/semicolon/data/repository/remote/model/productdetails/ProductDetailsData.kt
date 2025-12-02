package com.semicolon.data.repository.remote.model.productdetails

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetailsData(
    @SerializedName("product")val productDetails: ProductDetailsDto
)