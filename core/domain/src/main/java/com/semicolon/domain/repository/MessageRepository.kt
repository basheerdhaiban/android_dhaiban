package com.semicolon.domain.repository

import androidx.paging.PagingData
import com.semicolon.domain.entity.ChatModel
import com.semicolon.domain.entity.DataMessagesItemModel
import com.semicolon.domain.entity.InboxsModel
import com.semicolon.domain.entity.MessagesItemModel
import com.semicolon.domain.entity.ProductXModel
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun sendNewMessage(productId: Int ,orderId:Int,user:Int,message:String): ChatModel
    fun getFromsendNewMessage(    productId: Int,
                                  orderId: Int,
                                  user: Int,
                                  message: String): Flow<PagingData<ChatModel>>
    suspend fun sendMessage(inboxId:Int,message:String): Flow<PagingData<ChatModel>>
    suspend fun getMessage(inboxId:Int): List<ChatModel>
    suspend fun getAllMessage(): Flow<PagingData<InboxsModel>>
    suspend fun getInboxById(inboxId: Int): Flow<PagingData<MessagesItemModel>>
    suspend fun getMetaDataForMessageByID(inboxId: Int): ProductXModel
}