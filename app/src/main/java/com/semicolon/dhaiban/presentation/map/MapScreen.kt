import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner

import android.content.Context
import android.location.Geocoder
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.presentation.addaddress.AddAddressScreen
import com.semicolon.dhaiban.presentation.map.MapScreenInteractionListener
import com.semicolon.dhaiban.presentation.map.MapScreenModel
import com.semicolon.dhaiban.presentation.map.MapScreenUiEffect
import com.semicolon.dhaiban.presentation.map.MapScreenUiState
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf
import java.util.Locale

class MapScreen(
    private val latLng: LatLng = LatLng(0.0, 0.0),
    private val fromProfile: Boolean = false
) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<MapScreenModel>(parameters = { parametersOf(latLng) })
        val state: MapScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    is MapScreenUiEffect.NavigateToAddAddressScreen -> {
                        SafeNavigator.safeReplace(
                            navigator,
                            AddAddressScreen(
                                latLng = effect.latLng,
                                addressName = effect.addressName,
                                detailedAddress = effect.detailedAddress,
                                postalCode = effect.postalCode,
                                fromProfile = fromProfile
                            ),
                            lifecycleOwner = lifecycleOwner
                        )
                    }
                }
            }
        }
        MapScreenContent(state = state, listener = screenModel, context = context)
        BackHandler {
            SafeNavigator.safeReplace(
                navigator, AddressScreen(fromProfile),
                lifecycleOwner = lifecycleOwner
            )
        }
    }
}

@Composable
private fun MapScreenContent(
    state: MapScreenUiState,
    listener: MapScreenInteractionListener,
    context: Context
) {
    val geocoder = remember { Geocoder(context, Locale.getDefault()) }
    val markerPosition = state.latLng
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(markerPosition, 2f)
    }


    if (state.zoomLevel == 12f) {
        LaunchedEffect(Unit) {
            cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(state.userLocation, 15f))
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.76f)
                .safeDrawingPadding()
                .align(Alignment.TopCenter),
            cameraPositionState = cameraPositionState,
            uiSettings = MapUiSettings(zoomControlsEnabled = false),
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {
                },
            ) {
                Image(
                    painter = painterResource(id = R.drawable.location_pin_icon),
                    contentDescription = "marker",
                )
            }
        }
        if (cameraPositionState.isMoving.not()) {
            if (state.userLocation.latitude != 0.0) {
                listener.onMapClicked(
                    if (state.userLocation.latitude != 0.0) state.userLocation else cameraPositionState.position.target,
                    geocoder
                )
                listener.firstDialogShown()
            } else {
                listener.onMapClicked(
                    cameraPositionState.position.target,
                    geocoder
                )
            }
        }

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Bottom
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 16.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .background(Theme.colors.white),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(3f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.location_icon),
                                    contentDescription = "Location Icon"
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(
                                        text = state.address,
                                        color = Theme.colors.primarySemiDark,
                                        style = Theme.typography.body
                                    )
                                    Text(
                                        text = state.detailedAddress,
                                        color = Theme.colors.silverGray,
                                        style = Theme.typography.caption
                                    )
                                }
                            }
                        }
//                            Button(
//                                modifier = Modifier.weight(1f),
//                                onClick = { listener.onDismissMapDialog() },
//                                colors = ButtonDefaults.buttonColors(
//                                    containerColor = Theme.colors.mediumBrown
//                                ),
//                                shape = RoundedCornerShape(12.dp),
//                                contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
//                            ) {
//                                Text(
//                                    text = Theme.strings.change,
//                                    color = Theme.colors.white,
//                                    style = Theme.typography.caption
//                                )
//                            }
                    }
                    Spacer(modifier = Modifier.weight(0.3f))
                    AnimatedVisibility(visible = state.errorMessage.isNotEmpty()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                        ) {
                            Text(text = state.errorMessage, color = Theme.colors.error)
                        }
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f)
                            .padding(horizontal = 16.dp),
                        onClick = { listener.onNavigateToAddAddressScreen() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Theme.colors.mediumBrown
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = Theme.strings.addAddress,
                            color = Theme.colors.white,
                            style = Theme.typography.title
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.2f))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomDialog(
    modifier: Modifier = Modifier,
    address: String,
    detailedAddress: String,
    errorMessage: String,
    positiveText: String,
    negativeText: String,
    onDismiss: () -> Unit,
    onPositive: () -> Unit
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 16.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(0.30f)
                    .background(Theme.colors.white),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(2f)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(3f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.location_icon),
                                    contentDescription = "Location Icon"
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(
                                        text = address,
                                        color = Theme.colors.primarySemiDark,
                                        style = Theme.typography.body
                                    )
                                    Text(
                                        text = detailedAddress,
                                        color = Theme.colors.silverGray,
                                        style = Theme.typography.caption
                                    )
                                }
                            }
                            if (errorMessage.isNotEmpty())
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(text = errorMessage, color = Theme.colors.error)
                                }
                        }
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = { onDismiss() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Theme.colors.mediumBrown
                            ),
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 0.dp)
                        ) {
                            Text(
                                text = negativeText,
                                color = Theme.colors.white,
                                style = Theme.typography.caption
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(0.3f))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.8f)
                            .padding(horizontal = 16.dp),
                        onClick = { onPositive() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Theme.colors.mediumBrown
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = positiveText,
                            color = Theme.colors.white,
                            style = Theme.typography.title
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.2f))
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BottomDialogPreview() {
    DhaibanTheme {
        BottomDialog(
            address = "",
            detailedAddress = "",
            errorMessage = "",
            positiveText = "",
            negativeText = "",
            onDismiss = {}) {

        }
    }
}
