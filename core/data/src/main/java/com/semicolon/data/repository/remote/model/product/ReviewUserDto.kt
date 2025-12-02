package com.semicolon.data.repository.remote.model.product

import kotlinx.serialization.Serializable

@Serializable
data class ReviewUserDto(
    val id: Int,
    val name: String
)