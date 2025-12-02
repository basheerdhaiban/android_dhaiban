package com.semicolon.domain.entity.productdetails

data class DefaultAddress(
    val id: Int,
    val address: String, val shippingCost: Double,
    val shippingTax: Double
)
