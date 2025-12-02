package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.order.MakeOrderResponseData
import com.semicolon.data.repository.remote.model.social.Social
import com.semicolon.domain.entity.SocialModel
import com.semicolon.domain.entity.orders.OrderResponseModel

fun Social.toSocialModel() =
    SocialModel(facebook, instgram, snap_chat, tik_tok, twitter, we_chat, whats_app, whats_app_chanel, you_tube)