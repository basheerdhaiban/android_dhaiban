package com.semicolon.dhaiban.presentation.authuntication.signUp

import android.content.Context
import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface SignUpScreenInteractionListener : BaseInteractionListener {
    fun onClickLogin()
    fun onClickSignUp(username: String, email: String, phoneNumber: String, password: String, verificationMethod: Int)
    fun validatePhoneNumber(countryCode: String, phoneNumber: String?):String?
    fun onUsernameValueChanged(userName: String)
    fun onMobileNumberValueChanged(mobileNumber: String)
    fun onEmailValueChanged(email: String)
    fun onPasswordValueChanged(password: String)
    fun openWhatsApp(context: Context)
}