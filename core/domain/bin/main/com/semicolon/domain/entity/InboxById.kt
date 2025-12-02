package com.semicolon.domain.entity

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow


data class ProductXModel(

    val id: Int,

    val photo: String,

    val short_desc: String,
    val title: String,
    val unit_price: Int,

    val imageSeller: String,
    val nameSeller: String
)




data class MessagesItemModel(
    val descriptionOfProduct: String? = null,
    val titleOfProduct: String? = null,
    val inboxId: Int? = null,
    val userId: Int? = null,
    val createdAt: String? = null,
    val id: Int? = null,
    val message: String? = null,
    val user: User? = null,
    val userType: String? = null,
    val productBanner: String? = null,
    val customerId: Int? = null
)
data class DataMessagesItemModel(
val messagesItemModel: List<MessagesItemModel> ,
    val product: ProductXModel
)


