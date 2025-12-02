package com.semicolon.domain.entity

data class WalletHistoryModel(
    val amount: Double,
    val approval: Int,
    val createdAt: String,
    val id: Int,
    val paymentDetails: String,
    val paymentMethod: String
)
