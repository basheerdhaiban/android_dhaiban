package com.semicolon.data.repository.remote.model.user_message

data class Participants(
    val created_at: String,
    val id: Int,
    val inbox_id: Int,
    val user: UserX,
    val user_id: Int
)