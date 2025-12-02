package com.semicolon.dhaiban.presentation.addaddress

import com.google.android.gms.maps.model.LatLng
import com.semicolon.dhaiban.presentation.address.AddressUiState
import com.semicolon.domain.entity.AddressModel
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.entity.City
import com.semicolon.domain.entity.Province
import com.semicolon.domain.entity.Region

data class AddAddressScreenUiState(
    val isLoading: Boolean = false,
    val addressName: String = "",
    val detailedAddress: String = "",
    var dropStateForBottomSheetCountry: Boolean = false,
    val dropStateForBottomSheetCity: Boolean = false,
    val dropStateForBottomSheetGoverment: Boolean = false,
    val dropStateForBottomSheetRegion: Boolean = false,

    var countryCode: String = "",
    var country: String = "",
    val government: String = "",
    val city: String = "",
    val region: String = "",
    val postalCode: String = "",
    val contactNumber: String = "",
    val addressType: AddressType = AddressType.HOME,
    val defaultAddress: Boolean = false,
    val countries: List<MenuItemUiState> = emptyList(),
    val governments: List<MenuItemUiState> = emptyList(),
    val cities: List<MenuItemUiState> = emptyList(),
    val regions: List<MenuItemUiState> = emptyList(),
    val selectedCountry: Int = 0,
    val selectedGovernment: Int = 0,
    val selectedCity: Int = 0,
    val selectedRegion: Int = 0,
    val clearCityRegion: Boolean = false,
    val clearGovCityRegion: Boolean = false,
    val clearCity: Boolean = false,
    val clearRegion: Boolean = false,
    val regionsLoading: Boolean = false,
    val citesLoading: Boolean = false,
    val governmentsLoading: Boolean = false,
    val countriesLoading: Boolean = false,
    val latLng: LatLng = LatLng(0.0, 0.0),
    val errorMessage: String = "",
    val addressUiState: AddressUiState = AddressUiState(),
    val setInitialAddressData: Boolean = true
) {
    val editAddress: Boolean = addressUiState.id != 0
    val errorState: ErrorState = ErrorState(
        cantAddAddress = addressName.isEmpty() || detailedAddress.isEmpty() || (selectedCountry == 0)
                || (selectedGovernment == 0) || (selectedCity == 0) || (selectedRegion == 0)|| (selectedRegion == 0)
                ||  if (editAddress) addressUiState.postalCode.isEmpty() else postalCode.isEmpty() || contactNumber.isEmpty()
//                ||
//                if (editAddress) addressUiState.phone.length in 9..14  else contactNumber.length in 9..14
                || (addressType == AddressType.NONE),
        addressRequired = addressName.isEmpty(),
        detailedAddressRequired = detailedAddress.isEmpty(),
        countryRequired = selectedCountry == 0,
        governmentRequired = selectedGovernment == 0,
        cityRequired = selectedCity == 0,
        regionRequired = selectedRegion == 0,
        postalCodeRequired = if (editAddress) addressUiState.postalCode.isEmpty() else postalCode.isEmpty(),
        contactNumberRequired = contactNumber.isEmpty(),
        isValidPhoneNumber = if (editAddress) addressUiState.phone.length in 9..14 else contactNumber.length in 9..14,
    )
}

data class ErrorState(
    val cantAddAddress: Boolean = false,
    val addressRequired: Boolean = false,
    val detailedAddressRequired: Boolean = false,
    val countryRequired: Boolean = false,
    val governmentRequired: Boolean = false,
    val cityRequired: Boolean = false,
    val regionRequired: Boolean = false,
    val postalCodeRequired: Boolean = false,
    val contactNumberRequired: Boolean = false,
    val isValidPhoneNumber: Boolean = false


)

data class MenuItemUiState(
    val name: String = "",
    val id: Int = 0,
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            name,
        )
        return matchingCombinations.any {
            it.startsWith(query, ignoreCase = true) || it.contains(query, ignoreCase = true)
        }
    }
}

fun AppConfig.Country.toMenuItemUiState() = MenuItemUiState(
    id = id,
    name = title
)

fun Province.toMenuItemUiState() = MenuItemUiState(
    id = id,
    name = title
)

fun City.toMenuItemUiState() = MenuItemUiState(
    id = id,
    name = title
)

fun Region.toMenuItemUiState() = MenuItemUiState(
    id = id,
    name = title
)

fun AddAddressScreenUiState.toAddressModel() = AddressModel(
    address = this.detailedAddress,
    cityId = this.selectedCity,
    cityName = this.city,
    defaultAddress = if (this.defaultAddress) 1 else 0,
    id = 0,
    lat = this.latLng.latitude,
    lon = this.latLng.longitude,
    name = this.addressName,
    phone = this.countryCode+this.contactNumber,
    postalCode = this.postalCode,
    provinceId = this.selectedGovernment,
    provinceName = this.government,
    regionId = this.selectedRegion,
    regionName = this.region,
    workHome = if (this.addressType == AddressType.HOME) "home" else "work"
)

enum class AddressType {
    HOME, WORK, NONE
}
