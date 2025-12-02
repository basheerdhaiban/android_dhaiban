package com.semicolon.domain.usecase

import com.semicolon.domain.repository.WalletRepository

class ManageWalletUseCase(private val repository: WalletRepository) {
    suspend fun getCurrentBalance() = repository.getCurrentBalance()
    suspend fun getWalletHistory() = repository.getWalletHistory()
}