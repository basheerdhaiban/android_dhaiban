package com.semicolon.dhaiban.presentation.refund

interface RefundScreenInteractionListener {
    fun onClickUpButton()
    fun onClickRequestType(requestType: RequestType)
    fun onClickTrackOrder(refundItem: RefundItemUiState, requestType: RequestType)
    fun onClickNotification()
}