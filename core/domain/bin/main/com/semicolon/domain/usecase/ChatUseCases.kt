package com.semicolon.domain.usecase

import com.semicolon.domain.repository.MessageRepository
import com.semicolon.domain.repository.NotificationRepository

class ChatUseCases(private val repository: MessageRepository) {
     fun sendNewMessage(productId: Int, orderId: Int, user: Int, message: String) =
        repository.getFromsendNewMessage(productId=productId, orderId = orderId, user=user,message= message)

    suspend fun sendMessage(inboxID: Int, message: String) =repository.sendMessage(inboxID, message)

    suspend fun getMessage(inboxID: Int)=repository.getMessage(inboxID)
    suspend fun getAllInbox()=repository.getAllMessage()
    suspend fun getInboxById(inboxID: Int)=repository.getInboxById(inboxID)
    suspend fun getMetaDataInboxById(inboxID: Int)=repository.getMetaDataForMessageByID(inboxID)

}