package com.semicolon.dhaiban.presentation.contactus

import com.semicolon.data.repository.remote.model.social.Social
import com.semicolon.domain.entity.SocialModel

data class ContactUsUiState(
    val isLoading: Boolean = false,
    val countOfUnreadMessage :Int =0,
val social: SocialStat = SocialStat()
)
data class SocialStat (    val facebook: String = "",
                           val instgram: String = "",
                           val snap_chat: String = "",
                           val tik_tok: String = "",
                           val twitter: String = "",
                           val we_chat: String = "",
                           val whats_app: String = "",
                           val whats_app_chanel: String = "",
                           val you_tube: String = "")
fun SocialModel.toContactUsUiState() =
    SocialStat(
        facebook = facebook,
        instgram = instgram,
        snap_chat = snap_chat,
        tik_tok = tik_tok,
        twitter = twitter,
        we_chat = we_chat,
        whats_app = whats_app,
        whats_app_chanel = whats_app_chanel,
        you_tube = you_tube
    )