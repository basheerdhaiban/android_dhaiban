package com.semicolon.data.repository.remote.model.refund.trackrefund

data class Address(
    val address: String,
    val city_name: String,
    val id: Int,
    val lat: Double,
    val lon: Double,
    val name: String,
    val postal_code: String,
    val province_name: String,
    val region_name: String,
    val work_home: String
)