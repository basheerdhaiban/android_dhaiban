package com.semicolon.domain.entity

data class InboxsModel(
    val created_at: String?,
    val id: Int?,
    val order_id: Int?,
    val product_id: Int?,
    val user_id: Int?,
    val inbox_id: Int?,
    val message: String?,
    val imageOfProduct: String?,
    val price: Double?,
    val nameOfProduct:String,
    val name: String?, val photo: Any?
)
