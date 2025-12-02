package com.semicolon.dhaiban.presentation.map

import com.google.android.gms.maps.model.LatLng

data class MapScreenUiState(
    val isLoading: Boolean = false,
    val latLng: LatLng = LatLng(0.0, 0.0),
    val detailedAddress: String = "Please Choose Real Location",
    val address: String = "Unknown",
    val postalCode: String = "",
    val showFirstDialog: Boolean = true,
    val userLocation: LatLng = LatLng(0.0, 0.0),
    val zoomLevel: Float = 2f,
    val errorMessage: String = ""
)
