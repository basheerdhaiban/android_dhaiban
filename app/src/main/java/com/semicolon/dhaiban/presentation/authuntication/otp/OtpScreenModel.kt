package com.semicolon.dhaiban.presentation.authuntication.otp

import android.util.Log
import android.widget.Toast
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.utils.ValidationHandler
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.usecase.UserValidationUseCase
import com.semicolon.domain.utils.AuthorizationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OtpScreenModel(
    private val email: String,
    private val phoneNumber: String,
    private val isFromSignUpScreen: Boolean,
    private val otpCode: String,
    private val userName: String,
    private val userAuthenticationUseCase: UserAuthenticationUseCase,
    private val localPref: LocalConfigurationUseCase,
    private val authValidation: UserValidationUseCase,
    private val validationHelper: ValidationHandler
) : BaseScreenModel<OtpUiState, OtpScreenUiEffect>(OtpUiState()), OtpScreenInteractionListener {

    override val viewModelScope: CoroutineScope = screenModelScope
    init {
        viewModelScope.launch(Dispatchers.IO) {

            localPref.saveIsActive(false)
            localPref.saveUserName("Guest")
        }
    }
    override fun onClickBackIcon() {
        sendNewEffect(OtpScreenUiEffect.OnNavigateBack)
    }

    override fun onClickResendOtp() {
        resendOtp()
    }

    override fun onClickConfirm(otpVerificationCode: String) {
        if (isFromSignUpScreen) {
            activeUserAccount(otpVerificationCode)
        } else if (phoneNumber.isNotEmpty() || email.isNotEmpty()) {
            if (localOtpValidationForResetPassword(otpVerificationCode, otpCode)) {
                sendNewEffect(
                    OtpScreenUiEffect.OnNavigateToResetPasswordScreen(
                        phoneNumber = phoneNumber,
                        otpCode = otpVerificationCode,
                        email = ""
                    )
                )
            }
        }
    }


    private fun activeUserAccount(otpVerificationCode: String) {
        if (localOtpValidation(otpVerificationCode)) {
            updateState { it.copy(isLoading = true) }
            tryToExecute(
                function = { userAuthenticationUseCase.activateUserAccount(otpVerificationCode) },
                onSuccess = ::onActiveAccountSuccess,
                onError = {
                    viewModelScope.launch(Dispatchers.IO) {
                    localPref.saveIsActive(false)}
                    Log.e("OtpError", it.message.toString())
                          updateState { it.copy(isLoading = false,isotpError= true) }}
            )
        }
    }
    private fun onActiveAccountSuccess(token: String) {
        Log.e("OtpScreenModel", token)
        updateState { it.copy(isLoading = false) }
        viewModelScope.launch(Dispatchers.IO) {
            localPref.saveUserToken(token)
            Log.d("saveUserToken4",token.toString())

            localPref.saveIsActive(true)
            localPref.saveUserName(userName)

        }
        sendNewEffect(OtpScreenUiEffect.OnNavigateToHomeScreen)

    }

    private fun onActiveresendOtp(message: String) {

        updateState { it.copy(isLoading = false,messageForResend=message) }
        viewModelScope.launch(Dispatchers.IO) {


        }


    }
    private fun resendOtp() {

            updateState { it.copy(isLoading = true) }
            tryToExecute(
                function = { userAuthenticationUseCase.resendOtp() },
                onSuccess = ::onActiveresendOtp,
                onError = {

                    updateState { it.copy(isLoading = false) }}
            )

    }
    private fun onError(exception: Exception) {
        if (exception is AuthorizationException.InvalidOtpCodeException) {
            updateState { it.copy(errorMessage = exception.message.toString(), isLoading = false) }
        }
    }

    private fun localOtpValidation(otpVerificationCode: String): Boolean {
        val otpCodeValidation = authValidation.validateOtpCode(otpVerificationCode)
        val otpErrorMessage = validationHelper.handleValidation(otpCodeValidation)

        updateState {
            it.copy(
                errorMessage = otpErrorMessage,
                isLoading = false
            )
        }

        return (otpErrorMessage.isEmpty())
    }


    private fun localOtpValidationForResetPassword(
        userOtpCode: String,
        serverOtpCode: String
    ): Boolean {
        val otpCodeValidation =
            authValidation.validateOtpCodeForResetPassword(userOtpCode, serverOtpCode)
        val otpErrorMessage = validationHelper.handleValidation(otpCodeValidation)

        updateState {
            it.copy(
                errorMessage = otpErrorMessage,
                isLoading = false
            )
        }

        return (otpErrorMessage.isEmpty())

    }

}