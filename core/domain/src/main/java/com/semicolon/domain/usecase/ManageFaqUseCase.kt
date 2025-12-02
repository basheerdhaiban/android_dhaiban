package com.semicolon.domain.usecase

import com.semicolon.domain.repository.FaqRepository

class ManageFaqUseCase(private val repository: FaqRepository) {
    suspend fun getFaqTypes() = repository.getFaqTypes()
    suspend fun getFaqItems(id: Int) = repository.getFaqItems(id)
}