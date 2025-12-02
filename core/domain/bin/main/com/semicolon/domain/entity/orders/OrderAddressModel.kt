package com.semicolon.domain.entity.orders

data class OrderAddressModel(
    val address: String,
    val cityName: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val name: String,
    val postalCode: String,
    val provinceName: String,
    val regionName: String,
    val workHome: String
)
