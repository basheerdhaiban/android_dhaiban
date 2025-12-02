package com.semicolon.dhaiban.presentation.cart

import com.semicolon.dhaiban.presentation.base.BaseInteractionListener

interface CartScreenInteractionListener : BaseInteractionListener {
   suspend fun onClickDelete(cartItemId: Int)

    fun onCLickPlus(
        identifier: Long,
        serializedOptionsUiState: SerializedOptionsUiState,
        variant: String,
        productId: Int,
        quantity: Int,
        availableStockState: Boolean
    )

    fun onCLickMinus(
        identifier: Long,
        serializedOptionsUiState: SerializedOptionsUiState,
        variant: String,
        productId: Int,
        quantity: Int
    )

    fun onClickAddress()

    fun onClickContinueToPayment()

    fun onClickAddMore()

    fun onClickCartItem(productId: Int, variant: String)
    fun onClickNotification()
}