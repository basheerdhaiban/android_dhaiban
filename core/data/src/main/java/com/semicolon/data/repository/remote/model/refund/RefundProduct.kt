package com.semicolon.data.repository.remote.model.refund

import kotlinx.serialization.Serializable

@Serializable
data class RefundProduct(
    val id: Int,
    val photo: String,
    val title: String
)