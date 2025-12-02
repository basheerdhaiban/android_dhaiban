package com.semicolon.dhaiban.presentation.authuntication.otp

import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface OtpScreenInteractionListener : BaseInteractionListener {

    fun onClickBackIcon()

    fun onClickResendOtp()

    fun onClickConfirm(otpVerificationCode: String)
}