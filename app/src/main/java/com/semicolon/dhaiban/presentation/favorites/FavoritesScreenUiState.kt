package com.semicolon.dhaiban.presentation.favorites

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.domain.entity.FavoriteProduct

data class FavoritesScreenUiState(
    val isLoading: Boolean = false,
    val products: List<FavoriteProductUiState> = emptyList(),
    val userData: UserDataUiState = UserDataUiState(),
    val currencySymbol: String = "",
    val currency: FavoriteCurrencyUiState = FavoriteCurrencyUiState(),
    val countOfUnreadMessage :Int =0
)

data class FavoriteProductUiState(
    val id: Int,
    val imageUrl: String,
    val productTitle: String,
    val unitPrice: Double,
    val afterDiscount: Double
)

@Immutable
data class UserDataUiState(
    val username: String = "",
    val isAuthenticated: Boolean = false
)

@Immutable
data class FavoriteCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)


fun FavoriteProduct.toFavoriteProductUiState() =
    FavoriteProductUiState(
        id = this.id,
        imageUrl = photo,
        productTitle = title,
        unitPrice = unitPrice,
        afterDiscount = afterDiscount
    )

fun AppCurrencyUiState.toFavoriteCurrencyUiState() =
    FavoriteCurrencyUiState(code, exchangeRate, id, name, symbol)
