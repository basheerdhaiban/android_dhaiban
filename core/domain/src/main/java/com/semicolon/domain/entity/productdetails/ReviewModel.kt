package com.semicolon.domain.entity.productdetails

data class ReviewModel(
    val active: Int,
    val createdAt: String,
    val reviewId: Int,
    val productId: Int,
    val rate: Double,
    val review: String,
    val updatedAt: String,
    val reviewUser: ReviewUser,
    val userId: Int
)
