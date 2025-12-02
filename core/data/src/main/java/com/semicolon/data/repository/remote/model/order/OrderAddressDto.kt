package com.semicolon.data.repository.remote.model.order

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class OrderAddressDto(
    val address: String?,
    @SerializedName("city_name") val cityName: String?,
    val id: Int?,
    val lat: Double?,
    val lon: Double?,
    val name: String?,
    @SerializedName("postal_code") val postalCode: String?,
    @SerializedName("province_name") val provinceName: String?,
    @SerializedName("region_name") val regionName: String?,
    @SerializedName("work_home") val workHome: String?
)