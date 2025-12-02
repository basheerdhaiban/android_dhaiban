package com.semicolon.data.repository.remote.model.review

import kotlinx.serialization.Serializable

@Serializable

data class Order(
    val order_id: Int,
    val reviews: List<Review>
)