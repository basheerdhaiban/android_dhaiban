package com.semicolon.data.repository.remote.model.wallet

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class WalletHistoryDto(
    val amount: Double?,
    val approval: Int?,
    @SerializedName("created_at") val createdAt: String?,
    val id: Int?,
    @SerializedName("payment_details") val paymentDetails: String?,
    @SerializedName("payment_method") val paymentMethod: String?
)