package com.semicolon.dhaiban.presentation.myprofile

import android.content.Context
import android.util.Log
import android.widget.Toast
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.utils.ValidationHandler
import com.semicolon.domain.entity.User
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.usecase.UserValidationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyProfileScreenModel(
    private val authValidation: UserValidationUseCase,
    private val validationHelper: ValidationHandler,
    private val localConfigurationUseCase: LocalConfigurationUseCase,
    private val userAuthenticationUseCase: UserAuthenticationUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase
) : BaseScreenModel<MyProfileScreenState, MyProfileUiEffect>(
    MyProfileScreenState()
), MyProfileScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getData()
        getCountOfUnreadNotification()
    }
    private fun getCountOfUnreadNotification() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageNotificationUseCase.getUnReadNotificationCount() },
            onSuccess = {
                if (it != null) {
                    _state.update { screenUiState -> screenUiState.copy(
                        isLoading = false,
                        countOfUnreadMessage= it.notifications
                    )  }
                }

            },
            onError ={}
        )
    }
    private fun getData() {
        tryToExecute(
            function = { localConfigurationUseCase.getEmail() },
            onSuccess = { email ->
                _state.update { it.copy(email = email, oldEmail = email) }
            },
            onError = {}
        )
        tryToExecute(
            function = { localConfigurationUseCase.getContactNumber() },
            onSuccess = { phone ->
                _state.update { it.copy(phoneNumber = phone) }
            },
            onError = {}
        )
        tryToExecute(
            function = { localConfigurationUseCase.getUserName() },
            onSuccess = { userName ->
                _state.update { it.copy(username = userName, oldUsername = userName) }
            },
            onError = {}
        )
        tryToExecute(
            function = { localConfigurationUseCase.getImageUrl() },
            onSuccess = { imageUrl ->
                _state.update { it.copy(userImageUrl = imageUrl) }
            },
            onError = {}
        )
    }

    private fun changeNameAndEmail(name: String, email: String) {
        _state.update { it.copy(isLoading = true) }
//        if (name == _state.value.oldUsername && email == _state.value.oldEmail) {
//            viewModelScope.launch {
//                delay(1000)
//                _state.update { it.copy(isLoading = false) }
//            }
//        } else {

        tryToExecute(
            function = { userAuthenticationUseCase.changeNameAndEmail(name, email) },
            onSuccess = ::onChangeNameAndEmailSuccess,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
//        }
    }

    private fun changePassword(oldPassword: String, newPassword: String) {
        _state.update { it.copy(changePasswordLoading = true) }
        tryToExecute(
            function = { userAuthenticationUseCase.changeUserPassword(oldPassword, newPassword) },
            onSuccess = { response ->
                _state.update {
                    it.copy(
                        showPasswordBottomSheet = false,
                        changePasswordLoading = false,
                        passwordErrorMessage = response.error.ifEmpty { response.oldPasswordError }
                    )
                }
            },
            onError = { exception ->
                _state.update {
                    it.copy(
                        changePasswordLoading = false,
                        passwordErrorMessage = exception.message.toString()
                    )
                }
            }
        )
    }

    private fun changePhoneFirstStep(newPhone: String, password: String) {
        _state.update { it.copy(changePhoneLoading = true) }
        tryToExecute(
            function = { userAuthenticationUseCase.changePhoneNumberFirstStep(newPhone, password) },
            onSuccess = { result ->
                if (result == "Success") {
                    _state.update {
                        it.copy(
                            showPhoneBottomSheet = false,
                            changePhoneLoading = false,
                            showOtpBottomSheet = true,
                            changePhoneErrorMessage = ""
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            changePhoneErrorMessage = result,
                            changePhoneLoading = false,
                        )
                    }
                }

            },
            onError = { exception ->
                _state.update {
                    it.copy(
                        changePhoneErrorMessage = exception.message.toString(),
                        changePhoneLoading = false
                    )
                }
            }
        )
    }


    fun uploadProfileImage(text: String, byteArray: ByteArray?, context: Context) {
        tryToExecute(
            function = { userAuthenticationUseCase.uploadImage(text, byteArray) },
            onSuccess = ::handleOnSuccessUploadImage,
            onError = {
                handleOnSuccessUploadImage(false)
            }
        )
    }

    private fun handleOnSuccessUploadImage(isUploaded: Boolean) {
        if (isUploaded) {
            _state.update {
                it.copy(
                    imageIsUpdated = true
                )
            }
        }

    }

    private fun changePhoneSecondStep(newPhone: String, otp: String) {
        _state.update { it.copy(changePhoneLoading = true) }

        tryToExecute(
            function = { userAuthenticationUseCase.changePhoneNumberSecondStep(newPhone, otp) },
            onSuccess = { result ->
                if (result == "Success") {
                    _state.update {
                        it.copy(
                            showOtpBottomSheet = false,
                            changePhoneLoading = false,
                            changePhoneErrorMessage = ""
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            changePhoneErrorMessage = result,
                            changePhoneLoading = false,
                        )
                    }
                }
                viewModelScope.launch(Dispatchers.IO) {
                    localConfigurationUseCase.saveContactNumber(newPhone)
                    getData()
                }
            },
            onError = { exception ->
                _state.update {
                    it.copy(
                        changePhoneErrorMessage = exception.message.toString(),
                        changePhoneLoading = false
                    )
                }
            }
        )

    }

    private fun onChangeNameAndEmailSuccess(user: User) {
        Log.d("onChangeNameAndEmailSuccess", user.fName)
        _state.update { it.copy(isLoading = false, isUpdated = true) }

        viewModelScope.launch(Dispatchers.IO) {
            Log.d("onChangeNameAndEmailSuccess", user.photo)
            localConfigurationUseCase.saveUserName(user.name)
            localConfigurationUseCase.saveEmail(user.email)
            localConfigurationUseCase.saveImageUrl(user.photo)
            Log.d("prfile",localConfigurationUseCase.getUserToken())
            Log.d("prfiless",localConfigurationUseCase.getFcmToken())

        }
        sendNewEffect(MyProfileUiEffect.OnNavigateBack)
    }

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

    override fun onClickUpButton() {
        sendNewEffect(MyProfileUiEffect.OnNavigateBack)
    }

    override fun onClickChangePassword() {
        _state.update { it.copy(showPasswordBottomSheet = true) }
    }

    override fun onDismissPasswordBottomSheet() {
        _state.update { it.copy(showPasswordBottomSheet = false, passwordErrorMessage = "") }
    }

    override fun onCurrentPasswordValueChanged(currentPassword: String) {
        _state.update { it.copy(currentPassword = currentPassword) }
    }

    override fun onNewPasswordValueChanged(newPassword: String) {
        _state.update { it.copy(newPassword = newPassword) }
    }

    override fun onReEnterNewPasswordValueChanged(reEnterNewPassword: String) {
        _state.update { it.copy(reEnterNewPassword = reEnterNewPassword) }
    }

    override fun onClickResetPassword() {
        _state.update { it.copy(passwordErrorMessage = "") }
        if (_state.value.newPassword != _state.value.reEnterNewPassword) {
            _state.update { it.copy(passwordErrorMessage = "New Password Fields Are Not Identical.") }
        } else {
            changePassword(_state.value.currentPassword, _state.value.newPassword)
        }
    }

    override fun onChangePhoneNumberValueChanged(phone: String) {
        _state.update { it.copy(changePhoneNumber = phone) }
    }

    override fun onChangePhonePasswordChanged(password: String) {
        _state.update { it.copy(changePhonePassword = password) }
    }

    override fun onClickChangePhone() {
        _state.update { it.copy(showPhoneBottomSheet = true) }
    }

    override fun onDismissPhoneBottomSheet() {
        _state.update { it.copy(showPhoneBottomSheet = false) }
    }

    override fun onDismissOtpBottomSheet() {
        _state.update { it.copy(showOtpBottomSheet = false) }
    }

    override fun onDismissAllBottomSheets() {
        _state.update {
            it.copy(
                showPhoneBottomSheet = false,
                showPasswordBottomSheet = false,
                showOtpBottomSheet = false,
                passwordErrorMessage = ""
            )
        }
    }

    override fun onSubmitChangePhone(code: String) {
        val otpCodeValidation = authValidation.validateOtpCode(code)
        val otpErrorMessage = validationHelper.handleValidation(otpCodeValidation)
        _state.update { it.copy(changePhoneErrorMessage = otpErrorMessage) }
        if (otpErrorMessage.isEmpty()) {
            changePhoneSecondStep(_state.value.changePhoneNumber, code)
        }
    }

    override fun onClickSendOtp() {
        val newPhoneNumber = _state.value.countryCode+_state.value.changePhoneNumber
        val password = _state.value.changePhonePassword
        if (newPhoneNumber.isNotEmpty() and password.isNotEmpty()) {
            changePhoneFirstStep(newPhoneNumber, password)
        }
    }

    override fun onClickSave(context: Context) {
        val userName = _state.value.username
        val email = _state.value.email
        if (userName.isNotEmpty() and email.isNotEmpty()) {
            Log.d("userName.isNotEmpty", "userName.isNotEmpty")
            changeNameAndEmail(userName, email)
            if (state.value.imageIsUpdated) {
                Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
            }
        }
        if (_state.value.isUpdated) {
            sendNewEffect(MyProfileUiEffect.OnNavigateBack)

        }
    }

    override fun onClickDeleteAccount() {
        _state.update { it.copy(showDeleteAccountDialog = true) }
    }

    override fun onDismissDeleteDialog() {
        _state.update { it.copy(showDeleteAccountDialog = false) }
    }

    override fun onConfirmDeleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            localConfigurationUseCase.saveUserName("")
            localConfigurationUseCase.clearUserToken("")
            _state.update {
                it.copy(
                    showDeleteAccountDialog = false
                )
            }
        }
        sendNewEffect(MyProfileUiEffect.OnDeleteAccountSuccess)
    }

    override fun onClickNotification() {
        sendNewEffect(MyProfileUiEffect.OnNavigateToNotificationScreen)
    }

    override fun onClickChangeImage() {
        _state.update { it.copy(userImageUrl = "") }
    }
}