package com.semicolon.data.repository.remote.model.order

data class OrderProduct(
    val advanced_product: String,
    val coupon_discount: Int,
    val id: Int,
    val photo: String,
    val price: Int,
    val product_id: Int,
    val product_owner: String,
    val product_refundable: Boolean,
    val product_short_desc: String,
    val product_title: String,
    val quantity: Int,
    val serialized_options: SerializedOptionsX,
    val tax: Int
)