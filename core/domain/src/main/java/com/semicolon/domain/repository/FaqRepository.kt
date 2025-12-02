package com.semicolon.domain.repository

import com.semicolon.domain.entity.FaqItemModel
import com.semicolon.domain.entity.FaqModel

interface FaqRepository {
    suspend fun getFaqTypes(): List<FaqModel>
    suspend fun getFaqItems(id: Int): List<FaqItemModel>
}