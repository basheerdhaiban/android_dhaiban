package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.faq.FaqItemDto
import com.semicolon.data.repository.remote.model.faq.FaqTypeDto
import com.semicolon.domain.entity.FaqItemModel
import com.semicolon.domain.entity.FaqModel

fun FaqTypeDto.toFaqModel() = FaqModel(id = id ?: 0, type = title ?: "")
fun FaqItemDto.toFaqItemModel() =
    FaqItemModel(id = id ?: 0, question = question ?: "", answer = answer ?: "")