package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.AppConfigDto
import com.semicolon.domain.entity.AppConfig

fun AppConfigDto.LanguageDto.toEntity(): AppConfig.Language {
    return AppConfig.Language(
        id = id ?: 0,
        title = title ?: "",
        code = code ?: "",
        dir = dir ?: "",
        flag = flag ?: ""
    )
}

fun AppConfigDto.CurrencyDto.toEntity(): AppConfig.Currency {
    return AppConfig.Currency(
        id = id ?: 0,
        name = name ?: "",
        symbol = symbol ?: "",
        exchangeRate = exchangeRate ?: 0.0,
        code = code ?: ""
    )
}

fun AppConfigDto.CountryDto.toEntity(): AppConfig.Country {
    return AppConfig.Country(
        id = id ?: 0,
        code = code ?: "",
        logo = logo ?: "",
        title = title ?: "",
        active = active ?: 0
    )
}

fun AppConfigDto.DefaultItemDto.toDefaultItem(): AppConfig.DefaultItem {
    return AppConfig.DefaultItem(
        value = value ?: ""
    )
}


fun AppConfigDto.DefaultLanguageDto.toDefaultLanguage(): AppConfig.DefaultLanguage {
    return AppConfig.DefaultLanguage(
        id = this.id?.toInt() ?: 3,
        value = value ?: ""
    )
}
fun List<AppConfigDto.CountryDto>.toEntity(): List<AppConfig.Country> = map { it.toEntity() }

fun AppConfigDto.toEntity(): AppConfig {
    return AppConfig(
        languages = languages?.map { it.toEntity() } ?: emptyList(),
        currencies = currencies?.map { it.toEntity() } ?: emptyList(),
        countries = countries?.toEntity() ?: emptyList(),
        defaultLang = defaultLang?.toDefaultLanguage() ?: AppConfig.DefaultLanguage(id = 3, value = ""),
        defaultCurrency = defaultCurrency?.toDefaultItem() ?: AppConfig.DefaultItem(value = "")
    )
}

