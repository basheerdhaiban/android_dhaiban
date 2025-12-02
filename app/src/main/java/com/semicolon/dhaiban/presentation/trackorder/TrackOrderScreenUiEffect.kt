package com.semicolon.dhaiban.presentation.trackorder

sealed interface TrackOrderScreenUiEffect {
    data object OnNavigateBack : TrackOrderScreenUiEffect
    data object OnNavigateToNotificationScreen : TrackOrderScreenUiEffect
    data class OnNavigateToChatScreen(val numberofItem: Int) : TrackOrderScreenUiEffect
}