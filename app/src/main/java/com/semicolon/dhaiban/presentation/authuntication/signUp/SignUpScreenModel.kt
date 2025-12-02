package com.semicolon.dhaiban.presentation.authuntication.signUp

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.base.ErrorState
import com.semicolon.dhaiban.presentation.utils.ValidationHandler
import com.semicolon.domain.entity.Account
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.usecase.UserValidationUseCase
import com.semicolon.domain.utils.AuthorizationException
import com.semicolon.domain.utils.InternetException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpScreenModel(
    private val userAuthenticationUseCase: UserAuthenticationUseCase,
    private val localPref: LocalConfigurationUseCase,
    private val authValidation: UserValidationUseCase,
    private val validationHelper: ValidationHandler,
    private val manageNotificationUseCase: ManageNotificationUseCase
) : BaseScreenModel<SignUpScreenUiState, SignUpScreenUiEffect>(SignUpScreenUiState()),
    SignUpScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    override fun onClickLogin() {
        sendNewEffect(SignUpScreenUiEffect.OnNavigateToLoginScreen)
    }

    override fun onClickSignUp(
        username: String,
        email: String,
        phoneNumber: String,
        password: String,
        verification_method: Int
    ) {
        updateState { it.copy(isLoading = true) }
        registerNewUser(username, email, phoneNumber, password, verification_method)
    }

    override fun validatePhoneNumber(countryCode: String, phoneNumber: String?): String? =
        validationHelper.validatePhoneNumber(countryCode, phoneNumber)

    override fun onUsernameValueChanged(userName: String) {
        val userNameValidation = authValidation.validateUsername(userName)
        val userNameValidationMessage = validationHelper.handleValidation(userNameValidation)
        updateState {
            it.copy(
                username = userName,
                usernameErrorMessage = userNameValidationMessage
            )
        }
    }

    override fun onMobileNumberValueChanged(mobileNumber: String) {
        val mobileNumberValidation = authValidation.validatePhone(mobileNumber)
        val mobileNumberValidationMessage =
            validationHelper.handleValidation(mobileNumberValidation)
        updateState {
            it.copy(
                phoneNumber = mobileNumber,
                phoneErrorMessage = mobileNumberValidationMessage
            )
        }
    }

    override fun onEmailValueChanged(email: String) {
        val emailValidation = authValidation.validateEmail(email)
        val emailValidationMessage = validationHelper.handleValidation(emailValidation)
        updateState { it.copy(email = email, emailErrorMessage = emailValidationMessage) }
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

    override fun openWhatsApp(context: Context) {
        val uri = Uri.parse( "https://api.whatsapp.com/send?phone=+250780888888")
        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    private fun registerNewUser(username: String, email: String, phone: String, password: String, verification_method: Int) {
        if (localRegisterValidation(username, email, phone, password)) {
            updateState { it.copy(isLoading = true) }
            tryToExecute(
                function = {
                    userAuthenticationUseCase.signUpUser(
                        Account(
                            username = username,
                            email = email,
                            phone = phone,
                            password = password,
                            verificationMethod = verification_method
                        )
                    ) to username
                },
                onSuccess = ::onRegisterUserSuccess,
                onError = ::onRegisterUserError
            )
        }
    }

    private fun onRegisterUserSuccess(data: Pair<String, String>) {
        val token = data.first
        val username = data.second
        viewModelScope.launch(Dispatchers.IO) {
            localPref.saveUserToken(token)
            Log.d("saveUserToken6",token.toString())

            localPref.saveUserName(username)
            val localFcmToken = localPref.getFcmToken()
            tryToExecute(
                function = { manageNotificationUseCase.sendFcmToken(localFcmToken) },
                onSuccess = {  sendNewEffect(SignUpScreenUiEffect.OnNavigateToOtpScreen)},
                onError = {  sendNewEffect(SignUpScreenUiEffect.OnNavigateToOtpScreen) }
            )
        }
        updateState { it.copy(isLoading = false) }

    }

    private fun localRegisterValidation(
        username: String,
        email: String,
        phone: String,
        password: String
    ): Boolean {

        val usernameValidation = authValidation.validateUsername(username)
        val emailValidation = authValidation.validateEmail(email)
        val phoneValidation = authValidation.validatePhone(phone)
        val passwordValidation = authValidation.validatePassword(password)

        val usernameValidationMessage = validationHelper.handleValidation(usernameValidation)
        val emailValidationMessage = validationHelper.handleValidation(emailValidation)
        val phoneValidationMessage = validationHelper.handleValidation(phoneValidation)
        val passwordValidationMessage = validationHelper.handleValidation(passwordValidation)

        updateState {
            it.copy(
                usernameErrorMessage = usernameValidationMessage,
                emailErrorMessage = emailValidationMessage,
                phoneErrorMessage = phoneValidationMessage,
                passwordErrorMessage = passwordValidationMessage,
                isLoading = false
            )
        }

        return (
                usernameValidationMessage.isEmpty()
                        && emailValidationMessage.isEmpty()
                        && phoneValidationMessage.isEmpty()
                        && passwordValidationMessage.isEmpty()
                )
    }

    private fun onRegisterUserError(exception: Exception) {
        if (exception is AuthorizationException.InvalidUsernameException) {
            updateState {
                it.copy(
                    usernameErrorMessage = exception.message.toString(),
                    isLoading = false
                )
            }
        }
        if (exception is AuthorizationException.InvalidEmailException) {
            updateState {
                it.copy(
                    emailErrorMessage = exception.message.toString(),
                    isLoading = false
                )
            }
        }
        if (exception is AuthorizationException.InvalidPhoneException) {
            updateState {
                it.copy(
                    phoneErrorMessage = exception.message.toString(),
                    isLoading = false
                )
            }
        }
        if (exception is AuthorizationException.InvalidPasswordException) {
            updateState {
                it.copy(
                    passwordErrorMessage = exception.message.toString(),
                    isLoading = false
                )
            }
        }
        if (exception is InternetException) {
            updateState { it.copy(errorState = ErrorState.NoInternet, isLoading = false) }
        }
    }
}



