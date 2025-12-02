package com.semicolon.domain.entity

data class AddressModel(
    val address: String,
    val cityId: Int,
    val cityName: String,
    val defaultAddress: Int,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val name: String,
    val phone: String,
    val postalCode: String,
    val provinceId: Int,
    val provinceName: String,
    val regionId: Int,
    val regionName: String,
    val workHome: String
)
