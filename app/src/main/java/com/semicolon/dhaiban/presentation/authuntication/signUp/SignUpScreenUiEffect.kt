package com.semicolon.dhaiban.presentation.authuntication.signUp

sealed interface SignUpScreenUiEffect {

    data object OnNavigateToLoginScreen : SignUpScreenUiEffect
    data object OnNavigateToOtpScreen : SignUpScreenUiEffect
}