package com.semicolon.dhaiban.presentation.authuntication.resetPassword

sealed interface ResetPasswordUiEffect {

    data object OnNavigateBack : ResetPasswordUiEffect
    data object OnNavigateToHomeScreen : ResetPasswordUiEffect

}