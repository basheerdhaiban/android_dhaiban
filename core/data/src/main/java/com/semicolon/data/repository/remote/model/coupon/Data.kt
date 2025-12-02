package com.semicolon.data.repository.remote.model.coupon

data class ResponseOfCoupon(
    val discount: Double,
    val message: String,
    val products: List<Product>,
    val success: Boolean
)