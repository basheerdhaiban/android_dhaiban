package com.semicolon.data.repository.remote.model.wallet

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class BalanceDto(
    @SerializedName("user_balance") val balance: Double?
)
