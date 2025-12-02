package com.semicolon.dhaiban.presentation.authuntication.forgetPassword

import androidx.compose.ui.res.stringResource
import androidx.core.util.PatternsCompat.EMAIL_ADDRESS
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.utils.ValidationHandler
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.usecase.UserValidationUseCase
import com.semicolon.domain.utils.AuthorizationException
import io.ktor.util.collections.setValue
import kotlinx.coroutines.CoroutineScope

class ForgetPasswordScreenModel(
    private val userAuthenticationUseCase: UserAuthenticationUseCase,
    private val authValidation: UserValidationUseCase,
    private val validationHelper: ValidationHandler
) : BaseScreenModel<ForgetPasswordUiState, ForgetPasswordUiEffect>(
    ForgetPasswordUiState()
), ForgetPasswordInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    override fun onClickConfirmButton(phoneNumber: String, email: String) {
        if (localFieldValidation(email = email, phone = phoneNumber)) {
            updateState { it.copy(isLoading = true) }
            tryToExecute(
                function = { userAuthenticationUseCase.checkIfUserExists(phoneNumber) },
                onSuccess = { onCheckUserExistsSuccess(otpCode = it, phoneNumber) },
                onError = ::onError
            )
        }
    }

    override fun validatePhoneNumber(countryCode: String, phoneNumber: String?): String? =
        validationHelper.validatePhoneNumber(countryCode, phoneNumber)

    override fun validateEmail(email: String?): String? {
        return  validationHelper.validateEmail(email = email)
    }


    private fun onCheckUserExistsSuccess(otpCode: String, phoneNumber: String) {
        updateState { it.copy(isLoading = false) }
        sendNewEffect(
            ForgetPasswordUiEffect.OnNavigateToOtpScreen(
                phoneNumber = phoneNumber,
                email = "",
                otpCode = otpCode
            )
        )
    }

    private fun onError(exception: Exception) {
        if (exception is AuthorizationException.UserNotFoundException) {
            updateState { it.copy(errorMessage = exception.message.toString(), isLoading = false) }
        }
    }

    private fun localFieldValidation(email: String, phone: String): Boolean {
        var validationMessage = ""
        if (email.isNotEmpty()) {
            val emailValidation = authValidation.validateEmail(email)
            validationMessage = validationHelper.handleValidation(emailValidation)
        } else {
            val phoneValidation = authValidation.validatePhone(phone)
            validationMessage = validationHelper.handleValidation(phoneValidation)
        }
        updateState { it.copy(errorMessage = validationMessage, isLoading = false) }
        return (validationMessage.isEmpty())
    }

    override fun onNavigateBack() {
        sendNewEffect(ForgetPasswordUiEffect.OnNavigateBack)
    }

    override fun onMobileNumberValueChanged(mobileNumber: String) {
        val mobileNumberValidation = authValidation.validatePhone(mobileNumber)
        val mobileNumberValidationMessage = validationHelper.handleValidation(mobileNumberValidation)
        updateState { it.copy(phoneNumber = mobileNumber, errorMessage = mobileNumberValidationMessage) }
    }

    override fun onEmailValueChanged(email: String) {
        val emailValidation = authValidation.validateEmail(email)
        val emailValidationMessage = validationHelper.handleValidation(emailValidation)
        updateState { it.copy(email = email, errorMessage = emailValidationMessage) }
    }

    override fun onPhoneValueChanged(phone: String) {
        val phoneValidation = authValidation.validatePhone(phone)
        val phoneValidationMessage = validationHelper.handleValidation(phoneValidation)

        updateState { it.copy(phoneNumber = phone, errorMessage = phoneValidationMessage) }
    }

    override fun onChangeAuthOption() {
        updateState { it.copy(loginWithPhoneNum = !state.value.loginWithPhoneNum) }
    }
}