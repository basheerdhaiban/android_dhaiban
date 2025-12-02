package com.semicolon.dhaiban.presentation.addaddress

import AddressScreen
import MapScreen
import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapUiSettings
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.DhaibanMapMarker
import com.semicolon.dhaiban.designSystem.composables.DhaibanMoblieTextField
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.address.AddressState
import com.semicolon.dhaiban.presentation.address.AddressUiState
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf
import java.util.Locale

class AddAddressScreen(
    private val latLng: LatLng = LatLng(0.0, 0.0),
    private val addressName: String,
    private val detailedAddress: String,
    private val postalCode: String,
    private val addressUiState: AddressUiState = AddressUiState(),
    private val fromProfile: Boolean = false
) : Screen {
    @Composable
    override fun Content() {
        val screenModel =
            getScreenModel<AddAddressScreenModel>(parameters = {
                parametersOf(
                    latLng,
                    addressName,
                    detailedAddress,
                    "",
                    addressUiState
                )
            })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: AddAddressScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val countries by screenModel.countries.collectAsState()
        val governments by screenModel.governments.collectAsState()
        val cities by screenModel.cities.collectAsState()
        val regions by screenModel.regions.collectAsState()
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(Constants.ADD_ADDRESS_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    is AddAddressScreenUiEffect.OnNavigateBack -> navigator.replace(
                        AddressScreen(
                            fromProfile
                        )
                    )

                    is AddAddressScreenUiEffect.OnNavigateToMap -> {
                        navigator.replace(MapScreen(effect.latLng))
                    }

                    is AddAddressScreenUiEffect.OnSendMessage -> {
                        navigator.replace(AddressScreen(fromProfile))
                        Toast.makeText(
                            context, effect.errorMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        AddAddressScreenContent(
            context = context,
            state = state,
            listener = screenModel,
            countries,
            governments,
            cities,
            regions
        )
        BackHandler {
            navigator.replace(AddressScreen(fromProfile))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddAddressScreenContent(
    context: Context,
    state: AddAddressScreenUiState,
    listener: AddAddressScreenInteractionListener,
    countries: List<MenuItemUiState>,
    governments: List<MenuItemUiState>,
    cities: List<MenuItemUiState>,
    regions: List<MenuItemUiState>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {
        Card(
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
            shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        modifier = Modifier
                            .scale(
                                scaleX = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f,
                                scaleY = 1f
                            )
                            .noRippleEffect { listener.onClickUpButton() },
                        painter = painterResource(id = R.drawable.bar_back_icon),
                        contentDescription = "navigation back icon"
                    )
                    Text(
                        text = if (state.editAddress) Theme.strings.changeAddress else Theme.strings.addAddress,
                        style = Theme.typography.headline
                    )
                }
//                Image(
//                    painter = painterResource(id = R.drawable.notification_icon),
//                    contentDescription = "Notification Icon"
//                )
            }
        }
        AnimatedContent(
            targetState = state.latLng.latitude != 0.0 && state.latLng.longitude != 0.0,
            label = ""
        ) { isNotLoading ->
            if (isNotLoading) {
                Column {
                    MiniMap(
                        latLng = state.latLng,
                        context = context,
                        onFullMapScreenClicked = { listener.onClickFullMapScreen(state.latLng) }) { latLng ->
                        listener.onClickMap(latLng)
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = Theme.strings.addressName,
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .padding(horizontal = 16.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(56.dp),
                            value = if (state.editAddress) state.addressUiState.name else state.addressName,
                            onValueChange = { listener.onAddressValueChanged(it) },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Theme.colors.primary,
                                unfocusedIndicatorColor = Theme.colors.whiteTwo,
                                unfocusedContainerColor = Theme.colors.background,
                                cursorColor = Theme.colors.mediumBrown,
                                selectionColors = TextSelectionColors(
                                    handleColor = Theme.colors.mediumBrown,
                                    backgroundColor = Theme.colors.darkBeige
                                )
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = state.errorMessage.isNotEmpty() && state.errorState.addressRequired) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp),
                                text = Theme.strings.addressRequired,
                                color = Theme.colors.error,
                                style = Theme.typography.body
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Theme.strings.detailedAddress,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black,
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(56.dp),
                            value = if (state.editAddress) state.addressUiState.address else state.detailedAddress,
                            onValueChange = { listener.onDetailedAddressValueChanged(it) },
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Theme.colors.primary,
                                unfocusedIndicatorColor = Theme.colors.whiteTwo,
                                unfocusedContainerColor = Theme.colors.background,
                                cursorColor = Theme.colors.mediumBrown,
                                selectionColors = TextSelectionColors(
                                    handleColor = Theme.colors.mediumBrown,
                                    backgroundColor = Theme.colors.darkBeige
                                )
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = state.errorMessage.isNotEmpty() && state.errorState.detailedAddressRequired) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp),
                                text = Theme.strings.detailedAddressRequired,
                                color = Theme.colors.error,
                                style = Theme.typography.body
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Theme.strings.country,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black,
                        )
                        var counrty by remember {
                            mutableStateOf(false)
                        }
                        EditTextWithDropDownList(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { counrty = true },
                            dropStateForBottomSheetpara=state.dropStateForBottomSheetCountry,
                            text = state.country,
                            items = countries,
                            isCountries = true,
                            countryId = state.selectedCountry,
                            isLoading = state.countriesLoading,
                            onclickWhenNotEnabled = {counrty=true},
                            onValueChange = {
                         counrty = true
                            }
                        ) { countryId, countryName ->
                            listener.onChooseCountry(countryId, countryName)
                        }
                        val searchSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                        if (counrty) {
                            ModalBottomSheet(
                                onDismissRequest = { counrty = false },
                                sheetState = searchSheetState,
                                containerColor = Color.White,
                                shape = BottomSheetDefaults.ExpandedShape,

                                ) {
                                val resultList = ArrayList<MenuItemUiState>()

                                val countries = remember { mutableStateOf(countries) }
                                var filteredCountries: List<MenuItemUiState>

                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = state.country,
                                        enabled = true
                                        ,
                                        onValueChange = {
                                            listener.onCountryValueChanged(it)
                                            state.dropStateForBottomSheetCountry= false
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Theme.colors.primary,
                                            unfocusedIndicatorColor = Theme.colors.primary,
                                            unfocusedContainerColor = Theme.colors.background,
                                            disabledContainerColor = Theme.colors.background,
                                            disabledIndicatorColor = Theme.colors.primary,
                                            focusedContainerColor = Theme.colors.splashBackground,
                                            cursorColor = Theme.colors.mediumBrown,
                                            selectionColors = TextSelectionColors(
                                                handleColor = Theme.colors.mediumBrown,
                                                backgroundColor = Theme.colors.darkBeige
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        trailingIcon = {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = "clear text",
                                                modifier = Modifier
                                                    .clickable {

                                                        listener.onCountryValueChanged("")
                                                    }
                                            )
                                        }
                                    )

                                    LazyColumn(Modifier.padding(top = 8.dp)) {
                                        filteredCountries = if (state.country=="") {
                                            countries.value
                                        } else {
                                            for (country in countries.value) {
                                                if (country.name.lowercase(Locale.getDefault())
                                                        .contains(state.country.lowercase(
                                                            Locale.getDefault()))
                                                ) {
                                                    resultList.add(country)
                                                }
                                            }
                                            resultList
                                        }
                                        if (filteredCountries.isEmpty()){
                                            item {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 16.dp).fillMaxWidth()
                                                        .padding(top = 8.dp),
                                                    text = "There is no Result found",
                                                    color = Theme.colors.error,
                                                    style = Theme.typography.body
                                                )
                                            }
                                        }
                                        else{
                                            items(filteredCountries, itemContent = { item ->
                                                Text(
                                                    modifier = Modifier
                                                        .padding(top = 10.dp, bottom = 8.dp).fillMaxWidth()
                                                        .clickable {
                                                            listener.onChooseCountry(item.id, item.name)

//                                                        val country = item.name
//                                                            .split(" ")
//                                                            .first()
                                                            state.country = item.name

                                                            counrty = false

                                                        }, text = item.name, fontSize = 18.sp,

                                                    )
                                                Divider(modifier = Modifier.padding(top = 6.dp))
                                            })

                                    }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = state.errorMessage.isNotEmpty() && state.errorState.countryRequired) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp),
                                text = Theme.strings.countryRequired,
                                color = Theme.colors.error,
                                style = Theme.typography.body
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Theme.strings.government,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black,
                        )
                        var governmentBottomSheetFlag by remember {
                            mutableStateOf(false)
                        }
                        EditTextWithDropDownList(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { governmentBottomSheetFlag = true },
                            text = state.government,
                            items = governments,
                            dropStateForBottomSheetpara=state.dropStateForBottomSheetGoverment,

                            isGovernments = true,
                            countryId = state.selectedCountry,
                            governmentId = state.selectedGovernment,
                            clearText = state.clearGovCityRegion,
                            isLoading = state.governmentsLoading,
                            onclickWhenNotEnabled = {governmentBottomSheetFlag=true},

                            onValueChange = {
                                listener.onGovernmentValueChanged(it)
                            }
                        ) { governmentId, governmentName ->
                            listener.onChooseGovernment(governmentId, governmentName)
                        }
                        if (governmentBottomSheetFlag) {
                            ModalBottomSheet(
                                onDismissRequest = { governmentBottomSheetFlag = false },
                                sheetState = searchSheetState,
                                containerColor = Color.White,
                                shape = BottomSheetDefaults.ExpandedShape,

                                ) {
                                val government = remember { mutableStateOf(governments) }
                                var filteredgovernments: List<MenuItemUiState>

                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = state.government,
                                        enabled = true
                                        ,
                                        onValueChange = {
                                            listener.onGovernmentValueChanged(it)
                                            state.dropStateForBottomSheetCountry= false
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Theme.colors.primary,
                                            unfocusedIndicatorColor = Theme.colors.primary,
                                            unfocusedContainerColor = Theme.colors.background,
                                            disabledContainerColor = Theme.colors.background,
                                            disabledIndicatorColor = Theme.colors.primary,
                                            focusedContainerColor = Theme.colors.splashBackground,
                                            cursorColor = Theme.colors.mediumBrown,
                                            selectionColors = TextSelectionColors(
                                                handleColor = Theme.colors.mediumBrown,
                                                backgroundColor = Theme.colors.darkBeige
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        trailingIcon = {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = "clear text",
                                                modifier = Modifier
                                                    .clickable {
                                                        listener.onGovernmentValueChanged("")
                                                    }
                                            )
                                        }
                                    )

                                    LazyColumn(Modifier.padding(top = 8.dp)) {
                                        filteredgovernments = if (state.government.isEmpty()) {
                                            government.value
                                        } else {
                                            val resultList = ArrayList<MenuItemUiState>()
                                            for (government in government.value) {
                                                if (government.name.lowercase(Locale.getDefault())
                                                        .contains(state.government.lowercase(
                                                            Locale.getDefault()))
                                                ) {
                                                    resultList.add(government)
                                                }
                                            }
                                            resultList
                                        }
                                        if (filteredgovernments.isEmpty()){
                                            item {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 16.dp).fillMaxWidth()
                                                        .padding(top = 8.dp),
                                                    text = "There is no Result found",
                                                    color = Theme.colors.error,
                                                    style = Theme.typography.body
                                                )
                                            }
                                        }
                                        else{
                                            items(filteredgovernments, itemContent = { item ->
                                                Text(
                                                    modifier = Modifier.fillMaxWidth()
                                                        .padding(top = 10.dp, bottom = 8.dp)
                                                        .clickable {

                                                            val country = item.name
                                                                .split(" ")
                                                                .first()
                                                            listener.onGovernmentValueChanged(country)
                                                            listener.onChooseGovernment(
                                                                item.id,
                                                                item.name
                                                            )
                                                            governmentBottomSheetFlag = false

                                                        }, text = item.name, fontSize = 18.sp,

                                                    )
                                                Divider(modifier = Modifier.padding(top = 6.dp))
                                            })

                                        }

                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = state.errorMessage.isNotEmpty() && state.errorState.governmentRequired) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp),
                                text = Theme.strings.governmentRequired,
                                color = Theme.colors.error,
                                style = Theme.typography.body
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Theme.strings.city,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black,
                        )
                        var cityBottomSheetFlag by remember {
                            mutableStateOf(false)
                        }
                        EditTextWithDropDownList(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { cityBottomSheetFlag = true },
                            text = state.city,
                            items = cities,
                            dropStateForBottomSheetpara=state.dropStateForBottomSheetCity,

                            isCities = true,
                            cityId = state.selectedCity,
                            governmentId = state.selectedGovernment,
                            clearText = state.clearGovCityRegion || state.clearCityRegion || state.clearCity,
                            isLoading = state.citesLoading,
                            onclickWhenNotEnabled = {cityBottomSheetFlag=true},

                            onValueChange = {
                                listener.onCityValueChanged(it)
                            }
                        ) { cityId, cityName ->
                            listener.onChooseCity(cityId, cityName)
                        }
                        if (cityBottomSheetFlag) {
                            ModalBottomSheet(
                                onDismissRequest = { cityBottomSheetFlag = false },
                                sheetState = searchSheetState,
                                containerColor = Color.White,
                                shape = BottomSheetDefaults.ExpandedShape,

                                ) {
                                val cities = remember { mutableStateOf(cities) }
                                var filteredcity: List<MenuItemUiState>

                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = state.city,
                                        enabled = true
                                        ,
                                        onValueChange = {
                                            listener.onCityValueChanged(it)
                                            state.dropStateForBottomSheetCountry= false
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Theme.colors.primary,
                                            unfocusedIndicatorColor = Theme.colors.primary,
                                            unfocusedContainerColor = Theme.colors.background,
                                            disabledContainerColor = Theme.colors.background,
                                            disabledIndicatorColor = Theme.colors.primary,
                                            focusedContainerColor = Theme.colors.splashBackground,
                                            cursorColor = Theme.colors.mediumBrown,
                                            selectionColors = TextSelectionColors(
                                                handleColor = Theme.colors.mediumBrown,
                                                backgroundColor = Theme.colors.darkBeige
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        trailingIcon = {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = "clear text",
                                                modifier = Modifier
                                                    .clickable {
                                                        listener.onCityValueChanged("")
                                                    }
                                            )
                                        }
                                    )

                                    LazyColumn(Modifier.padding(top = 8.dp)) {
                                        filteredcity = if (state.city.isEmpty()) {
                                            cities.value
                                        } else {
                                            val resultList = ArrayList<MenuItemUiState>()
                                            for (city in cities.value) {
                                                if (city.name.lowercase(Locale.getDefault())
                                                        .contains(state.country.lowercase(
                                                            Locale.getDefault()))
                                                ) {
                                                    resultList.add(city)
                                                }
                                            }
                                            resultList
                                        }
                                        if (filteredcity.isEmpty()){
                                            item {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 16.dp)
                                                        .padding(top = 8.dp),
                                                    text = "There is no Result found",
                                                    color = Theme.colors.error,
                                                    style = Theme.typography.body
                                                )
                                            }
                                        }
                                        else{
                                        items(filteredcity, itemContent = { item ->

                                            Text(
                                                modifier = Modifier
                                                    .padding(top = 10.dp, bottom = 8.dp).fillMaxWidth()
                                                    .fillMaxWidth()
                                                    .clickable {

                                                        val city = item.name
                                                            .split(" ")
                                                            .first()
                                                        listener.onCityValueChanged(city)
                                                        listener.onChooseCity(item.id, item.name)
                                                        cityBottomSheetFlag = false

                                                    }, text = item.name, fontSize = 18.sp,

                                                )
                                            Divider(modifier = Modifier.padding(top = 6.dp))
                                        }) }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = state.errorMessage.isNotEmpty() && state.errorState.cityRequired) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp),
                                text = Theme.strings.cityRequired,
                                color = Theme.colors.error,
                                style = Theme.typography.body
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Theme.strings.region,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black,
                        )
                        var regionsBottomSheetFlag by remember {
                            mutableStateOf(false)
                        }
                        EditTextWithDropDownList(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .clickable { regionsBottomSheetFlag = true },
                            text = state.region,
                            items = regions,
                            isRegions = true,
                            dropStateForBottomSheetpara=state.dropStateForBottomSheetRegion,

                            cityId = state.selectedCity,
                            regionId = state.selectedRegion,
                            clearText = state.clearGovCityRegion || state.clearRegion || state.clearCityRegion,
                            isLoading = state.regionsLoading,
                            onclickWhenNotEnabled = {regionsBottomSheetFlag=true},

                            onValueChange = {
                                listener.onRegionValueChanged(it)
                            }
                        ) { regionId, regionName ->
                            listener.onChooseRegion(regionId, regionName)
                        }
                        if (regionsBottomSheetFlag) {
                            ModalBottomSheet(
                                onDismissRequest = { regionsBottomSheetFlag = false },
                                sheetState = searchSheetState,
                                containerColor = Color.White,
                                shape = BottomSheetDefaults.ExpandedShape,

                                ) {
                                val regions = remember { mutableStateOf(regions) }
                                var filteredcity: List<MenuItemUiState>

                                Column(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    OutlinedTextField(
                                        modifier = Modifier.fillMaxWidth(),
                                        value = state.region ,
                                        enabled = true
                                        ,
                                        onValueChange = {
                                            listener.onRegionValueChanged(it)
                                            state.dropStateForBottomSheetCountry= false
                                        },
                                        singleLine = true,
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Theme.colors.primary,
                                            unfocusedIndicatorColor = Theme.colors.primary,
                                            unfocusedContainerColor = Theme.colors.background,
                                            disabledContainerColor = Theme.colors.background,
                                            disabledIndicatorColor = Theme.colors.primary,
                                            focusedContainerColor = Theme.colors.splashBackground,
                                            cursorColor = Theme.colors.mediumBrown,
                                            selectionColors = TextSelectionColors(
                                                handleColor = Theme.colors.mediumBrown,
                                                backgroundColor = Theme.colors.darkBeige
                                            )
                                        ),
                                        shape = RoundedCornerShape(16.dp),
                                        trailingIcon = {
                                            Icon(
                                                Icons.Default.Clear,
                                                contentDescription = "clear text",
                                                modifier = Modifier
                                                    .clickable {
                                                        listener.onRegionValueChanged("")
                                                    }
                                            )
                                        }
                                    )

                                    LazyColumn(Modifier.padding(top = 8.dp)) {
                                        filteredcity = if (state.region.isEmpty()) {
                                            regions.value
                                        } else {
                                            val resultList = ArrayList<MenuItemUiState>()
                                            for (region in regions.value) {
                                                if (region.name.lowercase(Locale.getDefault())
                                                        .contains(state.region.lowercase(
                                                            Locale.getDefault()))
                                                ) {
                                                    resultList.add(region)
                                                }
                                            }
                                            resultList
                                        }
                                        if (filteredcity.isEmpty()){
                                            item {
                                                Text(
                                                    modifier = Modifier
                                                        .padding(horizontal = 16.dp)
                                                        .padding(top = 8.dp),
                                                    text = "There is no Result found",
                                                    color = Theme.colors.error,
                                                    style = Theme.typography.body
                                                )
                                            }
                                        }
                                        else{
                                            items(filteredcity, itemContent = { item ->
                                                Text(
                                                    modifier = Modifier
                                                        .padding(top = 10.dp, bottom = 8.dp).fillMaxWidth()
                                                        .clickable {

                                                            val city = item.name
                                                                .split(" ")
                                                                .first()
                                                            listener.onRegionValueChanged(city)
                                                            listener.onChooseRegion(item.id, item.name)
                                                            regionsBottomSheetFlag = false

                                                        }, text = item.name, fontSize = 18.sp,

                                                    )
                                                Divider(modifier = Modifier.padding(top = 6.dp))
                                            }) }


                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }

                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = state.errorMessage.isNotEmpty() && state.errorState.regionRequired) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp),
                                text = Theme.strings.regionRequired,
                                color = Theme.colors.error,
                                style = Theme.typography.body
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Theme.strings.postalCode,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black,
                        )
                        Log.d("postalCode", state.postalCode)
                        OutlinedTextField(
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth()
                                .height(56.dp),
                            value = if (state.editAddress) state.addressUiState.postalCode else state.postalCode,
                            onValueChange = { listener.onPostalCodeValueChanged(it) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Theme.colors.primary,
                                unfocusedIndicatorColor = Theme.colors.whiteTwo,
                                unfocusedContainerColor = Theme.colors.background,
                                cursorColor = Theme.colors.mediumBrown,
                                selectionColors = TextSelectionColors(
                                    handleColor = Theme.colors.mediumBrown,
                                    backgroundColor = Theme.colors.darkBeige
                                )
                            ),
                            shape = RoundedCornerShape(10.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = state.errorMessage.isNotEmpty() && state.errorState.postalCodeRequired) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp),
                                text = Theme.strings.postalCodeRequired,
                                color = Theme.colors.error,
                                style = Theme.typography.body
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Theme.strings.contactNumber,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black,
                        )
                        Log.d("codephoness",state.countryCode)
                        val isValidPhoneNumber = remember { mutableStateOf(true) }
                        DhaibanMoblieTextField(
                            modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                            label = "",
                            text =  if (state.editAddress) state.addressUiState.phone else state.contactNumber,
                            onValueChange = {
                                if (it.length in 9..14) {
                                    isValidPhoneNumber.value = true

                                } else {
                                    isValidPhoneNumber.value = false
                                }
                                listener.onPhoneNumberValueChanged(it)
                            },
                            hint = Theme.strings.phoneNumberHint,
                            leadingIcon = R.drawable.icon_phone,
                            keyboardType = KeyboardType.Phone,

                            onCountryChange = {
listener.onPhoneCodeNumberValueChanged(it.countryPhoneNumberCode)

                                Log.d("countryCode","${it.countryCode}")
                                Log.d("countryPhoneNumberCode","${it.countryPhoneNumberCode}")

                            }
                        )
