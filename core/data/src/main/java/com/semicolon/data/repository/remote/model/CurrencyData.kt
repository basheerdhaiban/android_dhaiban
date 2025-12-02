package com.semicolon.data.repository.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyData(
    val default: Default,
    val language: List<Language>
)