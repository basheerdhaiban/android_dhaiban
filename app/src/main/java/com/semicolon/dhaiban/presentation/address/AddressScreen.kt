import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.semicolon.dhaiban.presentation.address.*


import android.Manifest
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.DhaibanAlertDialog
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.addaddress.AddAddressScreen
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants.ADDRESS_SCREEN
import com.semicolon.dhaiban.presentation.utils.checkPermissions
import com.semicolon.dhaiban.presentation.utils.isLocationEnabled
import kotlinx.coroutines.flow.collectLatest
import me.saket.swipe.SwipeAction
import me.saket.swipe.SwipeableActionsBox

class AddressScreen(private val fromProfile: Boolean = false) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<AddressScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: AddressScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(ADDRESS_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    AddressScreenUiEffect.OnNavigateBackToCart -> {
                        if (fromProfile) {
                            if (navigator.canPop)
                                SafeNavigator.safePop(navigator, lifecycleOwner)
                        } else {
                            SafeNavigator.safeReplace(navigator, CartScreen(), lifecycleOwner)
                        }
                    }

                    AddressScreenUiEffect.OnNavigateToMap -> {
                        SafeNavigator.safeReplace(navigator, MapScreen(fromProfile = fromProfile), lifecycleOwner)
                    }

                    is AddressScreenUiEffect.OnNavigateToCart -> SafeNavigator.safeReplace(navigator, 
                        CartScreen(
                            effect.address.id, effect.address.address
                        ), lifecycleOwner)

                    is AddressScreenUiEffect.OnNavigateToChangeAddress -> SafeNavigator.safeReplace(navigator, 
                        AddAddressScreen(
                            addressUiState = effect.address,
                            addressName = effect.address.name,
                            detailedAddress = effect.address.address,
                            latLng = LatLng(effect.address.lat, effect.address.lon),
                            postalCode = effect.address.postalCode,
                            fromProfile = fromProfile
                        ), lifecycleOwner)

                    AddressScreenUiEffect.OnNavigateToNotificationScreen -> SafeNavigator.safePush(navigator, NotificationScreen(), lifecycleOwner)
                }
            }
        }
        AddressScreenContent(
            context = context,
            fromProfile = fromProfile,
            state = state,
            listener = screenModel
        )
        BackHandler {
            if (fromProfile.not())
                SafeNavigator.safeReplace(
                    navigator,
                    screen = CartScreen(),
                    lifecycleOwner = lifecycleOwner
                )
            else if (navigator.canPop)
                SafeNavigator.safePop(navigator, lifecycleOwner)
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun AddressScreenContent(
    context: Context,
    fromProfile: Boolean,
    state: AddressScreenUiState,
    listener: AddressScreenInteractionListener
) {
    var showLocationSnackBar by remember { mutableStateOf(false) }
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBarWithIcon(
            title = Theme.strings.address,
            onClickUpButton = { listener.onClickUpButton() },
            onClickNotification = { listener.onCLickNotification() }
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedContent(targetState = state.isLoading, label = "") { isLoading ->
            if (isLoading) {
                LoadingContent()
            } else {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    AnimatedContent(targetState = state.addresses.isNotEmpty(), label = "") {
                        if (it) {
                            Column {
                                state.addresses.forEach { addressUiState ->
                                    AddressItem(
                                        addressUiState = addressUiState,
                                        onButtonClick = { address ->
                                            when (address.defaultAddress) {
                                                AddressState.DEFAULT -> {
                                                    listener.onClickChange(address)
                                                }

                                                AddressState.NOT_DEFAULT -> listener.onClickChange(
                                                    address
                                                )
                                            }
                                        },
                                        onDelete = { addressId ->
                                            listener.onDeleteAddress(addressId)
                                        }) { address ->
                                        if (fromProfile.not())
                                            listener.onClickAddress(address)
                                    }
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .background(Theme.colors.transparent),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.image_error_icon),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            modifier = Modifier.noRippleEffect {
                                if (checkPermissions(context)) {
                                    if (isLocationEnabled(context)) {
                                        showLocationSnackBar = false
                                        listener.onClickAddAddress()
                                    } else {
                                        showLocationSnackBar = true
                                    }

                                } else {
                                    permissionState.launchMultiplePermissionRequest()
                                }
                            },
                            text = Theme.strings.addAddress,
                            color = Theme.colors.mediumBrown,
                            style = Theme.typography.title
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    AnimatedVisibility(
                        visible = showLocationSnackBar,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it },
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            DhaibanAlertDialog(
                                title = Theme.strings.enableLocation,
                                body = {
                                    Row(horizontalArrangement = Arrangement.Center) {
                                        Text(text = Theme.strings.locationNeedsToBeEnabled)
                                    }
                                },
                                positiveText = "Enable",
                                negativeText = "Cancel",
                                onDismiss = { showLocationSnackBar = false },
                                onPositive = {
                                    showLocationSnackBar = false
                                    Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
                                        startActivity(context, this, null)
                                    }
                                }
                            )
                        }
                    }
                }

            }
        }

    }
}

