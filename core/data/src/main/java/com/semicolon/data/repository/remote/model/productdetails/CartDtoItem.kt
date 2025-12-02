package com.semicolon.data.repository.remote.model.productdetails

import kotlinx.serialization.Serializable

@Serializable
data class CartDtoItem(
    val identifier: Long,
    val quantity: Int,
    val variant: String?
)