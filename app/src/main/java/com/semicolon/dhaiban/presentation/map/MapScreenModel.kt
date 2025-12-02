package com.semicolon.dhaiban.presentation.map

import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.android.gms.maps.model.LatLng
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.usecase.LocationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapScreenModel(private val latLng: LatLng, private val locationUseCase: LocationUseCase) :
    BaseScreenModel<MapScreenUiState, MapScreenUiEffect>(MapScreenUiState()),
    MapScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        if (latLng.longitude != 0.0 && latLng.latitude != 0.0) {
            _state.update { it.copy(latLng = latLng, userLocation = latLng) }
        } else {
            getLastLocation()
        }
    }

    private fun getLastLocation() {
        viewModelScope.launch(Dispatchers.IO) {
            locationUseCase().take(1).collectLatest { latLng ->
                latLng?.let {
                    _state.update { uiState ->
                        uiState.copy(
                            zoomLevel = if (_state.value.latLng.latitude != 0.0) 12f else 2f,
                            userLocation = LatLng(
                                latLng.first,
                                latLng.second
                            ),
                            latLng = LatLng(
                                latLng.first,
                                latLng.second
                            )
                        )
                    }
                }
                if (latLng == null) {
                    _state.update { it.copy(showFirstDialog = false) }
                }
            }
        }
    }

    override fun onDismissMapDialog() {
        _state.update { it.copy(address = "", detailedAddress = "") }
    }

    override fun onMapClicked(latLng: LatLng, geocoder: Geocoder) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update {
                it.copy(
                    zoomLevel = if (_state.value.latLng.latitude != 0.0) 12f else 2f,
                    latLng = latLng
                )
            }
            getAddressesFromLocation(geocoder, latLng.latitude, latLng.longitude)
        }
    }

    override fun onNavigateToAddAddressScreen() {
        if (_state.value.address != "Unknown")
            sendNewEffect(
                MapScreenUiEffect.NavigateToAddAddressScreen(
                    _state.value.latLng,
                    _state.value.address,
                    _state.value.detailedAddress,
                    _state.value.postalCode
                )
            )
        else
            _state.update { it.copy(errorMessage = "*Choose real location") }
    }

    override fun firstDialogShown() {
        viewModelScope.launch(Dispatchers.IO) {
            delay(1000)
            _state.update { it.copy(userLocation = LatLng(0.0, 0.0)) }
        }
    }

    @Suppress("DEPRECATION")
    private fun getAddressesFromLocation(
        geocoder: Geocoder,
        latitude: Double,
        longitude: Double
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    latitude,
                    longitude,
                    1,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            val address = addresses.firstOrNull()
                            address?.let {
                                _state.update {
                                    it.copy(
                                        detailedAddress = getDetailedAddressWithoutNulls(address),
                                        address = getAddressWithoutNulls(address),
                                        postalCode = if (address.postalCode != null) address.postalCode
                                        else "",
                                        errorMessage = ""
                                    )
                                }
                            } ?: _state.update {
                                it.copy(
                                    address = "Unknown",
                                    detailedAddress = "Please Choose Real Location"
                                )
                            }
                        }

                        override fun onError(errorMessage: String?) {
                            _state.update {
                                it.copy(
                                    address = "Unknown",
                                    detailedAddress = "Please Choose Real Location"
                                )
                            }
                        }
                    }
                )
            } else {
                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                addresses?.let {
                    if (addresses.isNotEmpty()) {
                        val address = addresses.firstOrNull()
                        address?.let {
                            _state.update {
                                it.copy(
                                    detailedAddress =
                                    "${address.adminArea}, ${address.subAdminArea}, ${address.locality}",
                                    address = "${address.adminArea}, ${address.subAdminArea}",
                                    postalCode = address.postalCode,
                                    errorMessage = ""
                                )
                            }
                        } ?: _state.update {
                            it.copy(
                                address = "Unknown",
                                detailedAddress = "Please Choose Real Location"
                            )
                        }
                    } else {
                        _state.update {
                            it.copy(
                                address = "Unknown",
                                detailedAddress = "Please Choose Real Location"
                            )
                        }
                    }
                }
            }
            Log.e("MapScreenModel", state.value.toString())
        } catch (e: Exception) {
            _state.update {
                it.copy(
                    address = "Unknown",
                    detailedAddress = "Please Choose Real Location"
                )
            }
        }
    }


    private fun getDetailedAddressWithoutNulls(address: Address): String {

        val result = buildString {
            if (address.adminArea != null) {
                append("${address.adminArea},")
            }
            if (address.subAdminArea != null) {
                append("${address.subAdminArea},")
            }
            if (address.locality != null) {
                append("${address.locality},")
            }
        }

        // Remove the trailing comma if present
        val formattedResult = if (result.endsWith(',')) {
            result.dropLast(1)
        } else {
            result
        }
        return formattedResult
    }

    private fun getAddressWithoutNulls(address: Address): String {
        val result = buildString {
            if (address.adminArea != null) {
                append("${address.adminArea},")
            }
            if (address.subAdminArea != null) {
                append("${address.subAdminArea},")
            }
        }

        // Remove the trailing comma if present
        val formattedResult = if (result.endsWith(',')) {
            result.dropLast(1)
        } else {
            result
        }
        return formattedResult
    }
}