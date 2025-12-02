package com.semicolon.data.repository.remote.model.all_chats

data class Participants(
    val created_at: String,
    val id: Int,
    val inbox_id: Int,
    val user: UserX,
    val user_id: Int
)