package com.semicolon.dhaiban.presentation.authuntication.login

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.mutableStateOf
import com.arpitkatiyarprojects.countrypicker.models.CountryDetails

@Immutable
data class LoginScreenUiState(
    val isLoading: Boolean = false,
    val loginWithPhoneNum: Boolean = false,
    var email: String = "",
    var phoneNumber: String = "",
    var password: String = "",
    val emailErrorMessage: String = "",
    val phoneErrorMessage: String = "",
    val passwordErrorMessage: String = "",

    var countryCode: String =""

)
