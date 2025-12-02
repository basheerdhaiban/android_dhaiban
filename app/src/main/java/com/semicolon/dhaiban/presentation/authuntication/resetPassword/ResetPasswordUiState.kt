package com.semicolon.dhaiban.presentation.authuntication.resetPassword

data class ResetPasswordUiState(
    val isLoading: Boolean = false,
    val password: String = "",
    val passwordMessage: String = "",
    val reEnteredPassword: String = "",
    val reEnteredPasswordMessage: String = "",
)