//                        OutlinedTextField(
//                            modifier = Modifier
//                                .padding(horizontal = 16.dp)
//                                .fillMaxWidth()
//                                .height(56.dp),
//                            value = if (state.editAddress) state.addressUiState.phone else state.contactNumber,
//                            onValueChange = {
//                                if (it.length in 8..14) {
//                                    isValidPhoneNumber.value = true
//
//                                } else {
//                                    isValidPhoneNumber.value = false
//                                }
//                                listener.onPhoneNumberValueChanged(it)
//                            },
//                            colors = TextFieldDefaults.colors(
//                                focusedIndicatorColor = if (isValidPhoneNumber.value) Theme.colors.primary else Color.Red,
//                                unfocusedIndicatorColor = Theme.colors.whiteTwo,
//                                unfocusedContainerColor = Theme.colors.background,
//                                cursorColor = Theme.colors.mediumBrown,
//                                selectionColors = TextSelectionColors(
//                                    handleColor = Theme.colors.mediumBrown,
//                                    backgroundColor = Theme.colors.darkBeige
//                                )
//                            ),
//                            shape = RoundedCornerShape(10.dp),
//                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
//                            leadingIcon = {
//                                Box(
//                                    modifier = Modifier
//                                        .fillMaxHeight()
//                                        .fillMaxWidth(0.15f)
//                                        .padding(2.dp)
//                                        .background(
//                                            color = Theme.colors.container,
//                                            shape = RoundedCornerShape(
//                                                topEnd = 10.dp,
//                                                topStart = 10.dp,
//                                                bottomStart = 10.dp,
//                                            )
//                                        ),
//                                    contentAlignment = Alignment.Center
//                                ) {
//                                    Image(
//                                        modifier = Modifier,
//                                        imageVector = ImageVector.vectorResource(id = R.drawable.icon_phone),
//                                        contentDescription = ""
//                                    )
//                                }
//                            },
//                            singleLine = true,
//                            isError = !isValidPhoneNumber.value
//                        )
                        AnimatedVisibility(!isValidPhoneNumber.value) {
                            Text(
                                text = Theme.strings.invalidPhone ,
                                modifier = Modifier.padding(top = 4.dp, start = 16.dp, end = 16.dp),
                                style = Theme.typography.body,
                                color = Theme.colors.error
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedVisibility(visible = state.errorMessage.isNotEmpty() && state.errorState.contactNumberRequired) {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(top = 8.dp),
                                text = Theme.strings.contactNumberRequired,
                                color = Theme.colors.error,
                                style = Theme.typography.body
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = Theme.strings.addressType,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 8.dp),
                            style = Theme.typography.title,
                            color = Theme.colors.black,
                        )
                        Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                            Button(
                                onClick = {

                                        listener.onAddressTypeValueChanged(AddressType.HOME)
                                    },
                                    modifier =
                                        if (state.editAddress) {
                                            if (state.addressUiState.workHome == "home") Modifier
                                                .width(100.dp)
                                                .border(
                                                    2.dp,
                                                    Theme.colors.mediumBrown,
                                                    RoundedCornerShape(12.dp)
                                                ) else Modifier.width(100.dp)
                                        } else {
                                            if (state.addressType == AddressType.HOME) Modifier
                                                .width(100.dp)
                                                .border(
                                                    2.dp,
                                                    Theme.colors.mediumBrown,
                                                    RoundedCornerShape(12.dp)
                                                ) else Modifier.width(100.dp)
                                        },
                                    colors =
                                        ButtonDefaults.buttonColors(containerColor = Theme.colors.beigeTwo),
                                    shape = RoundedCornerShape(12.dp)
                                    ) {
                                    Text(
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        text = Theme.strings.home,
                                        style = Theme.typography.body.copy(
                                            fontWeight = FontWeight(
                                                500
                                            )
                                        ),
                                        color = Theme.colors.black
                                    )
                                }
                                    Spacer(modifier = Modifier.width(32.dp))
                                    Button(
                                        onClick = { listener.onAddressTypeValueChanged(AddressType.WORK) },
                                        modifier =
                                        if (state.editAddress) {
                                            if (state.addressUiState.workHome == "work") Modifier
                                                .width(100.dp)
                                                .border(
                                                    2.dp,
                                                    Theme.colors.mediumBrown,
                                                    RoundedCornerShape(12.dp)
                                                ) else Modifier.width(100.dp)
                                        } else {
                                            if (state.addressType == AddressType.WORK) Modifier
                                                .width(100.dp)
                                                .border(
                                                    2.dp,
                                                    Theme.colors.mediumBrown,
                                                    RoundedCornerShape(12.dp)
                                                ) else Modifier.width(100.dp)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.beigeTwo),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(vertical = 4.dp),
                                            text = Theme.strings.work,
                                            style = Theme.typography.body.copy(
                                                fontWeight = FontWeight(
                                                    500
                                                )
                                            ),
                                            color = Theme.colors.black
                                        )
                                    }
                                }
                                        Spacer (modifier = Modifier.height(8.dp))
                                        Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Checkbox(
                                    checked = if (state.editAddress)
                                        state.addressUiState.defaultAddress == AddressState.DEFAULT
                                    else state.defaultAddress,
                                    onCheckedChange = { listener.onDefaultAddressValueChanged(it) },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = Theme.colors.mediumBrown,
                                        uncheckedColor = Theme.colors.mediumBrown
                                    )
                                )
                                Text(
                                    text = Theme.strings.setAsDefaultAddress,
                                    style = Theme.typography.title,
                                    color = Theme.colors.black,
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            AnimatedVisibility(visible = state.errorMessage.isNotEmpty()) {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .padding(top = 8.dp),
                                    text = state.errorMessage,
                                    color = Theme.colors.error,
                                    style = Theme.typography.body
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    Log.d("isValidPhoneNumber",isValidPhoneNumber.value.toString())
                                    if (isValidPhoneNumber.value) {
                                    listener.onClickAddAddress()} },
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .padding(bottom = 6.dp)
                                    .fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.mediumBrown),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    text = if (state.editAddress) Theme.strings.confirm else Theme.strings.addAddress,
                                    style = Theme.typography.title,
                                    color = Theme.colors.white
                                )
                            }
                        }
                    }

                } else {
                    LoadingContent()
                }
            }

        }
    }

    @Composable
    private fun EditTextWithDropDownList(
        modifier: Modifier,
        text: String,
        items: List<MenuItemUiState>,
        dropStateForBottomSheetpara: Boolean = false,
        isCountries: Boolean = false,
        isGovernments: Boolean = false,
        isCities: Boolean = false,
        isRegions: Boolean = false,
        countryId: Int = 0,
        governmentId: Int = 0,
        cityId: Int = 0,
        regionId: Int = 0,
        clearText: Boolean = false,
        enabled: Boolean = false,
        isLoading: Boolean = false,
        errorState: ErrorState = ErrorState(),
        onValueChange: (String) -> Unit,
        onclickWhenNotEnabled: () -> Unit,
        onItemClick: (Int, String) -> Unit
    ) {
        var dropDownState by remember { mutableStateOf(false) }
        var dropStateForBottomSheet by remember { mutableStateOf(dropStateForBottomSheetpara) }
        val rotationState by animateFloatAsState(
            targetValue = if (dropDownState||dropStateForBottomSheet) 180f else 0f, label = ""
        )
        var errorMessage by remember { mutableStateOf("") }
        Column {
            Box(modifier = modifier.fillMaxWidth()) {
                OutlinedTextField(
                    enabled = enabled,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    value = if (clearText) "" else text,
                    onValueChange = {
                        dropDownState = true
                        onValueChange(it)
                    },
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Theme.colors.primary,
                        unfocusedIndicatorColor = Theme.colors.whiteTwo,
                        unfocusedContainerColor = Theme.colors.background,
                        disabledContainerColor = Theme.colors.background,
                        disabledIndicatorColor = Theme.colors.whiteTwo,
                        cursorColor = Theme.colors.mediumBrown,
                        selectionColors = TextSelectionColors(
                            handleColor = Theme.colors.mediumBrown,
                            backgroundColor = Theme.colors.darkBeige
                        )
                    ),
                    shape = RoundedCornerShape(10.dp),

                    trailingIcon =  {

                        if (!dropDownState)
                            Box(modifier = Modifier
                                .padding(16.dp)
                                .noRippleEffect {
                                    if (enabled) {
                                        when {
                                            isGovernments -> {
                                                errorMessage =
                                                    if (countryId == 0) "Select Country First." else ""
                                                dropDownState = countryId != 0
                                            }

                                            isCities -> {
                                                errorMessage =
                                                    if (governmentId == 0) "Select Government First." else ""
                                                dropDownState = governmentId != 0
                                            }

                                            isRegions -> {
                                                errorMessage =
                                                    if (cityId == 0) "Select City First." else ""
                                                dropDownState = cityId != 0
                                            }

                                            else -> dropDownState = true
                                        }
                                    } else {
                                        dropStateForBottomSheet = true
                                        onclickWhenNotEnabled()
                                    }
                                }) {
                                Image(
                                    modifier = Modifier
                                        .rotate(rotationState),
                                    painter = painterResource(
                                        id = R.drawable.down_arrow
                                    ),
                                    contentDescription = "Down Arrow",
                                    colorFilter = ColorFilter.tint(Theme.colors.mediumBrown)
                                )
                            }

                   }

                )
                DropdownMenu(
                    modifier = Modifier
                        .background(Theme.colors.transparent)
                        .height(
                            if (items.size > 2) 200.dp else if (items.size == 1) 60.dp else 120.dp
                        )
                        .background(Theme.colors.white)
                        .clip(RoundedCornerShape(12.dp)),
                    scrollState = rememberScrollState(),
                    expanded = dropDownState,
                    properties = PopupProperties(clippingEnabled = false),
                    onDismissRequest = { dropDownState = false },
                ) {
                    items.forEach {
                        AnimatedVisibility(visible = isLoading.not()) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.name,
                                        color = Theme.colors.mediumBrown,
                                        style = Theme.typography.body
                                    )
                                },
                                onClick = {
                                    dropDownState = false
                                    onItemClick(it.id, it.name)
                                }
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    text = errorMessage,
                    color = Theme.colors.error,
                    style = Theme.typography.body
                )
            }
        }

    }

    @SuppressLint("UnrememberedMutableState")
    @Composable
    private fun MiniMap(
        latLng: LatLng,
        context: Context,
        onFullMapScreenClicked: () -> Unit,
        onMapClicked: (LatLng) -> Unit
    ) {

        var zoomState by remember { mutableFloatStateOf(15f) }
        val cameraPositionState = CameraPositionState(
            position = CameraPosition.fromLatLngZoom(
                latLng, zoomState
            )
        )
        LaunchedEffect(zoomState) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition.fromLatLngZoom(
                        latLng,
                        zoomState
                    )
                )
            )
        }
        Box(modifier = Modifier.fillMaxHeight(0.25f)) {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapClick = { latLng ->
                    onMapClicked(latLng)
                },
                uiSettings = MapUiSettings(zoomControlsEnabled = false)
            ) {
                DhaibanMapMarker(
                    context = context,
                    position = latLng,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp, top = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .noRippleEffect {
                            onFullMapScreenClicked()
                        },
                    imageVector = ImageVector.vectorResource(id = R.drawable.full_map_icon),
                    contentDescription = ""
                )
                Box(
                    modifier = Modifier
                        .fillMaxHeight(0.70f)
                        .width(50.dp)
                        .background(color = Theme.colors.white, shape = RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .noRippleEffect {
                                    if (zoomState < 21)
                                        zoomState += 1f
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.plus_icon),
                                contentDescription = ""
                            )
                        }

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .weight(0.1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(90.dp)
                                    .height(2.dp)
                                    .padding(horizontal = 6.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Theme.colors.silverGray)
                            )
                        }

                        Box(
                            Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .noRippleEffect {
                                    if (zoomState > 3)
                                        zoomState -= 1f
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                imageVector = ImageVector.vectorResource(id = R.drawable.minus_icon),
                                contentDescription = ""
                            )
                        }

                    }
                }
            }

        }
    }

    @Composable
    private fun LoadingContent() {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.25f)
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(50.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(50.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(50.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(50.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(50.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(50.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(50.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .width(90.dp)
                            .height(45.dp)
                            .shimmerEffect()
                    )
                    Box(
                        Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .width(90.dp)
                            .height(45.dp)
                            .shimmerEffect()
                    )
                }
            }
        }
    }



    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun AddAddressScreenContentPrev() {
        val dummyListener = object : AddAddressScreenInteractionListener {
            override fun onClickUpButton() {}
            override fun onClickMap(latLng: LatLng) {}
            override fun onChooseCountry(countryId: Int, countryName: String) {}
            override fun onChooseGovernment(governmentId: Int, governmentName: String) {}
            override fun onChooseCity(cityId: Int, cityName: String) {}
            override fun onChooseRegion(regionId: Int, regionName: String) {}
            override fun onClickFullMapScreen(latLng: LatLng) {}
            override fun onAddressValueChanged(addressName: String) {}
            override fun onDetailedAddressValueChanged(detailedAddress: String) {}
            override fun onPostalCodeValueChanged(postalCode: String) {}
            override fun onPhoneNumberValueChanged(phoneNumber: String) {}
            override fun onPhoneCodeNumberValueChanged(codephone: String) {
                TODO("Not yet implemented")
            }

            override fun onAddressTypeValueChanged(addressType: AddressType) {}
            override fun onDefaultAddressValueChanged(defaultState: Boolean) {}
            override fun onClickAddAddress() {}
            override fun onCountryValueChanged(country: String) {}
            override fun onGovernmentValueChanged(government: String) {}
            override fun onCityValueChanged(city: String) {}
            override fun onRegionValueChanged(region: String) {}
        }
        DhaibanTheme {
            AddAddressScreenContent(
                context = LocalContext.current,
                state = AddAddressScreenUiState(),
                listener = dummyListener,
                listOf(),
                listOf(),
                listOf(),
                listOf(),
            )
        }
    }