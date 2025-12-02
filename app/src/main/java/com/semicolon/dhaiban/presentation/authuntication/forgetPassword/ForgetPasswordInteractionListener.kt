package com.semicolon.dhaiban.presentation.authuntication.forgetPassword

import android.content.Context
import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface ForgetPasswordInteractionListener : BaseInteractionListener {

    fun onClickConfirmButton(phoneNumber: String, email: String)

    fun validatePhoneNumber(countryCode: String, phoneNumber: String?): String?
    fun validateEmail(email: String?): String?

    fun onNavigateBack()

    fun onMobileNumberValueChanged(mobileNumber: String)

    fun onEmailValueChanged(email: String)
    fun onPhoneValueChanged(phone: String)

    fun onChangeAuthOption()

}