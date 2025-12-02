package com.semicolon.data.repository.remote.model.product

import kotlinx.serialization.Serializable

@Serializable
data class SelectedAttribute(
    val id: Int,
    val options: List<String>,
    val title: String
)