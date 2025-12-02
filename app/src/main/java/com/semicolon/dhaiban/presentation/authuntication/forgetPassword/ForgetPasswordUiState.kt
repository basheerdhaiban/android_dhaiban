package com.semicolon.dhaiban.presentation.authuntication.forgetPassword

data class ForgetPasswordUiState(
    val isLoading: Boolean = false,
    val phoneNumber: String = "",
    val email: String = "",
    val errorMessage: String = "",
    var countryCode: String ="",
    var loginWithPhoneNum: Boolean = false
)
