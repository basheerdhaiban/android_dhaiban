package com.semicolon.dhaiban.presentation.favorites

interface FavoritesScreenInteractionListener {
    fun onClickUpButton()
    fun onClickProduct(productId: Int)
    fun onClickFavorite(productId: Int)
    fun onClickNotification()
}