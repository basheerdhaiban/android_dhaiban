package com.semicolon.data.repository.remote.model.user_message

data class Inbox(
    val created_at: String,
    val id: Int,
    val lastmesage: Lastmesage,
    val order_id: Int,
    val participants: Participants,
    val product_id: Int
)