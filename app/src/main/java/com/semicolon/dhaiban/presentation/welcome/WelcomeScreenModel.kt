package com.semicolon.dhaiban.presentation.welcome

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import kotlinx.coroutines.CoroutineScope

class WelcomeScreenModel(private val localPref: LocalConfigurationUseCase) : BaseScreenModel<WelcomeUiState, WelcomeUiEffect>(WelcomeUiState()),
    WelcomeScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    override fun onClickLogin() {
        sendNewEffect(WelcomeUiEffect.OnNavigateToLoginScreen)
    }

    override fun onClickSignUp() {
        sendNewEffect(WelcomeUiEffect.OnNavigateToSignUpScreen)
    }

    override suspend fun onClickSkip() {
        localPref.saveUserToken("")
        sendNewEffect(WelcomeUiEffect.OnNavigateToHomeScreenScreen)
    }
}