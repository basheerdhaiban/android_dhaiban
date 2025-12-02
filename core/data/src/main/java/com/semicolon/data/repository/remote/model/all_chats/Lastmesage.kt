package com.semicolon.data.repository.remote.model.all_chats

data class Lastmesage(
    val created_at: String,
    val id: Int,
    val inbox_id: Int,
    val message: String,
//    val product: Product,
    val product_banner: String,
    val user: User,
    val user_id: Int
)