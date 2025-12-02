package com.semicolon.data.repository.remote.model.order

data class DataX(
    val OrderProduct: List<OrderProduct>,
    val address: Address,
    val code: String,
    val commercial_register: Any,
    val coupon_discount: Int,
    val currency: String,
    val date: String,
    val delivery_status: String,
    val id: Int,
    val invoice_link: Any,
    val is_rated: Int,
    val order_type: String,
    val payment_status: Int,
    val payment_type: String,
    val shipping_cost: Int,
    val shipping_tax: Int,
    val tax: Int,
    val total_price: Int,
    val user: UserX,
    val viewed: Int,
    val wallet_discount: Int
)