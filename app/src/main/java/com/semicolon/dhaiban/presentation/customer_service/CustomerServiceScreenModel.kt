package com.semicolon.dhaiban.presentation.customer_service

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.SocialModel
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.ManageSocialUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class  CustomerServiceScreenModel(
    private val manageSocialUseCases: ManageSocialUseCases,
    private val manageNotificationUseCase: ManageNotificationUseCase,
    private val localConfigurationUseCase: LocalConfigurationUseCase,
) :
    BaseScreenModel<CustomerServiceUiState, CustomerServiceUiEffect>(CustomerServiceUiState()),
    CustomerServiceScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope


    init {
        getCountOfUnreadNotification()
    }

    override fun onClickUpButton() {
        sendNewEffect(CustomerServiceUiEffect.OnNavigateBack)
    }

    override fun onClickNotification() {
        sendNewEffect(CustomerServiceUiEffect.OnNavigateToNotificationScreen)
    }

    override fun onContentChange(text: String) {
        _state.update {
            it.copy(
                content = text
            )
        }
    }

    override fun onSubjectChange(subject: String) {
        _state.update {
            it.copy(
                subject = subject
            )
        }    }

    private fun getCountOfUnreadNotification() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageNotificationUseCase.getUnReadNotificationCount() },
            onSuccess = {
                if (it != null) {
                    _state.update { screenUiState ->
                        screenUiState.copy(
                            isLoading = false,
                            countOfUnreadMessage = it.notifications
                        )
                    }
                }

            },
            onError = {}
        )
    }
     fun contactWithCustomerService() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageSocialUseCases.contactUs(localConfigurationUseCase.getUserName(),state.value.subject,state.value.content,localConfigurationUseCase.getEmail()) },
            onSuccess = {
                _state.update { it.copy(
                    isLoading = false,
                    successSent = true
                    , subject = "",
                    content = ""
                )


                }


            },
            onError = {
                Log.d("contactWithCustomerService",it.message.toString())
                _state.update { it.copy(
                    isLoading = false,)}
            }
        )
    }

    private fun getSocialContact() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageSocialUseCases.getSocialContact() },
            onSuccess = ::onGetSocial,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetSocial(social: SocialModel) {

        _state.update { uiState ->
            uiState.copy(

                isLoading = false,

                )
        }

    }

}