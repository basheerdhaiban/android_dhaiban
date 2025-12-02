package com.semicolon.domain.repository

import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.ChoiceItemModel
import com.semicolon.domain.entity.PayTabsModel
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

    suspend fun paytabsInitiate(order_id: Int): PayTabsModel?
    suspend fun paytabsStatus(tran_ref: String?): PayTabsModel?
//    suspend fun paytabsReturn(tran_ref: String?): PayTabsModel?
//    suspend fun paytabsCallBack(tran_ref: String, cart_id: String): PayTabsModel?
}