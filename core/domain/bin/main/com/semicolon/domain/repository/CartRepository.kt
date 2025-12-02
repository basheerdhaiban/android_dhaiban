package com.semicolon.domain.repository

import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.ChoiceItemModel
import com.semicolon.domain.entity.productdetails.CartDataModel

interface CartRepository {
    suspend fun getCartProducts(): CartDataModel

    suspend fun addToCart(
        identifier: Long? = null,
        productId: Int? = null,
        quantity: Int,
        variant: String? = null,
        currencyId: Int? = null,
        isUpdated: Boolean? = null,
        colorId:Int? = null,
        variantList: List<ChoiceItemModel>
    ): String

    suspend fun removeFromCart(cartItemId: Int): List<CartItem>
}