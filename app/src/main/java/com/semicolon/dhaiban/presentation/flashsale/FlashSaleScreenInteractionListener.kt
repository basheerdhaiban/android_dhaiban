package com.semicolon.dhaiban.presentation.flashsale

interface FlashSaleScreenInteractionListener {
    fun onClickBackButton()
    fun onClickProductFavorite(productId: Int, isFavorite: Boolean)
    fun onClickProduct(productId: Int)
    fun onClickNotification()
}