package com.semicolon.dhaiban.presentation.trackrefund

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.refund.RefundProductUiState
import com.semicolon.dhaiban.presentation.refund.RefundStatus

data class TrackRefundUiState(
    val isLoading: Boolean = false,
    val currency: TrackRefundCurrencyUiState = TrackRefundCurrencyUiState(),
    val refundStatus: RefundStatus = RefundStatus.PENDING,
    val orderNumber: String = "",
    val receiver: String = "",
    val orderDate: String = "",
    val totalPrice: Double = 0.0,
    val refundProduct: RefundProductUiState = RefundProductUiState(),
)

@Immutable
data class TrackRefundCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)

fun AppCurrencyUiState.toTrackRefundCurrencyUiState() =
    TrackRefundCurrencyUiState(code, exchangeRate, id, name, symbol)
