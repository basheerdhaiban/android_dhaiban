package com.semicolon.domain.entity.productdetails

import com.semicolon.domain.entity.CartItem

data class CartDataModel(
    val cartProducts: List<CartItem>,
    val defaultAddress: DefaultAddress
)
