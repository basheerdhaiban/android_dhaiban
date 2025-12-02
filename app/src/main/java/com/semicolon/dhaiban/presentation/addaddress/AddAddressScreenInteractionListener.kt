package com.semicolon.dhaiban.presentation.addaddress

import com.google.android.gms.maps.model.LatLng

interface AddAddressScreenInteractionListener {
    fun onClickUpButton()
    fun onChooseCountry(countryId: Int, countryName: String)
    fun onChooseGovernment(governmentId: Int, governmentName: String)
    fun onChooseCity(cityId: Int, cityName: String)
    fun onChooseRegion(regionId: Int, regionName: String)
    fun onClickMap(latLng: LatLng)
    fun onClickFullMapScreen(latLng: LatLng)
    fun onAddressValueChanged(addressName: String)
    fun onDetailedAddressValueChanged(detailedAddress: String)
    fun onCountryValueChanged(country: String)
    fun onGovernmentValueChanged(government: String)
    fun onCityValueChanged(city: String)
    fun onRegionValueChanged(region: String)
    fun onPostalCodeValueChanged(postalCode: String)
    fun onPhoneNumberValueChanged(phoneNumber: String)
    fun onPhoneCodeNumberValueChanged(codephone: String)
    fun onAddressTypeValueChanged(addressType: AddressType)
    fun onDefaultAddressValueChanged(defaultState: Boolean)
    fun onClickAddAddress()
}