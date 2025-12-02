package com.semicolon.dhaiban.presentation.authuntication.login

import android.content.Context
import com.semicolon.dhaiban.presentation.base.BaseInteractionListener


interface LoginScreenInteractionListener : BaseInteractionListener {

    fun onClickLogin(email: String, phoneNumber: String, password: String)

    fun validatePhoneNumber(countryCode: String, phoneNumber: String?):String?
    fun validateEmail(email: String?): String?

    fun onClickSignUp()

    fun onClickForgetPassword()

    fun onChangeAuthOption()

    fun onPhoneNumberTextChanged(phoneNumber: String)

    fun onEmailTextChanged(email: String)

    fun onPasswordTextChanged(password: String)

    fun onSwitchAuthWay()

    fun onPhoneValueChanged(phone: String)

    fun onPasswordValueChanged(password: String)

    fun onEmailValueChanged(email: String)

    fun openWhatsApp(context: Context)
}