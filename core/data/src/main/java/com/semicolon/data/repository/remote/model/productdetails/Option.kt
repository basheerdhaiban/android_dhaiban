package com.semicolon.data.repository.remote.model.productdetails

import kotlinx.serialization.Serializable
@Serializable
data class Option(
    val id: Int?,
    val title: String?
)