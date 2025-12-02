package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.address.AddressDto
import com.semicolon.data.repository.remote.model.cart.CartDataItem
import com.semicolon.data.repository.remote.model.cart.CartProductDto
import com.semicolon.data.repository.remote.model.cart.SerializedOptions
import com.semicolon.domain.entity.CartChoice
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.CartProduct
import com.semicolon.domain.entity.SerializedOptionsModel
import com.semicolon.domain.entity.productdetails.DefaultAddress

fun CartProductDto.toCartProduct() = CartProduct(
    advancedProduct = this.advancedProduct ?: "",
    currentStock = this.currentStock ?: 0,
    discount = this.discount ?: 0.0,
    discountType = this.discountType ?: "",
    id = this.id ?: 0,
    owner = this.owner ?: "",
    photo = this.photo ?: "",
    sellerId = this.sellerId ?: 0,
    shortDescription = this.shortDesc ?: "",
    title = this.title ?: ""
)

fun SerializedOptions.toSerializedOptionsModel() = SerializedOptionsModel(
    color = this.color ?: 0,

    choices = this.choices.map {
        CartChoice(
            it.id ?: 0,
            it.parentId ?: 0,
            it.title ?: "",
            it.parentTitle ?: ""
        )

    },
    colorCode = this.color_code?:"",
    colorTitle = this.color_title?:""
)

fun CartDataItem.toCartItem() = CartItem(
    currencyCode = this.currencyCode ?: "",
    currencyId = this.currencyId ?: 0,
    id = this.id ?: 0,
    identifier = this.identifier ?: 0,
    isUpdated = this.isUpdated ?: 0,
    price = this.price ?: 0.0,
    product = this.product?.toCartProduct()!!,
    quantity = this.quantity ?: 0,
    serializedOptions = this.serializedOptions?.toSerializedOptionsModel()
        ?: SerializedOptionsModel(
            emptyList(), 0 ,"",""
        ),
    tax = this.tax ?: 0.0,
    variant = this.variant ?: "",


)

fun AddressDto.toDefaultAddress() = DefaultAddress(
    id = this.id?: 0,
    address = this.address?:"",
    shippingCost?:0.0, shippingTax?:0.0
)