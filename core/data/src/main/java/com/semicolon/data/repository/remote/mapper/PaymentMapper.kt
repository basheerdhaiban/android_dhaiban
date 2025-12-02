package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.order.MakeOrderResponseData
import com.semicolon.data.repository.remote.model.payment.PaymentResponse
import com.semicolon.data.repository.remote.model.payment.Transaction
import com.semicolon.domain.entity.PaymentModel
import com.semicolon.domain.entity.TransactionModel
import com.semicolon.domain.entity.orders.OrderResponseModel

fun PaymentResponse.toPaymentModel() =
    transaction?.let { PaymentModel(transactionId = transactionId?:"", transaction = it.toTransactionModel()) }


fun Transaction.toTransactionModel() =
    TransactionModel(
        amount,
        content,
        currency,
        externalId,
        financialTransactionId,
        payeeNote,
        payerMessage,
        status,
        statusCode,
        payer?.partyId,
        payer?.partyIdType
    )
fun com.semicolon.data.repository.remote.model.status_momo.Transaction.toTransactionModel() =
    TransactionModel(
        amount = amount,
       content =  content,
      currency =   currency,
        externalId = externalId,
        payeeNote = payeeNote,
        payerMessage = payerMessage,
        status = status,
        statusCode = statusCode,

    )