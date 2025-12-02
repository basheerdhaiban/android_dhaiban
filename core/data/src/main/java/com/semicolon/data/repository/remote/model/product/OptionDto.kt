package com.semicolon.data.repository.remote.model.product

import kotlinx.serialization.Serializable

@Serializable
data class OptionDto(
    val id : Int,
    val title: String
)