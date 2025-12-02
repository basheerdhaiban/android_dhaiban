package com.semicolon.dhaiban.presentation.authuntication.forgetPassword

sealed interface ForgetPasswordUiEffect {
    data object OnNavigateBack : ForgetPasswordUiEffect

    data class OnNavigateToOtpScreen(
        val email: String,
        val phoneNumber: String,
        val otpCode: String,
    ) : ForgetPasswordUiEffect
}