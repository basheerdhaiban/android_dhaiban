package com.semicolon.dhaiban.presentation.welcome

sealed interface WelcomeUiEffect {
    data object OnNavigateToLoginScreen : WelcomeUiEffect
    data object OnNavigateToSignUpScreen : WelcomeUiEffect
    data object OnNavigateToHomeScreenScreen : WelcomeUiEffect
}