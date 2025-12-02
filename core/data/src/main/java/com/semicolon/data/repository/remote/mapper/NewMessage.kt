package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.notification.NotificationDataDto
import com.semicolon.data.repository.remote.model.product.ProductTypeDto
import com.semicolon.data.repository.remote.model.send_new_message.Data
import com.semicolon.data.repository.remote.model.send_new_message.Message
import com.semicolon.domain.entity.ChatModel
import com.semicolon.domain.entity.Chats
import com.semicolon.domain.entity.UserOfChatModel
import com.semicolon.domain.entity.notification.NotificationDataModel

fun Data.toNewMessageDataModel() = ChatModel(
    created_at = messages[0].created_at,
    id = messages[0].id,
    inbox_id = messages[0].inbox_id,
    message = messages[0].message,
    user_id = messages[0].user_id
    , user = UserOfChatModel(messages[0].user.user_type)


)
fun Message.toNewMessagesDataModel() = ChatModel(
    created_at = created_at,
    id = id,
    inbox_id = inbox_id,
    message = message,
    user_id = user_id
    , user =UserOfChatModel(user.user_type)



)
fun List<Message>.toNewMessagesDataEntity() = this.map { it.toNewMessagesDataModel() }
