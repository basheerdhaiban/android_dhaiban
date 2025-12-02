package com.semicolon.domain.entity

data class DefaultCurrency(
    val code: String,
    val exchangeRate: Double,
    val id: Int,
    val name: String,
    val symbol: String
)
