package com.semicolon.dhaiban.presentation.refund

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.RefundModel
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.ManageRefundUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class RefundScreenModel(
    private val manageRefundUseCase: ManageRefundUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase
) :
    BaseScreenModel<RefundScreenUiState, RefundScreenUiEffect>(RefundScreenUiState()),
    RefundScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getCurrentRefunds()
        getPreviousRefunds()
    }

    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toRefundCurrencyUiState()) }
    }

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

    private fun getCurrentRefunds() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageRefundUseCase.getCurrentRefunds() },
            onSuccess = ::onGetCurrentRefundsSuccess,
            onError = {
                _state.update { it.copy(isLoading = false) }
            }
        )
    }

    private fun onGetCurrentRefundsSuccess(currentRefunds: List<RefundModel>) {
        _state.update { uiState ->
            uiState.copy(
                isLoading = false,
                currentRefundItems = currentRefunds.reversed().map { it.toRefundItemUiState() })
        }
    }

    private fun getPreviousRefunds() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageRefundUseCase.getPreviousRefunds() },
            onSuccess = ::onGetPreviousRefundsSuccess,
            onError = {
                _state.update { it.copy(isLoading = false) }
            }
        )
    }

    private fun onGetPreviousRefundsSuccess(previousRefunds: List<RefundModel>) {
        _state.update { uiState ->
            uiState.copy(
                isLoading = false,
                previousRefundItems = previousRefunds.reversed().map { it.toRefundItemUiState() }
            )
        }
    }

    override fun onClickUpButton() {
        sendNewEffect(RefundScreenUiEffect.OnNavigateBack)
    }

    override fun onClickRequestType(requestType: RequestType) {
        _state.update { it.copy(requestType = requestType) }
    }

    override fun onClickTrackOrder(refundItem: RefundItemUiState, requestType: RequestType) {
        sendNewEffect(RefundScreenUiEffect.OnNavigateToTrackRefund(refundItem, requestType))
    }

    override fun onClickNotification() {
        sendNewEffect(RefundScreenUiEffect.OnNavigateToNotificationScreen)
    }
}