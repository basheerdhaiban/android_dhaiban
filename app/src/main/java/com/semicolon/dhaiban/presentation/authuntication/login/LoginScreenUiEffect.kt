package com.semicolon.dhaiban.presentation.authuntication.login

sealed interface LoginScreenUiEffect {

    data object OnNavigateToSignUpScreen : LoginScreenUiEffect
    data object OnNavigateToForgetPasswordScreen : LoginScreenUiEffect
    data object OnNavigateToHomeScreen : LoginScreenUiEffect

    data object OnNavigateToOtpScreen : LoginScreenUiEffect
}