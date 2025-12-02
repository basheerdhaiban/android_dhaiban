package com.semicolon.dhaiban.presentation.trackrefund

sealed interface TrackRefundUiEffect {
    data object OnNavigateBack : TrackRefundUiEffect
    data object OnNavigateToNotificationScreen : TrackRefundUiEffect
}