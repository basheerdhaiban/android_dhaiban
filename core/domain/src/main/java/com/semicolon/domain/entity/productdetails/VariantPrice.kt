package com.semicolon.domain.entity.productdetails

data class VariantPrice(
    val stockCount: Int,
    val measuringUnit: String,
    val price: Double,
    val productId: Int,
    val variant: String,
    val weight: Int
)
