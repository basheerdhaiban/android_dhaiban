package com.semicolon.dhaiban.presentation.authuntication.signUp

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.base.ErrorState

@Immutable
data class SignUpScreenUiState(
    val isLoading: Boolean = false,
    val username: String = "",
    val usernameErrorMessage: String = "",
    val email: String = "",
    val emailErrorMessage: String = "",
    val phoneNumber: String = "",
    val phoneErrorMessage: String = "",
    val password: String = "",
    val passwordErrorMessage: String = "",
    val errorState: ErrorState = ErrorState.Idle,
    var countryCode: String = "",
)