package com.semicolon.dhaiban.designSystem.composables

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.presentation.utils.bitmapDescriptorFromVector

@Composable
fun DhaibanMapMarker(
    context: Context,
    position: LatLng,
    title: String = "",
    @DrawableRes iconResourceId: Int = R.drawable.location_pin_icon
) {
    val icon = bitmapDescriptorFromVector(
        context, iconResourceId
    )
    Marker(
        state = MarkerState(position = position),
        title = title,
        icon = icon,
    )
}