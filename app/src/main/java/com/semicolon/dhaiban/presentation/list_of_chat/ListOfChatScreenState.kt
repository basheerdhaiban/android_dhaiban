package com.semicolon.dhaiban.presentation.list_of_chat

import com.semicolon.domain.entity.InboxsModel


data class ListOfChatScreenState(
    val isLoading: Boolean = true,
    val messageID: String = "",
    var message: String = "",
    val created_at: String = "",
    val id: Int = 0,
    val inbox_id: Int = 0,
    val user_id: Int = 0,
    val userType: String = "",
    var chats: List<InboxState> = listOf(),
    val countOfUnreadMessage:Int =0
)

data class InboxState(
    val created_at: String = "",
    val id: Int = 0,
    val order_id: Int = 0,
    val product_id: Int = 0,
    val user_id: Int = 0,
    val inbox_id: Int = 0,
    val message: String = "",

    val name: String = "",
    val photo: String = "",
    val imageOfProduct: String,
    val price: Int,
    val nameOfProuduct: String
)

fun InboxsModel.toInboxState() =
    InboxState(
        created_at = created_at ?: "",
        message = message ?: "",
        photo = photo.toString(),
        name = name ?: "",
        user_id = user_id ?: 0,
        inbox_id = inbox_id ?: 0,
        nameOfProuduct = nameOfProduct ?: "",
        imageOfProduct = imageOfProduct ?: "", price = price?.toInt()?:0
    )