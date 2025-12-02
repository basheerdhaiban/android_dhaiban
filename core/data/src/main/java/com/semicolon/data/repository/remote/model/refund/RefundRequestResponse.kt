package com.semicolon.data.repository.remote.model.refund

import kotlinx.serialization.Serializable

@Serializable
data class RefundRequestResponse(
    val success: Boolean
)