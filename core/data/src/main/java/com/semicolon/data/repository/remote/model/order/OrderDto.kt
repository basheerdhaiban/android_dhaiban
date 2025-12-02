package com.semicolon.data.repository.remote.model.order

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OrderDto(
    @SerializedName("OrderProduct") val orderProduct: List<OrderProductDto>?,
    val address: OrderAddressDto?,
    val code: String?,
    @SerializedName("commercial_register") val commercialRegister: String?, //Not sure
    @SerializedName("coupon_discount") val couponDiscount: Int?,
    val currency: String?,
    val date: String?,
    @SerializedName("delivery_status") val deliveryStatus: String?,
    val id: Int?,
    @SerializedName("invoice_link") val invoiceLink: String?, //Not sure
    @SerializedName("is_rated") val isRated: Int?,
    @SerializedName("order_type") val orderType: String?,
    @SerializedName("payment_status") val paymentStatus: Int?,
    @SerializedName("payment_type") val paymentType: String?,
    @SerializedName("shipping_cost") val shippingCost: Int?,
    @SerializedName("shipping_tax") val shippingTax: Int?,
    val tax: Double?,
    @SerializedName("total_price") val totalPrice: Double?,
    val user: User?,
    val viewed: Int?,
    @SerializedName("wallet_discount") val walletDiscount: Double?
)