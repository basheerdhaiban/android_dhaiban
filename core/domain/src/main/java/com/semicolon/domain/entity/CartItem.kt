package com.semicolon.domain.entity

data class CartItem(
    val currencyCode: String,
    val currencyId: Int,
    val id: Int,
    val identifier: Long,
    val isUpdated: Int,
    val price: Double,
    val product: CartProduct,
    val quantity: Int,
    val serializedOptions: SerializedOptionsModel,

    val tax: Double,
    val variant: String ,
)
{
    val afterDiscount: Double = if (this.product.discountType == "amount") {
        price - this.product.discount.toDouble()
    } else {
        price - price * this.product.discount.toDouble().toDouble().div(100.0)
    }
}