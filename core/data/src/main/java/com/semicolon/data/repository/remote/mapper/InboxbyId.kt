package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.inboxby_id.Data
import com.semicolon.data.repository.remote.inboxby_id.Message
import com.semicolon.data.repository.remote.inboxby_id.Product
import com.semicolon.domain.entity.DataMessagesItemModel
import com.semicolon.domain.entity.MessagesItemModel
import com.semicolon.domain.entity.ProductXModel

fun Message.toMessageItemModel() = MessagesItemModel(
    id = id,
    inboxId = inbox_id,
    message = message,
    createdAt = created_at,
    userType = user?.user_type,



)
fun Data.toMessageItemModel() = DataMessagesItemModel(
    messagesItemModel = messages.map { it.toMessageItemModel() } ,
    product= product.toProductInMessageItemModel()




)
fun Product.toProductInMessageItemModel() = ProductXModel(
nameSeller =seller.name,
    short_desc = short_desc?:"" ,
    photo = photo?:"",
    title = title?:"",
    unit_price = unit_price?:0, imageSeller = seller.image
    , id = id



)