package com.semicolon.dhaiban.presentation.map

import com.google.android.gms.maps.model.LatLng

sealed interface MapScreenUiEffect {
    data class NavigateToAddAddressScreen(
        val latLng: LatLng,
        val addressName: String,
        val detailedAddress: String,
        val postalCode: String
    ): MapScreenUiEffect
}