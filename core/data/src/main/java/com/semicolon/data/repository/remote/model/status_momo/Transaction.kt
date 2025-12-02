package com.semicolon.data.repository.remote.model.status_momo

data class Transaction(
    val amount: Double,
    val content: String,
    val currency: String,
    val externalId: String,
    val payeeNote: String,
    val payer: Payer,
    val payerMessage: String,
    val status: String,
    val statusCode: Int
)