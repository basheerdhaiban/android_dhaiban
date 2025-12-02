package com.semicolon.dhaiban.presentation.brandproducts

interface BrandProductsScreenInteractionListener {
    fun onClickBackButton()
    fun onClickProductFavorite(productId: Int, isFavorite: Boolean)
    fun onClickProduct(productId: Int)
    fun onClickNotification()
}