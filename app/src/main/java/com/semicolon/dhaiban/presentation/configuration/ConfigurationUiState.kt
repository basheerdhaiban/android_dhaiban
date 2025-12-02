package com.semicolon.dhaiban.presentation.configuration

import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.domain.utils.UserConfig

data class ConfigurationUiState(
    val isLoading: Boolean = false,
    val appConfig: AppConfigUiState = AppConfigUiState(),
    val recreateState : Boolean = true,
    val onBoardingState: Boolean = false,
    val showBottomSheet: Boolean = false,
    val selectedLanguage: String = "",
    val selectedCountry: String = "",
    val selectedCurrency: String = "",
    val queryValue: String = "",
    val userConfig: UserConfig = UserConfig.LANGUAGE,
    val selectedCurrencyId: Int = 0,

    ) {
    val defaultLangCode =
        appConfig.languages.find { it.id.toString() == appConfig.defaultLang.id.toString() }?.code ?: ""
    val defaultCurrencySymbol =
        appConfig.currencies.find { it.id.toString() == appConfig.defaultCurrency }?.symbol ?: ""

    val layoutDirection =
        appConfig.languages.find { it.id.toString() == appConfig.defaultLang.id.toString() }?.dir ?: ""
}
