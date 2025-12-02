package com.semicolon.dhaiban.presentation.wallet

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.BalanceModel
import com.semicolon.domain.entity.WalletHistoryModel
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.ManageWalletUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class WalletScreenModel(
    private val manageWalletUseCase: ManageWalletUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase
) :
    BaseScreenModel<WalletScreenUiState, WalletScreenUiEffect>(
        WalletScreenUiState()
    ), WalletScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
init {
    getCountOfUnreadNotification()
}
    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toWalletCurrencyUiState()) }
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
            onError = {}
        )
    }

    fun getCurrentBalance() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageWalletUseCase.getCurrentBalance() },
            onSuccess = ::onGetCurrentBalanceSuccess,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetCurrentBalanceSuccess(balance: BalanceModel) {
        _state.update { it.copy(balance = balance.balance, isLoading = false) }
    }

    fun getWalletHistory() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageWalletUseCase.getWalletHistory() },
            onSuccess = ::onGetWalletHistorySuccess,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetWalletHistorySuccess(walletHistory: List<WalletHistoryModel>) {
        _state.update { uiState ->
            uiState.copy(
                walletHistory = walletHistory.map { it.toWalletHistoryUiState() },
                isLoading = false
            )
        }
    }

    override fun onClickUpButton() {
        sendNewEffect(WalletScreenUiEffect.OnNavigateBack)
    }

    override fun onClickNotification() {
        sendNewEffect(WalletScreenUiEffect.OnNavigateToNotificationScreen)
    }
}