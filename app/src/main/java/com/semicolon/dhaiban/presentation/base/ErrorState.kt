package com.semicolon.dhaiban.presentation.base

import com.semicolon.domain.utils.AuthorizationException
import com.semicolon.domain.utils.BadRequestException
import com.semicolon.domain.utils.DhaibanException
import com.semicolon.domain.utils.InternetException

sealed interface ErrorState {
    data object NoInternet : ErrorState
    data object BadRequest : ErrorState
    data object InvalidUsername : ErrorState
    data object InvalidPassword : ErrorState
    data object InvalidEmail : ErrorState
    data object InvalidPhone : ErrorState
    data object Idle : ErrorState
}

fun handelAuthenticationException(
    exception: AuthorizationException,
    onError: (t: ErrorState) -> Unit,
) {
    when (exception) {
        is AuthorizationException.InvalidUsernameException -> onError(ErrorState.InvalidUsername)
        is AuthorizationException.InvalidEmailException -> onError(ErrorState.InvalidEmail)
        is AuthorizationException.InvalidPhoneException -> onError(ErrorState.InvalidPhone)
        is AuthorizationException.InvalidPasswordException -> onError(ErrorState.InvalidPassword)
    }
}