package com.semicolon.dhaiban.presentation.sharedUiState

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.LayoutDirection
import com.semicolon.domain.entity.AppConfig

@Immutable
data class AppConfigUiState(
    val languages: List<LanguageUiState> = emptyList(),
    val currencies: List<CurrencyUiState> = emptyList(),
    val countries: List<CountryUiState> = emptyList(),
    val defaultLang: DefaultLanguageUiState = DefaultLanguageUiState(),
    val defaultCurrency: String = "",
    var layoutDirection:LayoutDirection = LayoutDirection.Rtl,
)

data class CurrencyUiState(
    val id: Int = 0,
    val name: String = "",
    val symbol: String = "",
    val exchangeRate: Double = 0.0,
    val code: String = "",
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
        )
        return matchingCombinations.any {
            it.startsWith(query, ignoreCase = true) || it.contains(query, ignoreCase = true)
        }
    }
}

data class CountryUiState(
    val id: Int = 0,
    val code: String = "",
    val logo: String? = "",
    val title: String = "",
    val active: Int = 0,
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            title,
        )
        return matchingCombinations.any {
            it.startsWith(query, ignoreCase = true) || it.contains(query, ignoreCase = true)
        }
    }
}

data class LanguageUiState(
    val id: Int = 0,
    val title: String = "",
    val code: String = "",
    val dir: String = "",
    val flag: String,
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            title,
        )
        return matchingCombinations.any {
            it.startsWith(query, ignoreCase = true) || it.contains(query, ignoreCase = true)
        }
    }
}

data class DefaultLanguageUiState(
    val id: Int = 0,
    val url: String = ""
)

fun AppConfig.toUiState(): AppConfigUiState {
    return AppConfigUiState(
        languages = languages.map { it.toLanguageUiState() },
        countries = countries.map { it.toCountryUiState() },
        currencies = currencies.map { it.toCurrencyUiState() },
        defaultLang = DefaultLanguageUiState(defaultLang.id, defaultLang.value),
        defaultCurrency = defaultCurrency.value
    )
}

fun AppConfig.Country.toCountryUiState(): CountryUiState {
    return CountryUiState(
        id = id,
        code = code,
        logo = logo,
        title = title,
        active = active
    )
}

fun AppConfig.Language.toLanguageUiState(): LanguageUiState {
    return LanguageUiState(
        id = id,
        code = code,
        title = title,
        dir = dir,
        flag = flag
    )
}

fun AppConfig.Currency.toCurrencyUiState(): CurrencyUiState {
    return CurrencyUiState(
        id = id,
        code = code,
        name = name,
        symbol = symbol,
        exchangeRate = exchangeRate
    )
}

enum class SliderItemType {
    PRODUCT, CATEGORY
}