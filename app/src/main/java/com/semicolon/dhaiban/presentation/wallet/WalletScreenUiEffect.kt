package com.semicolon.dhaiban.presentation.wallet

sealed interface WalletScreenUiEffect {
    data object OnNavigateBack : WalletScreenUiEffect
    data object OnNavigateToNotificationScreen : WalletScreenUiEffect
}