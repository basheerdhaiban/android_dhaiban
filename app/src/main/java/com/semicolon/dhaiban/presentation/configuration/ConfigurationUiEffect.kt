package com.semicolon.dhaiban.presentation.configuration

import com.semicolon.domain.utils.UserConfig


sealed interface ConfigurationUiEffect {
    data class OnOpenLanguagesBottomSheet(val userConfig: UserConfig) : ConfigurationUiEffect
    data class OnOpenCountriesBottomSheet(val userConfig: UserConfig) : ConfigurationUiEffect
    data class OnOpenCurrenciesBottomSheet(val userConfig: UserConfig) : ConfigurationUiEffect
    data object OnNavigateToOnBoardingScreen : ConfigurationUiEffect
    data class OnUpdateCurrencySymbol(val currencySymbol: String) : ConfigurationUiEffect
    data object ShowNetworkError : ConfigurationUiEffect
    data class OnUpdateCurrency(val id: Int): ConfigurationUiEffect
}