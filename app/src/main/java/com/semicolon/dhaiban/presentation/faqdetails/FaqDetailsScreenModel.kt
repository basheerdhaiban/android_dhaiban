package com.semicolon.dhaiban.presentation.faqdetails

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.FaqItemModel
import com.semicolon.domain.usecase.ManageFaqUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class FaqDetailsScreenModel(
    faqId: Int,
    private val manageFaqUseCase: ManageFaqUseCase
) :
    BaseScreenModel<FaqDetailsUiState, FaqDetailsUiEffect>(FaqDetailsUiState()),
    FaqDetailsInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getFaqDetails(faqId)
    }

    private fun getFaqDetails(faqId: Int) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageFaqUseCase.getFaqItems(faqId) },
            onSuccess = ::onGetFaqDetailsSuccess,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetFaqDetailsSuccess(faqDetails: List<FaqItemModel>) {
        _state.update { uiState ->
            uiState.copy(
                faqDetails = faqDetails.map { it.toFaqDetailsItemUiState() },
                isLoading = false
            )
        }
    }

    override fun onClickUpButton() {
        sendNewEffect(FaqDetailsUiEffect.OnNavigateBack)
    }

    override fun onClickNotification() {
        sendNewEffect(FaqDetailsUiEffect.OnNavigateToNotificationScreen)
    }
}