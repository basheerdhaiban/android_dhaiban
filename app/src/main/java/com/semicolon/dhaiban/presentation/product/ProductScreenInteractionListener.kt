package com.semicolon.dhaiban.presentation.product

interface ProductScreenInteractionListener {
    fun onClickSmallImage(imageId: Int)

    fun onClickUpButton()

    fun onClickColor(colorId: Int)

    fun onClickRecommendedProduct(productId: Int)

    fun onClickOption(parentChoiceId: Int, optionId: Int)

    fun onClickImage(visibility: Boolean)
    fun onClickProductFavorite(productId: Int, isFavorite: Boolean)
    fun onCLickAddToCart(productId: Int, quantity: Int)
    fun showMessage(message: String)
    fun onDismissAlert()
    fun onClickLogin()
    fun onClickCartIcon()
    fun onCloseImageViewer()
    fun onDismissCartDialog()
}