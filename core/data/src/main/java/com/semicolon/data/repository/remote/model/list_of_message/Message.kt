package com.semicolon.data.repository.remote.model.list_of_message

data class Message(
    val created_at: String,
    val id: Int,
    val inbox_id: Int,
    val message: String,
    val user: User,
    val user_id: Int
)