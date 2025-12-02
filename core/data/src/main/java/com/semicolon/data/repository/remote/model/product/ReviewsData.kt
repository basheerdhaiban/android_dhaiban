package com.semicolon.data.repository.remote.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ReviewsData(
    val reviews: List<ReviewDto>?,
    val totalRate: TotalRate?
)