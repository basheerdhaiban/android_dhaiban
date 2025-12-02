package com.semicolon.domain.entity

import com.semicolon.domain.entity.orders.OrderAddressModel

data class RefundModel(
    val product: RefundProductModel,
    val address: OrderAddressModel,
    val adminReason: String,
    val code: String,
    val createdAt: String,
    val customerReason: String,
    val editAddress: Boolean?,
    val id: Int,
    val orderProductId: Int,
    val paymentType: String,
    val refundAmount: Double,
    val status: String,
    val user: RefundUser,
    val viewed: Int
)

data class RefundUser(
    val id: Int,
    val name: String,
    val phone: String
)
