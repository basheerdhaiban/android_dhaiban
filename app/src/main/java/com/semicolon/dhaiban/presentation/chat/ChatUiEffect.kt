package com.semicolon.dhaiban.presentation.chat

import com.semicolon.dhaiban.presentation.myprofile.MyProfileUiEffect


sealed interface  ChatUiEffect {
    data object OnNavigateToNotificationScreen : ChatUiEffect
    data object OnNavigateBack : ChatUiEffect

}