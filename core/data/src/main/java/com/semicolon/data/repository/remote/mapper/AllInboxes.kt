package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.all_chats.Inbox
import com.semicolon.domain.entity.InboxsModel

fun Inbox.toinboxDataModel() = InboxsModel(
    created_at =lastmesage.created_at,
    id = id,
    message = lastmesage?.message,
    order_id =order_id,
    product_id =product_id,
    name = participants?.user?.name
    , photo = participants?.user?.photo,
    inbox_id = lastmesage?.inbox_id,
    user_id = participants?.user_id,
    imageOfProduct = lastmesage.product_banner,
    price = 0.0,
    nameOfProduct = ""



)
