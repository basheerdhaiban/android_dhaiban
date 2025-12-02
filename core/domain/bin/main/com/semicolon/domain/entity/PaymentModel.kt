package com.semicolon.domain.entity

data class PaymentModel(    val transaction: TransactionModel,
                            val transactionId: String)
data class TransactionModel(
    val amount : Double ? =null,
    val content : String ? =null,
    val currency : String ? =null,
    val externalId : String ? =null,
    val financialTransactionId : String ? =null,
    val payeeNote : String ? =null,
    val payerMessage : String ? =null,
    val status : String ? =null,
    val statusCode: Int ?=null,
    val partyId : String ? =null,
    val partyIdType : String ? =null,
)
