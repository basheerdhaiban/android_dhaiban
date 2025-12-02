package com.semicolon.data.repository.remote.model.brand

import kotlinx.serialization.Serializable

@Serializable
data class BrandDto(
    val id: Int?,
    val title: String?,
    val logo: String?
)