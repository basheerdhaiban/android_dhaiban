package com.semicolon.dhaiban.presentation.map

import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng

interface MapScreenInteractionListener {
    fun onMapClicked(latLng: LatLng, geocoder: Geocoder)
    fun onDismissMapDialog()
    fun firstDialogShown()
    fun onNavigateToAddAddressScreen()
}