package com.semicolon.data.repository.remote.model.user_message

data class Lastmesage(
    val created_at: String,
    val id: Int,
    val inbox_id: Int,
    val message: String,
    val user: User,
    val user_id: Int
)