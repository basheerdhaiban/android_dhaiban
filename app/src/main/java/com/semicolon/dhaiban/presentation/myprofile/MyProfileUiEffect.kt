package com.semicolon.dhaiban.presentation.myprofile

sealed interface MyProfileUiEffect {
    data object OnNavigateBack : MyProfileUiEffect
    data object OnDeleteAccountSuccess : MyProfileUiEffect
    data object OnNavigateToNotificationScreen : MyProfileUiEffect
}