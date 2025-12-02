package com.semicolon.data.repository.remote.model.payment

data class PaymentResponse(
    val transaction: Transaction?,
    val transactionId: String?
)