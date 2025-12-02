package com.semicolon.data.repository.remote.model.review

import kotlinx.serialization.Serializable

@Serializable
data class Review(
    val product_id: Int,
    val rate: Int,
    val review: String
)