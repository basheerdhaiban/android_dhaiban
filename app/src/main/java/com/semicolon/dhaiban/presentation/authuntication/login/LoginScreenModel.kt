package com.semicolon.dhaiban.presentation.authuntication.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.semicolon.domain.entity.User

import cafe.adriel.voyager.core.model.screenModelScope
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.utils.ValidationHandler
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.usecase.UserValidationUseCase
import com.semicolon.domain.utils.AuthorizationException
import com.semicolon.domain.utils.BadRequestExceptionTest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginScreenModel(
    private val userAuthenticationUseCase: UserAuthenticationUseCase,
    private val localPref: LocalConfigurationUseCase,
    private val authValidation: UserValidationUseCase,
    private val validationHelper: ValidationHandler,
    private val manageNotificationUseCase: ManageNotificationUseCase
) : BaseScreenModel<LoginScreenUiState, LoginScreenUiEffect>(LoginScreenUiState()),
    LoginScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    override fun onClickLogin(email: String, phoneNumber: String, password: String) {
        updateState { it.copy(isLoading = true) }
        if (localLoginValidation(
                email = email,
                phone = phoneNumber,
                password = password,
                loginByPhone = _state.value.loginWithPhoneNum
            )
        ) {
            updateState { it.copy(isLoading = true) }
            tryToExecute(
                function = {
                    if (!_state.value.loginWithPhoneNum) {
                        userAuthenticationUseCase.loginUserByPhoneNumber(
                            phone =phoneNumber,
                            password = password
                        )
                    } else {
                        userAuthenticationUseCase.loginUserByEmail(
                            email = email,
                            password = password
                        )
                    }
                },
                onSuccess = ::onLoginUserSuccess,
                onError = ::onError
            )
        }


    }
    override fun validatePhoneNumber(countryCode: String, phoneNumber: String?): String? =
        validationHelper.validatePhoneNumber(countryCode, phoneNumber)

    override fun validateEmail(email: String?): String? =
        validationHelper.validateEmail(email = email)
    private fun onLoginUserSuccess(token: User) {
        updateState { it.copy(isLoading = false) }
        viewModelScope.launch(Dispatchers.IO) {
            if (token.userToken=="0"){
                Log.d("onLoginUserSuccess=0","onLoginUserSuccess=0")
                localPref.saveUserToken(token.userToken)
                Log.d("saveUserToken1",token.userToken)
                localPref.saveIsActive(false)
                Log.d("onLoginUserSuccess", token.userToken)
                val localFcmToken = localPref.getFcmToken()
                Log.d("onLoginUserSuccess", localFcmToken)
                sendNewEffect(LoginScreenUiEffect.OnNavigateToOtpScreen)

            }
            else{
                localPref.saveUserToken(token.userToken)
                Log.d("saveUserToken2",token.userToken)
                localPref.saveIsActive(true)
                localPref.saveUserName(token.name)
                Log.d("onLoginUserSuccessaaaa", token.userToken)
                Log.d("onLoginUserSuccess", token.deviceToken)
                val localFcmToken = localPref.getFcmToken()
                Log.d("onLoginUserSuccess", localFcmToken)

            tryToExecute(
                function = { manageNotificationUseCase.sendFcmToken(localFcmToken) },
                onSuccess = { sendNewEffect(LoginScreenUiEffect.OnNavigateToHomeScreen) },
                onError = { sendNewEffect(LoginScreenUiEffect.OnNavigateToHomeScreen) }
            )
            }
        }

    }

    private fun onError(exception: Exception) {
        if (exception is AuthorizationException.InvalidInputsException) {
            updateState {
                it.copy(
                    emailErrorMessage = exception.message.toString(),
                    phoneErrorMessage = exception.message.toString(),
                    passwordErrorMessage = exception.message.toString(),
                    isLoading = false
                )
            }
        }
        if (exception is AuthorizationException.AccountActivationException) {
            Log.d("saveUserToken3",exception.message.toString())
            updateState { it.copy(isLoading = false) }
            viewModelScope.launch(Dispatchers.IO) { localPref.saveUserToken(exception.message.toString()) }
            sendNewEffect(LoginScreenUiEffect.OnNavigateToOtpScreen)
        }
        if (exception is BadRequestExceptionTest) {
            updateState {
                it.copy(
                    passwordErrorMessage = exception.message.toString(),
                    isLoading = false
                )
            }
        }
    }


    override fun onClickSignUp() {
        sendNewEffect(LoginScreenUiEffect.OnNavigateToSignUpScreen)
    }

    override fun onClickForgetPassword() {
        sendNewEffect(LoginScreenUiEffect.OnNavigateToForgetPasswordScreen)
    }

    override fun onChangeAuthOption() {
        updateState { it.copy(loginWithPhoneNum = !it.loginWithPhoneNum) }
    }

    override fun onSwitchAuthWay() {
        updateState { it.copy(loginWithPhoneNum = !state.value.loginWithPhoneNum) }
    }

    override fun onPhoneNumberTextChanged(phoneNumber: String) {
        updateState { it.copy(phoneNumber = phoneNumber) }

    }

    override fun onEmailTextChanged(email: String) {
        updateState { it.copy(email = email) }
    }

    override fun onPasswordTextChanged(password: String) {
        updateState { it.copy(password = password) }
    }
    override fun onPasswordValueChanged(password: String) {
        val passwordValidation = authValidation.validatePassword(password)
        val passwordValidationMessage = validationHelper.handleValidation(passwordValidation)
        updateState {
            it.copy(
                password = password,
                passwordErrorMessage = passwordValidationMessage
            )
        }
    }


    override fun onPhoneValueChanged(phone: String) {
        val phoneValidation = authValidation.validatePhone(phone)
        val phoneValidationMessage = validationHelper.handleValidation(phoneValidation)

        updateState { it.copy(phoneNumber = phone, phoneErrorMessage = phoneValidationMessage) }
    }


    override fun onEmailValueChanged(email: String) {
        val emailValidation = authValidation.validateEmail(email)
        val emailValidationMessage = validationHelper.handleValidation(emailValidation)
        updateState { it.copy(email = email, emailErrorMessage = emailValidationMessage) }
    }

    private fun localLoginValidation(
        email: String,
        phone: String,
        password: String,
        loginByPhone: Boolean
    ): Boolean {

        val emailValidation = authValidation.validateEmail(email)
        val phoneValidation = authValidation.validatePhone(phone)
        val passwordValidation = authValidation.validatePassword(password)

        val emailValidationMessage = validationHelper.handleValidation(emailValidation)
        val phoneValidationMessage = validationHelper.handleValidation(phoneValidation)
        val passwordValidationMessage = validationHelper.handleValidation(passwordValidation)

        updateState {
            it.copy(
                emailErrorMessage = emailValidationMessage,
                phoneErrorMessage = phoneValidationMessage,
                passwordErrorMessage = passwordValidationMessage,
                isLoading = false
            )
        }

        if (loginByPhone) {
            return (emailValidationMessage.isEmpty() && passwordValidationMessage.isEmpty())
        }

        return (phoneValidationMessage.isEmpty() && passwordValidationMessage.isEmpty())
    }

    override fun openWhatsApp(context: Context) {
        val uri = Uri.parse( "https://api.whatsapp.com/send?phone=+250780888888")
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun isAppInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }

}