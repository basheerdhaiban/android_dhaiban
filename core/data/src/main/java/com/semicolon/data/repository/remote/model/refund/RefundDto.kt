package com.semicolon.data.repository.remote.model.refund

import com.google.gson.annotations.SerializedName
import com.semicolon.data.repository.remote.model.order.OrderAddressDto
import kotlinx.serialization.Serializable

@Serializable
data class RefundDto(
    @SerializedName("Product") val product: RefundProduct?,
    val address: OrderAddressDto?,
    @SerializedName("admin_resone") val adminReason: String?,
    val code: String?,
    @SerializedName("created_at") val createdAt: String?,
    @SerializedName("customer_reson") val customerReason: String?,
    @SerializedName("edit_address") val editAddress: Boolean?,
    val id: Int?,
    @SerializedName("order_product_id") val orderProductId: Int?,
    @SerializedName("payment_type") val paymentType: String?,
    @SerializedName("refund_amount") val refundAmount: Double?,
    val status: String?,
    val user: User?,
    val viewed: Int?
)