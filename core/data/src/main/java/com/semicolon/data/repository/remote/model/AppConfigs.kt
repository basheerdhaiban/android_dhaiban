package com.semicolon.data.repository.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AppConfigDto(
    @SerializedName("languages") val languages: List<LanguageDto>?,
    @SerializedName("currencies") val currencies: List<CurrencyDto>?,
    @SerializedName("country") val countries: List<CountryDto>?,
    @SerializedName("default_lang") val defaultLang: DefaultLanguageDto?,
    @SerializedName("default_currency") val defaultCurrency: DefaultItemDto?
) {

    @Serializable
    data class LanguageDto(
        @SerializedName("id") val id: Int?,
        @SerializedName("title") val title: String?,
        @SerializedName("code") val code: String?,
        @SerializedName("dir") val dir: String?,
        @SerializedName("flag") val flag: String?
    )

    @Serializable
    data class CurrencyDto(
        @SerializedName("id") val id: Int?,
        @SerializedName("name") val name: String?,
        @SerializedName("symbol") val symbol: String?,
        @SerializedName("exchange_rate") val exchangeRate: Double?,
        @SerializedName("code") val code: String?
    )

    @Serializable
    data class CountryDto(
        @SerializedName("id") val id: Int?,
        @SerializedName("code") val code: String?,
        @SerializedName("logo") val logo: String?,
        @SerializedName("title") val title: String?,
        @SerializedName("active") val active: Int?
    )

    @Serializable
    data class DefaultLanguageDto(
        @SerializedName("path") val value: String?,
        @SerializedName("0") val id: String?
    )

    @Serializable
    data class DefaultItemDto(
        @SerializedName("value") val value: String?
    )
}
