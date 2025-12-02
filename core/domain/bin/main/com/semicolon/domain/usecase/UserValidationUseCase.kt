package com.semicolon.domain.usecase

import com.semicolon.domain.utils.ValidationState

class UserValidationUseCase {
    fun validateUsername(username: String): ValidationState {
        return when {
            username.isBlank() -> ValidationState.BLANK_USER_NAME
            username.length < 4 -> ValidationState.SHORT_USER_NAME

            else -> ValidationState.SUCCESS
        }
    }

    fun validatePassword(password: String): ValidationState {
        return when {
            password.isBlank() -> ValidationState.BLANK_PASSWORD
            password.length < 6 -> ValidationState.SHORT_PASSWORD

            else -> ValidationState.SUCCESS
        }
    }

    fun validateEmail(email: String): ValidationState {
        return when {
            email.isBlank() -> ValidationState.BLANK_EMAIL
            !email.matches(emailRegex) -> ValidationState.INVALID_EMAIL

            else -> ValidationState.SUCCESS
        }
    }

    fun validatePhone(phone: String): ValidationState {
        return when {
            phone.isBlank() -> ValidationState.BLANK_PHONE
            phone.length < 9 -> ValidationState.INVALID_PHONE

            else -> ValidationState.SUCCESS
        }
    }

    fun validateOtpCode(otpCode: String): ValidationState {
        return when {
            otpCode.isBlank() -> ValidationState.BLANK_OTP
            otpCode.length < 4 -> ValidationState.SHORT_OTP

            else -> ValidationState.SUCCESS
        }
    }

    fun validateOtpCodeForResetPassword(
        userOtpCode: String,
        serverOtpCode: String
    ): ValidationState {
        return when {
            userOtpCode.isBlank() -> ValidationState.BLANK_OTP
            userOtpCode.length < 4 -> ValidationState.SHORT_OTP
            userOtpCode != serverOtpCode -> ValidationState.INVALID_OTP

            else -> ValidationState.SUCCESS
        }
    }

    fun validateResetPasswordFieldsFromForgetPassword(
        password: String,
        reEnteredPassword: String
    ): ValidationState {
        return when {
            password.isBlank() -> ValidationState.BLANK_PASSWORD
            password.length < 6 -> ValidationState.SHORT_PASSWORD
            reEnteredPassword != password -> ValidationState.NOT_SAME

            else -> ValidationState.SUCCESS
        }
    }


    private companion object {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$".toRegex()
    }
}