package com.semicolon.data.repository.remote.model.order

data class BaseOrder(
    val `data`: Data,
    val errors: Any,
    val message: String,
    val status: Int,
    val success: Boolean
)