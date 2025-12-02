package com.semicolon.data.repository.remote.model.cart

import com.semicolon.data.repository.remote.model.address.AddressDto

data class CartDataDto(
    val address: AddressDto?,
    val data: List<CartDataItem>
)