package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.CurrencyData
import com.semicolon.domain.entity.DefaultCurrency

fun CurrencyData.toDefaultCurrency(): DefaultCurrency {
    val defaultCurrency = this.language.find { it.id.toString() == this.default.scalar }
    defaultCurrency?.let {
        return DefaultCurrency(
            code = it.code,
            exchangeRate = it.exchangeRate,
            id = it.id,
            name = it.name,
            symbol = it.symbol
        )
    } ?: return DefaultCurrency("جنيه مصري", exchangeRate = 1.0, id = 0, name = "Egypt", symbol = "ج.م")
}
