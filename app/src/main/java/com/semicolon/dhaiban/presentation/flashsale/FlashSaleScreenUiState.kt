package com.semicolon.dhaiban.presentation.flashsale

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.domain.entity.Product

data class FlashSaleScreenUiState (
    val isLoading: Boolean = false,
    val products: List<SalesUiState> = emptyList(),
    val currency: SaleCurrencyUiState = SaleCurrencyUiState(),
    val errorMessage: String = ""
)


data class SalesUiState(
    val id: Int = 0,
    val imageUrl: String = "",
    val isFavourite: Boolean = false,
    val title: String = "",
    val description: String,
    val price: Double = 0.0,
    val afterDiscount: Double = 0.0,
)

@Immutable
data class SaleCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)


fun Product.toSalesUiState() =
    SalesUiState(
        id = this.id,
        imageUrl = this.photo,
        isFavourite = userFavorite,
        title = this.title,
        description = shortDescription,
        price = unitPrice,
        afterDiscount = this.afterDiscount
    )

fun AppCurrencyUiState.toSaleCurrencyUiState() =
    SaleCurrencyUiState(code, exchangeRate, id, name, symbol)