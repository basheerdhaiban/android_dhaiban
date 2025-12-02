package com.semicolon.domain.repository

import com.semicolon.domain.entity.BalanceModel
import com.semicolon.domain.entity.WalletHistoryModel

interface WalletRepository {
    suspend fun getCurrentBalance(): BalanceModel
    suspend fun getWalletHistory(): List<WalletHistoryModel>
}