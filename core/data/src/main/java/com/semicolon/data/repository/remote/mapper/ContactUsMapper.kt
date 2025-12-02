package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.inboxby_id.Message
import com.semicolon.data.repository.remote.model.contact_as.ContactUs
import com.semicolon.domain.entity.ContactUsModel
import com.semicolon.domain.entity.MessagesItemModel

fun ContactUs.toContactUsModel() = ContactUsModel(
    content =content,
    email =email,
    name = name,
    subject = subject,




    )