package com.semicolon.data.repository.remote.model.refund

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val name: String,
    val phone: String
)