@Composable
private fun AddressItem(
    addressUiState: AddressUiState,
    onButtonClick: (AddressUiState) -> Unit,
    onDelete: (Int) -> Unit,
    onClick: (AddressUiState) -> Unit
) {
    val delete = SwipeAction(
        onSwipe = {
            onDelete(addressUiState.id)
        },
        icon = {
            Icon(
                modifier = Modifier.padding(16.dp),
                painter = painterResource(id = R.drawable.delete_address_icon),
                contentDescription = "",
                tint = Theme.colors.white
            )
        },
        background = Theme.colors.error
    )
    SwipeableActionsBox(endActions = listOf(delete), swipeThreshold = 150.dp) {
        Column(modifier = Modifier
            .padding(horizontal = 16.dp)
            .noRippleEffect { onClick(addressUiState) }) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(4f)) {
                    Text(
                        text = if (addressUiState.workHome == "home") Theme.strings.home else Theme.strings.work,
                        style = Theme.typography.title.copy(fontWeight = FontWeight(600)),
                        color = Theme.colors.black
                    )
                    Text(
                        text = addressUiState.address,
                        style = Theme.typography.caption,
                        color = Theme.colors.silverGray
                    )
                }
                Button(
                    onClick = { onButtonClick(addressUiState) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                        if (addressUiState.defaultAddress == AddressState.NOT_DEFAULT)
                            Theme.colors.mediumBrownTwo
                        else
                            Theme.colors.mediumBrown
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 4.dp),
                        text =
                        if (addressUiState.defaultAddress == AddressState.NOT_DEFAULT)
                            Theme.strings.change
                        else
                            Theme.strings.default,
                        style = Theme.typography.body.copy(fontSize = 13.sp),
                        color = Theme.colors.white
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Theme.colors.silver.copy(alpha = 0.5f))
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        repeat(10) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .width(200.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                }
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .width(70.dp)
                        .height(35.dp)
                        .shimmerEffect()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Theme.colors.silver.copy(alpha = 0.5f))
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingContentPrev() {
    DhaibanTheme {
        LoadingContent()
    }
}

@Preview(showBackground = true)
@Composable
private fun AddressItemPrev() {
    DhaibanTheme {
        AddressItem(AddressUiState(), {}, {}) {}
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun AddressScreenContentPrev() {
    val dummyListener = object : AddressScreenInteractionListener {
        override fun onClickUpButton() {}
        override fun onClickAddAddress() {}
        override fun onClickAddress(address: AddressUiState) {}
        override fun onClickDefault(address: AddressUiState) {}
        override fun onClickChange(address: AddressUiState) {}
        override fun onDeleteAddress(addressId: Int) {}
        override fun onCLickNotification() {}
    }

    DhaibanTheme {
        AddressScreenContent(
            context = LocalContext.current,
            false,
            state = AddressScreenUiState(),
            listener = dummyListener
        )
    }
}