package com.semicolon.dhaiban.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivityViewModel(
    private val localConfigurationUseCase: LocalConfigurationUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase
) :
    ViewModel() {

    private fun shouldUpdateFcmToken(token: String): Boolean {
        var shouldUpdate = false
        viewModelScope.launch(Dispatchers.IO) {
            val localToken = async { localConfigurationUseCase.getFcmToken() }.await()
            println("local token : $localToken, current token : $token")
            shouldUpdate = (token != localToken)
        }
        return shouldUpdate
    }

    fun updateFcmToken(token: String) {
        if (shouldUpdateFcmToken(token)) {
            println("Token Updated $token")
            viewModelScope.launch(Dispatchers.IO) {
                manageNotificationUseCase.sendFcmToken(token)
            }
        }
    }
}