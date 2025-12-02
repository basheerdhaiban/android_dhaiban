package com.semicolon.dhaiban.presentation.onBoarding

sealed interface OnBoardingUiEffect {

    data object OnNavigateToWelcomeScreen : OnBoardingUiEffect

}