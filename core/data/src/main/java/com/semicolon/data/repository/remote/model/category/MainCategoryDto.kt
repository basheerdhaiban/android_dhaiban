package com.semicolon.data.repository.remote.model.category

import kotlinx.serialization.Serializable

@Serializable
data class MainCategoryDto(
    val id: Int?,
    val title: String?,
    val image: String?,
)