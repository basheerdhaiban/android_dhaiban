package com.semicolon.dhaiban.presentation.authuntication.otp

sealed interface OtpScreenUiEffect {
    data class OnNavigateToResetPasswordScreen(
        val phoneNumber: String,
        val email: String,
        val otpCode: String
    ) : OtpScreenUiEffect

    data object OnNavigateBack : OtpScreenUiEffect

    data object OnNavigateToHomeScreen : OtpScreenUiEffect
}