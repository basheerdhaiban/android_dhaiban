package com.semicolon.data.repository.remote.model.address

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class AddressDto(
    @SerializedName("address") val address: String?,
    @SerializedName("city_id") val cityId: Int?,
    @SerializedName("city_name") val cityName: String?,
    @SerializedName("default_adress") val defaultAddress: Int?,
    @SerializedName("id") val id: Int?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lon") val lon: Double?,
    @SerializedName("name") val name: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("postal_code") val postalCode: String?,
    @SerializedName("province_id") val provinceId: Int?,
    @SerializedName("province_name") val provinceName: String?,
    @SerializedName("region_id") val regionId: Int?,
    @SerializedName("region_name") val regionName: String?,
    @SerializedName("work_home") val workHome: String?,
    @SerializedName("shipping_cost") val shippingCost: Double?,
    @SerializedName("shipping_tax") val shippingTax: Double?

)