package com.semicolon.dhaiban.presentation.faq

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.FaqModel
import com.semicolon.domain.usecase.ManageFaqUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class FaqScreenModel(
    private val manageFaqUseCase: ManageFaqUseCase
) : BaseScreenModel<FaqScreenUiState, FaqScreenUiEffect>(FaqScreenUiState()),
    FaqScreenListenerInteraction {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getFaqTypes()
    }

    private fun getFaqTypes() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = {
                manageFaqUseCase.getFaqTypes()
            },
            onSuccess = ::onGetFaqSuccess,
            onError = {
                _state.update { it.copy(isLoading = false) }
            }
        )
    }

    private fun onGetFaqSuccess(faqs: List<FaqModel>) {
        _state.update { uiState ->
            uiState.copy(
                faqTypes = faqs.map { it.toFaqTypeUsState() },
                isLoading = false
            )
        }
    }

    override fun onClickUpButton() {
        sendNewEffect(FaqScreenUiEffect.OnNavigateBack)
    }

    override fun onFaqClick(id: Int) {
        sendNewEffect(FaqScreenUiEffect.OnNavigateToFaqDetails(id))
    }

    override fun onClickNotification() {
        sendNewEffect(FaqScreenUiEffect.OnNavigateToNotificationScreen)
    }
}