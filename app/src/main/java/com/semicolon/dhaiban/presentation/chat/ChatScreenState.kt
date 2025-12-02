package com.semicolon.dhaiban.presentation.chat

import com.semicolon.dhaiban.presentation.cart.CartCurrencyUiState
import com.semicolon.domain.entity.ChatModel
import com.semicolon.domain.entity.DataMessagesItemModel
import com.semicolon.domain.entity.MessagesItemModel
import com.semicolon.domain.entity.ProductXModel


data class ChatScreenState(
    val isLoading: Boolean = false,
    val isLoadingForIntial: Boolean = false,
    val productBanner: String? = "",
    var titleOfProduct: String? = "", val descriptionOfProduct: String? = "",

    var price: Double = 0.0,
    var imageSeller :String ="",
    var nameOfSeller :String="",
    val messageID: String = "",
    var message: String = "",
    val created_at: String = "",
    val id: Int = 0,
    val inbox_id: Int = 0,
    var productImg: String = "",
    var productTitle: String = "",
    var productPrice: Double = 0.0,
    val user_id: Int = 0,
    val userType: String = "",
    var chats: List<ChatsState> = listOf(),
    val product: ProductState = ProductState(),
    val currency : CartCurrencyUiState = CartCurrencyUiState(),
    )

data class ProductState(
    val productBanner: String? = "",
    val titleOfProduct: String? = "", val descriptionOfProduct: String? = "",

    val price: Double = 0.0,
    var imageSeller :String ="",
    var nameOfSeller :String=""
)

data class ChatsState(
    val message: String,
    val created_at: String,
    val userType: String? = "",
    val user_id: Int = 0,
    val inbox_id: Int = 0,

    )

fun ChatModel.toChatScreenState() =
    ChatScreenState(created_at = created_at, inbox_id = inbox_id)

fun ChatModel.toChatsScreenState() =
    ChatsState(
        created_at = created_at,
        message = message,
        userType = user.user_type,
        user_id = user_id,
        inbox_id = inbox_id,
    )

fun MessagesItemModel.toChatsScreenState() =
    ChatsState(
        created_at = createdAt ?: "",
        message = message ?: "",
        userType = userType ?: "",
        inbox_id = inboxId ?: 0,
         user_id = userId ?: 0
    )

fun ProductXModel.FromProducttoChatsScreenState() =
    ProductState(

        descriptionOfProduct = short_desc ,
        productBanner = photo, titleOfProduct = title, imageSeller = imageSeller, nameOfSeller = nameSeller
    )