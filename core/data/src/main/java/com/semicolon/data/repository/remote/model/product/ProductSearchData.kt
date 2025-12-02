package com.semicolon.data.repository.remote.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ProductSearchData(
    val products: List<ProductDto>
)
