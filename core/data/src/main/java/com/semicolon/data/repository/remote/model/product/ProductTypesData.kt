package com.semicolon.data.repository.remote.model.product

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class ProductTypesData(@SerializedName("offers")val productTypes: List<ProductTypeDto>)