package com.semicolon.dhaiban.presentation.app

import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.domain.entity.DefaultCurrency

data class AppUiState(
    val isLoading: Boolean = false,
    val isOnboardingCompleted: Boolean? = null,
    val isActive: Boolean? = false,
    val language: String = "",
    val layoutDirection: String = "",
    val stringRes: StringResources = StringResources(),
    val secondStringRes: SecondStringResources = SecondStringResources(),

    val appConfig: AppConfigUiState = AppConfigUiState(),
    val showSnackBar: Boolean = false,
    val userChangedFavoriteFromFavoriteScreen : Boolean = false,
    val currentScreen : String = "",
    val currency: AppCurrencyUiState = AppCurrencyUiState(),
    val userToken: String = "",
    val cartItemsNumber: Int = 0,
) {
//    val defaultLangCode = appConfig.languages.find { it.id.toString() == appConfig.defaultLang }?.code ?: ""
//    val defaultLayoutDirection = appConfig.languages.find { it.id.toString() == appConfig.defaultLang }?.dir ?: ""

}

data class AppCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String ="",
    val symbol: String = ""
)

fun DefaultCurrency.toCurrencyUiState() =
    AppCurrencyUiState(
        code, exchangeRate, id, name, symbol
    )

fun com.semicolon.dhaiban.presentation.sharedUiState.CurrencyUiState.toCurrencyUiState() =
    AppCurrencyUiState(
        code, exchangeRate, id, name, symbol
    )
