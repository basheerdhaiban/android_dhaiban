package com.semicolon.data.repository.remote.model.list_of_message

data class s(
    val `data`: Data,
    val errors: Any,
    val message: String,
    val status: Int,
    val success: Boolean
)