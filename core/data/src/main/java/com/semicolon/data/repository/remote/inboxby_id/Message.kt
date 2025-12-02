package com.semicolon.data.repository.remote.inboxby_id

data class Message(
    val comapny_name: Any,
    val created_at: String,
    val id: Int,
    val inbox_id: Int,
    val message: String,
    val product: Any,
    val product_banner: Any,
    val user: User,
    val user_id: Int
)