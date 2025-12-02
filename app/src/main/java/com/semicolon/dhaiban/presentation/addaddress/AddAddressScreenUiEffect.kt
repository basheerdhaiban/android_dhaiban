package com.semicolon.dhaiban.presentation.addaddress

import com.google.android.gms.maps.model.LatLng

interface AddAddressScreenUiEffect {
    data object OnNavigateBack : AddAddressScreenUiEffect
    data class OnNavigateToMap(val latLng: LatLng): AddAddressScreenUiEffect
    data class OnSendMessage(val errorMessage: String): AddAddressScreenUiEffect
}