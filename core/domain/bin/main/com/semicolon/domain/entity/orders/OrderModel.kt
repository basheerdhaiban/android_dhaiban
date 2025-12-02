package com.semicolon.domain.entity.orders

data class OrderModel(
    val orderProducts: List<OrderProductModel>,
    val address: OrderAddressModel,
    val code: String,
    val commercialRegister: String, //Not sure
    val couponDiscount: Int,
    val currency: String,
    val date: String,
    val deliveryStatus: String,
    val id: Int,
    val invoiceLink: String, //Not sure
    val isRated: Int,
    val orderType: String,
    val paymentStatus: Int,
    val paymentType: String,
    val shippingCost: Int,
    val shippingTax: Int,
    val tax: Double,
    val totalPrice: Double,
    val user: String,
    val viewed: Int,
    val walletDiscount: Double
)
