package com.semicolon.data.repository.remote.model.payment

data class Transaction(
    val amount: Double?,
    val content: String?,
    val currency: String?,
    val externalId: String?,
    val financialTransactionId: String?,
    val payeeNote: String?,
    val payer: Payer?,
    val payerMessage: String?,
    val status: String?,
    val statusCode: Int?
)