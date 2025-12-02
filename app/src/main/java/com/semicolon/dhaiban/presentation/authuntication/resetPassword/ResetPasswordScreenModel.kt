package com.semicolon.dhaiban.presentation.authuntication.resetPassword

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.authuntication.login.LoginScreenUiEffect
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.utils.ValidationHandler
import com.semicolon.domain.entity.User
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.usecase.UserValidationUseCase
import com.semicolon.domain.utils.AuthorizationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ResetPasswordScreenModel(
    private val phone: String,
    private val otpCode: String,
    private val userAuthenticationUseCase: UserAuthenticationUseCase,
    private val authValidation: UserValidationUseCase,
    private val validationHelper: ValidationHandler,
    private val localPref: LocalConfigurationUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase
) :
    BaseScreenModel<ResetPasswordUiState, ResetPasswordUiEffect>(ResetPasswordUiState()),
    ResetPasswordInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    override fun onClickResetButton(password: String, reEnteredPassword: String) {
        // call end point of reset pass
        if (validateNewPasswordFields(password = password, reEnteredPassword = reEnteredPassword)) {
            updateState { it.copy(isLoading = true) }
            tryToExecute(
                function = {
                    userAuthenticationUseCase.resetUserPassword(
                        phoneOrEmail = phone,
                        otpVerificationCode = otpCode,
                        password = password
                    )
                },
                onSuccess = ::onResetPasswordSuccess,
                onError = ::onError
            )
        }

    }


    private fun onResetPasswordSuccess(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            localPref.saveUserToken(user.userToken)
            Log.d("saveUserToken5",user.userToken)

            Log.d("onResetPasswordSuccess",user.deviceToken)
            localPref.saveIsActive(true)
            Log.d("onLoginUserSuccessaaaa", user.userToken)

            localPref.saveUserName(user.name)
            val localFcmToken = localPref.getFcmToken()
            Log.d("onLoginUserSuccess", localFcmToken)

            tryToExecute(
                function = { manageNotificationUseCase.sendFcmToken(localFcmToken) },
                onSuccess = { sendNewEffect((ResetPasswordUiEffect.OnNavigateToHomeScreen)) },
                onError = { sendNewEffect((ResetPasswordUiEffect.OnNavigateToHomeScreen)) }
            )
        }
        updateState { it.copy(isLoading = false) }
//        sendNewEffect(ResetPasswordUiEffect.OnNavigateToHomeScreen)
    }

    private fun onError(exception: Exception) {
        if (exception is AuthorizationException.UserNotFoundException) {
            updateState {
                it.copy(
                    isLoading = false,
                    passwordMessage = exception.message.toString(),
                    reEnteredPasswordMessage = exception.message.toString(),
                )
            }
        }
    }

    private fun validateNewPasswordFields(password: String, reEnteredPassword: String): Boolean {
        val passwordValidation = authValidation.validateResetPasswordFieldsFromForgetPassword(
            password,
            reEnteredPassword
        )

        val passwordValidationMessage = validationHelper.handleValidation(passwordValidation)

        updateState {
            it.copy(
                passwordMessage = passwordValidationMessage,
                reEnteredPasswordMessage = passwordValidationMessage,
                isLoading = false
            )
        }

        return (passwordValidationMessage.isEmpty())
    }

    override fun onClickBackButton() {
        sendNewEffect(ResetPasswordUiEffect.OnNavigateBack)
    }

    override fun onPasswordValueChanged(password: String) {
        val passwordValidation = authValidation.validatePassword(password)
        val passwordValidationMessage = validationHelper.handleValidation(passwordValidation)
        updateState { it.copy(password = password, passwordMessage = passwordValidationMessage) }
    }

    override fun onReEnteredPasswordValueChanged(reEnteredPassword: String) {
        val passwordValidation = authValidation.validatePassword(reEnteredPassword)
        val passwordValidationMessage = validationHelper.handleValidation(passwordValidation)
        updateState { it.copy(reEnteredPassword = reEnteredPassword, reEnteredPasswordMessage = passwordValidationMessage) }
    }
}