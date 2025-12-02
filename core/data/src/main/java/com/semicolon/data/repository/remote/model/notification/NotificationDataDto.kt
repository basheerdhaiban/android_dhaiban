package com.semicolon.data.repository.remote.model.notification

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDataDto(
    @SerializedName("delivery_status") val deliveryStatus: String? = null,
    @SerializedName("refund_status") val refundStatus: String? = null,
    val message: String?,
    @SerializedName("order_id") val orderId: Int?,
    @SerializedName("inbox_id") val inboxId: Int?,
    @SerializedName("product_id") val productId: Int?,

    @SerializedName("refund_id") var refundId: Int? = null,

    @SerializedName("message_from") var messageFrom: String? = null,

    val title: String?
)