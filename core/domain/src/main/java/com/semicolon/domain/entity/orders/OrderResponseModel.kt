package com.semicolon.domain.entity.orders

data class OrderResponseModel(
    val message: String,
    val orderId: Int,
    val success: Boolean
)
