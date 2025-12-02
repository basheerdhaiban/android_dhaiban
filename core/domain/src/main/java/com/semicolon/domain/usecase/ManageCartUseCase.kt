package com.semicolon.domain.usecase

import com.semicolon.domain.entity.ChoiceItemModel
import com.semicolon.domain.repository.CartRepository

class ManageCartUseCase(private val cartRepository: CartRepository) {
    suspend fun getCartProducts() = cartRepository.getCartProducts()

    suspend fun addToCart(
        identifier: Long? = null,
        productId: Int? = null,
        quantity: Int,
        variant: String? = null,
        currencyId: Int? = null,
        isUpdated: Boolean? = null,
        colorId: Int? = null,
        variantList: List<ChoiceItemModel>
    ) =
        cartRepository.addToCart(
            identifier,
            productId,
            quantity,
            variant,
            currencyId,
            isUpdated,
            colorId,
            variantList
        )
    suspend fun removeFromCart(cartItemId: Int) = cartRepository.removeFromCart(cartItemId)

    suspend fun paytabsInitiate(order_id: Int) = cartRepository.paytabsInitiate(order_id)
    suspend fun paytabsCheckStatus(tran_ref: String?) = cartRepository.paytabsStatus(tran_ref)
//    suspend fun paytabsReturn(tran_ref: String?) = cartRepository.paytabsReturn(tran_ref)
//    suspend fun paytabsCallBack(tran_ref: String, cart_id: String) = cartRepository.paytabsCallBack(tran_ref, cart_id)
}