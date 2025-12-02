package com.semicolon.dhaiban.presentation.wallet

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.domain.entity.WalletHistoryModel

data class WalletScreenUiState(
    val isLoading: Boolean = false,
    val balance: Double = 0.0,
    val walletHistory: List<WalletHistoryUiState> = emptyList(),
    val currency: WalletCurrencyUiState = WalletCurrencyUiState() ,
    val countOfUnreadMessage :Int = 0
)

@Immutable
data class WalletCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)

data class WalletHistoryUiState(
    val amount: Double = 0.0,
    val approval: Int = 0,
    val createdAt: String = "",
    val id: Int = 0,
    val paymentDetails: String = "",
    val paymentMethod: String = ""
)

fun AppCurrencyUiState.toWalletCurrencyUiState() =
    WalletCurrencyUiState(code, exchangeRate, id, name, symbol)

fun WalletHistoryModel.toWalletHistoryUiState() = WalletHistoryUiState(
    amount, approval, createdAt, id, paymentDetails, paymentMethod
)