package com.semicolon.domain.entity

data class CartProduct(
    val advancedProduct: String,
    val currentStock: Int,
    val discount: Double,
    val discountType: String,
    val id: Int,
    val owner: String,
    val photo: String,
    val sellerId: Int,
    val shortDescription: String,
    val title: String
)