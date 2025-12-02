package com.semicolon.data.repository.remote.model.product

import kotlinx.serialization.Serializable

@Serializable
data class Attribute(
    val id: Int?,
    val options: List<OptionDto>?,
    val title: String?
)