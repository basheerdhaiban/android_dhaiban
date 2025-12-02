package com.semicolon.data.repository.remote.model.refund.trackrefund

data class DataX(
    val Product: Product,
    val address: Address,
    val admin_resone: String,
    val code: String,
    val created_at: String,
    val customer_reson: String,
    val edit_address: Boolean,
    val id: Int,
    val order_product_id: Int,
    val payment_type: String,
    val refund_amount: Double,
    val status: String,
    val user: User,
    val viewed: Int
)