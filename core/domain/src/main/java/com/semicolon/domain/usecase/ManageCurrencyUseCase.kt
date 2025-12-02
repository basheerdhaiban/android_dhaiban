package com.semicolon.domain.usecase

import com.semicolon.domain.repository.UserRepository

class ManageCurrencyUseCase(private val userRepository: UserRepository) {
    suspend fun getDefaultCurrency() = userRepository.getDefaultCurrency()
}