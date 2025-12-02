package com.semicolon.dhaiban.presentation.onBoarding

import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OnBoardingScreenModel(
    private val localConfigurationUseCase: LocalConfigurationUseCase
) : BaseScreenModel<OnBoardingUiState, OnBoardingUiEffect>(OnBoardingUiState()),
    OnBoardingInteractionListener {

    override val viewModelScope: CoroutineScope = screenModelScope

    private fun saveOnBoardingState() {
        viewModelScope.launch(Dispatchers.IO) {
            localConfigurationUseCase.saveOnBoardingState(isCompleted = true)
        }
    }

    override fun onClickNextButton() {
        saveOnBoardingState()
        sendNewEffect(OnBoardingUiEffect.OnNavigateToWelcomeScreen)
    }

}