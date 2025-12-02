package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.wallet.BalanceDto
import com.semicolon.data.repository.remote.model.wallet.WalletHistoryDto
import com.semicolon.domain.entity.BalanceModel
import com.semicolon.domain.entity.WalletHistoryModel

fun BalanceDto.toBalanceModel() = BalanceModel(balance = this.balance ?: 0.0)

fun WalletHistoryDto.toWalletHistoryModel() =
    WalletHistoryModel(
        amount ?: 0.0,
        approval ?: 0,
        createdAt ?: "",
        id ?: 0,
        paymentDetails ?: "",
        paymentMethod ?: ""
    )
