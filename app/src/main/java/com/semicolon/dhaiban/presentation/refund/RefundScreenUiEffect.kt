package com.semicolon.dhaiban.presentation.refund

sealed interface RefundScreenUiEffect {
    data object OnNavigateBack : RefundScreenUiEffect
    data class OnNavigateToTrackRefund(
        val refundItem: RefundItemUiState,
        val requestType: RequestType
    ) : RefundScreenUiEffect
    data object OnNavigateToNotificationScreen : RefundScreenUiEffect
}