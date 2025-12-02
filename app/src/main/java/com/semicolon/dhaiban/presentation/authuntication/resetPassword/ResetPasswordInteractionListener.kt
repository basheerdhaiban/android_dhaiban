package com.semicolon.dhaiban.presentation.authuntication.resetPassword

import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface ResetPasswordInteractionListener : BaseInteractionListener {
    fun onClickResetButton(password:String, reEnteredPassword: String)
    fun onClickBackButton()

    fun onPasswordValueChanged(password: String)
    fun onReEnteredPasswordValueChanged(reEnteredPassword: String)
}