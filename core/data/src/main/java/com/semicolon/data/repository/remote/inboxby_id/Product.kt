package com.semicolon.data.repository.remote.inboxby_id

data class Product(
    val advanced_product: String,
    val avg_rating: Double,
    val country: String,
    val current_stock: Int,
    val discount: Int,
    val discount_type: String,
    val id: Int,
    val label_color: String,
    val label_text: Any,
    val measruing_unit: String,
    val new: Boolean,
    val photo: String,
    val product_date: String,
    val seller: Seller,
    val short_desc: String,
    val title: String,
    val unit_price: Int,
    val user_favorite: Boolean
)