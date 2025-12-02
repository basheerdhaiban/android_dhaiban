package com.semicolon.domain.entity


data class AppConfig(
    val languages: List<Language>,
    val currencies: List<Currency>,
    val countries: List<Country>,
    val defaultLang: DefaultLanguage,
    val defaultCurrency: DefaultItem
) {
    data class Currency(
        val id: Int,
        val name: String,
        val symbol: String,
        val exchangeRate: Double,
        val code: String
    )

    data class Country(
        val id: Int,
        val code: String,
        val logo: String?,
        val title: String,
        val active: Int
    )

    data class Language(
        val id: Int,
        val title: String,
        val code: String,
        val dir: String,
        val flag: String
    )

    data class DefaultLanguage(
        val id: Int,
        val value: String
    )
    data class DefaultItem(
        val value: String
    )
}