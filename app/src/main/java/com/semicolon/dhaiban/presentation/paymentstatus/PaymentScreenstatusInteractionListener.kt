package com.semicolon.dhaiban.presentation.paymentstatus

interface PaymentScreenstatusInteractionListener {
    fun onClickUpButton()

    fun onClickBackToHome()
    fun onClickTrackOrder(orderId: Int)
    fun onClickNotification()

}