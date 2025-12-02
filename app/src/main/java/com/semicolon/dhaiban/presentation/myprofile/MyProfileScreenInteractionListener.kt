package com.semicolon.dhaiban.presentation.myprofile

import android.content.Context

interface MyProfileScreenInteractionListener {

    fun onClickUpButton()
    fun onUsernameValueChanged(userName: String)
    fun onMobileNumberValueChanged(mobileNumber: String)
    fun onEmailValueChanged(email: String)
    fun onPasswordValueChanged(password: String)
    fun onCurrentPasswordValueChanged(currentPassword: String)
    fun onNewPasswordValueChanged(newPassword: String)
    fun onReEnterNewPasswordValueChanged(reEnterNewPassword: String)
    fun onChangePhoneNumberValueChanged(phone: String)
    fun onChangePhonePasswordChanged(password: String)
    fun onClickChangePhone()
    fun onSubmitChangePhone(code: String)
    fun onClickSendOtp()
    fun onClickSave(context: Context)
    fun onClickChangePassword()
    fun onDismissPasswordBottomSheet()
    fun onDismissPhoneBottomSheet()
    fun onDismissOtpBottomSheet()
    fun onClickResetPassword()
    fun onDismissAllBottomSheets()
    fun onClickDeleteAccount()
    fun onDismissDeleteDialog()
    fun onConfirmDeleteAccount()
    fun onClickNotification()
    fun onClickChangeImage()
}