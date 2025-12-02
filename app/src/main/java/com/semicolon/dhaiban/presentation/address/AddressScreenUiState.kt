package com.semicolon.dhaiban.presentation.address

import android.os.Parcelable
import com.semicolon.domain.entity.AddressModel
import kotlinx.parcelize.Parcelize

data class AddressScreenUiState(
    val isLoading: Boolean = false,
    val addresses: List<AddressUiState> = emptyList()
)

@Parcelize
data class AddressUiState(
    val address: String = "",
    val cityId: Int = 0,
    val cityName: String = "",
    val defaultAddress: AddressState = AddressState.NOT_DEFAULT,
    val id: Int = 0,
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val name: String = "",
    val phone: String = "",
    val postalCode: String = "",
    val provinceId: Int = 0,
    val provinceName: String = "",
    val regionId: Int = 0,
    val regionName: String = "",
    val workHome: String = ""
): Parcelable

fun AddressModel.toAddressUiState() = AddressUiState(
    address,
    cityId,
    cityName,
    if (this.defaultAddress == 0) AddressState.NOT_DEFAULT else AddressState.DEFAULT,
    id,
    lat,
    lon,
    name,
    phone,
    postalCode,
    provinceId,
    provinceName,
    regionId,
    regionName,
    workHome
)

fun AddressUiState.toEditAddressModel() = AddressModel(
    address,
    cityId,
    cityName,
    when (this.defaultAddress) {
        AddressState.DEFAULT -> 0
        AddressState.NOT_DEFAULT -> 1
    },
    id,
    lat,
    lon,
    name,
    phone,
    postalCode,
    provinceId,
    provinceName,
    regionId,
    regionName,
    workHome
)

fun AddressUiState.toAddressModel() = AddressModel(
    address,
    cityId,
    cityName,
    when (this.defaultAddress) {
        AddressState.DEFAULT -> 1
        AddressState.NOT_DEFAULT -> 0
    },
    id,
    lat,
    lon,
    name,
    phone,
    postalCode,
    provinceId,
    provinceName,
    regionId,
    regionName,
    workHome
)

enum class AddressState {
    DEFAULT, NOT_DEFAULT
}