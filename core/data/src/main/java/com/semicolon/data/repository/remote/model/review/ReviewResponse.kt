package com.semicolon.data.repository.remote.model.review

import kotlinx.serialization.Serializable

@Serializable

data class ReviewResponse(
    val message: String,
    val success: Boolean
)