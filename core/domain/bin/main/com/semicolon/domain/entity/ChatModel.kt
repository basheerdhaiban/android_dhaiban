package com.semicolon.domain.entity

data class ChatModel(    val created_at: String,
                         val id: Int,
                         val inbox_id: Int,
                         val message: String,
val user: UserOfChatModel ,
                         val user_id: Int )
data class Chats(val messages: List<ChatModel>,)
data class UserOfChatModel ( val user_type: String,)