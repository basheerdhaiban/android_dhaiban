package com.semicolon.dhaiban.presentation.authuntication.otp

data class OtpUiState(
    val isLoading: Boolean = false,
    var isotpError: Boolean = false,

    val errorMessage: String = "",
    var messageForResend: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val isFromSignUpScreen: Boolean = false,
)
