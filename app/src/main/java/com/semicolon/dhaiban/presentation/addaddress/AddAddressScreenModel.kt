package com.semicolon.dhaiban.presentation.addaddress

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.google.android.gms.maps.model.LatLng
import com.semicolon.dhaiban.presentation.address.AddressState
import com.semicolon.dhaiban.presentation.address.AddressUiState
import com.semicolon.dhaiban.presentation.address.toAddressModel
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.AddressModel
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.entity.City
import com.semicolon.domain.entity.Province
import com.semicolon.domain.entity.Region
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.LocationUseCase
import com.semicolon.domain.usecase.ManageAddressUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddAddressScreenModel(
    private val latLng: LatLng,
    private val addressName: String,
    private val detailedAddress: String,
    private val postalCode: String,
    private val addressUiState: AddressUiState,
    private val manageAddressUseCase: ManageAddressUseCase,
    private val localConfigurationUseCase: LocalConfigurationUseCase,
    private val locationUseCase: LocationUseCase
) :
    BaseScreenModel<AddAddressScreenUiState, AddAddressScreenUiEffect>(
        AddAddressScreenUiState()
    ), AddAddressScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _countryQuery = MutableStateFlow(_state.value.country)
    private val countryQuery = _countryQuery.asStateFlow()

    private val _governmentQuery = MutableStateFlow(_state.value.government)
    private val governmentQuery = _governmentQuery.asStateFlow()

    private val _cityQuery = MutableStateFlow(_state.value.city)
    private val cityQuery = _cityQuery.asStateFlow()

    private val _regionQuery = MutableStateFlow(_state.value.region)
    private val regionQuery = _regionQuery.asStateFlow()

    private val _countries = MutableStateFlow(_state.value.countries)
    val countries =_countries.asStateFlow()

    private val _governments = MutableStateFlow(_state.value.governments)
    val governments = _governments.asStateFlow()

    private val _cites = MutableStateFlow(_state.value.cities)
    val cities =    _cites.asStateFlow()


    private val _regions = MutableStateFlow(_state.value.regions)
    val regions =  _regions.asStateFlow()
    init {
        if (latLng.longitude != 0.0 && latLng.latitude != 0.0 && addressUiState.address == "" && addressUiState.id == 0) {
            _state.update {
                it.copy(
                    latLng = latLng,
                    addressName = addressName,
                    detailedAddress = detailedAddress,
                    postalCode = postalCode
                )
            }
        } else if (latLng.longitude != 0.0 && latLng.latitude != 0.0 && addressUiState.address != "" && addressUiState.id != 0) {
            _state.update {
                it.copy(
                    latLng = LatLng(addressUiState.lat, addressUiState.lon),
                    addressName = addressName,
                    detailedAddress = detailedAddress,
                    postalCode = postalCode,
                    addressUiState = addressUiState,
                    contactNumber = addressUiState.phone,
                    addressType = if (addressUiState.workHome == "work") AddressType.WORK else
                        AddressType.HOME,
                    defaultAddress = when (addressUiState.defaultAddress) {
                        AddressState.DEFAULT -> true
                        AddressState.NOT_DEFAULT -> false
                    }
                )
            }
        } else {
            getLastLocation()
        }
        getCountryId()
        getAvailableCountries()
    }

    private fun getAvailableCountries() {
        _state.update { it.copy(countriesLoading = true) }
        tryToExecute(
            function = { manageAddressUseCase.getAvailableCountries() },
            onSuccess = ::onGetAvailableCountries,
            onError = {}
        )
    }

    private fun getGovernmentsByCountryId(countryId: Int) {
        _state.update { it.copy(governmentsLoading = true) }
        tryToExecute(
            function = { manageAddressUseCase.getGovernmentsByCountryId(countryId) },
            onSuccess = ::onGetGovernmentsSuccess,
            onError = {}
        )
    }

    private fun getCitiesByGovernmentId(governmentId: Int) {
        _state.update { it.copy(citesLoading = true) }
        tryToExecute(
            function = { manageAddressUseCase.getCitiesByGovernment(governmentId) },
            onSuccess = ::onGetCitiesSuccess,
            onError = {}
        )
    }

    private fun getRegionsByCityId(cityId: Int) {
        _state.update { it.copy(regionsLoading = true) }
        tryToExecute(
            function = { manageAddressUseCase.getRegionsByCity(cityId) },
            onSuccess = ::onGetRegionsSuccess,
            onError = {}
        )
    }

    private fun addAddress() {
        tryToExecute(
            function = { manageAddressUseCase.addAddress(_state.value.toAddressModel()) },
            onSuccess = {
                if (it) {
                    sendNewEffect(AddAddressScreenUiEffect.OnSendMessage("Added Successfully"))
                }
            },
            onError = {
                Log.e("AddAddressError", it.message.toString())
            }
        )
    }

    private fun getLastLocation() {
        viewModelScope.launch {
            locationUseCase().take(1).collectLatest { latLng ->
                latLng?.let {
                    _state.update { uiState ->
                        uiState.copy(
                            latLng = LatLng(
                                latLng.first,
                                latLng.second
                            )
                        )
                    }
                }
            }
        }
    }

    private fun getCountryId() {
        tryToExecute(
            function = { localConfigurationUseCase.getCountryId() },
            onSuccess = { countryId ->
                if (countryId != 0) {
                    _state.update { it.copy(selectedCountry = countryId) }
                    getGovernmentsByCountryId(countryId)
                }
            },
            onError = {}
        )
    }

    private fun saveCountryId(countryId: Int) {
        tryToExecute(
            function = { localConfigurationUseCase.saveCountryId(countryId) },
            onSuccess = {},
            onError = {}
        )
    }

    private fun editAddress(addressModel: AddressModel) {
        tryToExecute(
            function = { manageAddressUseCase.editAddress(addressModel) },
            onSuccess = ::onEditAddressSuccess,
            onError = { Log.e("Edit error", it.message.toString()) }
        )
    }

    override fun onClickUpButton() {
        sendNewEffect(AddAddressScreenUiEffect.OnNavigateBack)
    }

    override fun onClickMap(latLng: LatLng) {
        _state.update { it.copy(latLng = latLng) }
    }

    override fun onClickFullMapScreen(latLng: LatLng) {
        _state.update { it.copy(addressName = "", detailedAddress = "", postalCode = "") }
        sendNewEffect(AddAddressScreenUiEffect.OnNavigateToMap(latLng))
    }

    override fun onChooseCountry(countryId: Int, countryName: String) {
        saveCountryId(countryId)
        getGovernmentsByCountryId(countryId)
        _state.update {
            it.copy(
                selectedCountry = countryId,
                country = countryName,
                selectedGovernment = 0,
                selectedCity = 0,
                clearGovCityRegion = true
            )
        }
    }

    override fun onChooseGovernment(governmentId: Int, governmentName: String) {
        getCitiesByGovernmentId(governmentId)
        _state.update {
            it.copy(
                selectedGovernment = governmentId,
                government = governmentName,
                selectedCity = 0,
                clearCity = true,
                clearCityRegion = true,
                clearGovCityRegion = false
            )
        }
        if (_state.value.editAddress)
            _state.update { it.copy(addressUiState = it.addressUiState.copy(provinceId = governmentId)) }
    }

    override fun onChooseCity(cityId: Int, cityName: String) {
        getRegionsByCityId(cityId)
        _state.update {
            it.copy(
                selectedCity = cityId,
                city = cityName,
                selectedRegion = 0,
                clearRegion = true,
                clearCityRegion = false,
                clearCity = false
            )
        }
        if (_state.value.editAddress)
            _state.update { it.copy(addressUiState = it.addressUiState.copy(cityId = cityId)) }
    }

    override fun onChooseRegion(regionId: Int, regionName: String) {
        _state.update {
            it.copy(
                selectedRegion = regionId,
                region = regionName,
                clearRegion = false
            )
        }
        if (_state.value.editAddress)
            _state.update { it.copy(addressUiState = it.addressUiState.copy(regionId = regionId)) }
    }

    override fun onAddressValueChanged(addressName: String) {
        if (_state.value.editAddress)
            _state.update {
                it.copy(
                    addressUiState = it.addressUiState.copy(name = addressName),
                    addressName = addressName
                )
            }
        else
            _state.update { it.copy(addressName = addressName) }
    }

    override fun onDetailedAddressValueChanged(detailedAddress: String) {
        if (_state.value.editAddress)
            _state.update {
                it.copy(
                    addressUiState = it.addressUiState.copy(address = detailedAddress),
                    detailedAddress = detailedAddress
                )
            }
        else
            _state.update { it.copy(detailedAddress = detailedAddress) }
    }

    override fun onCountryValueChanged(country: String) {
        _state.update { it.copy(country = country) }
        _countryQuery.value = country
    }

    override fun onGovernmentValueChanged(government: String) {
        _state.update {
            it.copy(
                government = government,
                selectedGovernment = 0,
                clearGovCityRegion = false
            )
        }
        _governmentQuery.value = government
    }

    override fun onCityValueChanged(city: String) {
        _state.update {
            it.copy(
                city = city,
                selectedCity = 0,
                clearCityRegion = false,
                clearCity = false,
                clearRegion = false
            )
        }
        _cityQuery.value = city
    }

    override fun onRegionValueChanged(region: String) {
        _state.update {
            it.copy(
                region = region,
                selectedRegion = 0,
                clearCityRegion = false,
                clearCity = false,
                clearRegion = false
            )
        }
        _regionQuery.value = region
    }

    override fun onPostalCodeValueChanged(postalCode: String) {
        if (_state.value.editAddress)
            _state.update {
                it.copy(
                    addressUiState = it.addressUiState.copy(postalCode = postalCode),
                    postalCode = postalCode
                )
            }
        else
            _state.update { it.copy(postalCode = postalCode) }
    }

    override fun onPhoneNumberValueChanged(phoneNumber: String) {
        if (_state.value.editAddress)
            _state.update {
                it.copy(
                    addressUiState = it.addressUiState.copy(phone = phoneNumber),
                    contactNumber = phoneNumber
                )
            }
        else
            _state.update { it.copy(contactNumber = phoneNumber) }
    }

    override fun onPhoneCodeNumberValueChanged(codephone: String) {
        if (_state.value.editAddress)
            _state.update {
                it.copy(
                    countryCode = codephone,

                )
            }
        else
            _state.update { it.copy(countryCode = codephone) }
    }

    override fun onAddressTypeValueChanged(addressType: AddressType) {
        if (_state.value.editAddress) {
            _state.update {
                it.copy(
                    addressUiState = it.addressUiState.copy(
                        workHome =
                        if (addressType == AddressType.HOME)
                            "home"
                        else
                            "work"
                    )
                )
            }
        } else {
            _state.update { it.copy(addressType = addressType) }
        }

    }

    override fun onDefaultAddressValueChanged(defaultState: Boolean) {
        if (_state.value.editAddress) {
            _state.update {
                it.copy(
                    addressUiState = it.addressUiState.copy(
                        defaultAddress =
                        if (defaultState)
                            AddressState.DEFAULT
                        else
                            AddressState.NOT_DEFAULT
                    )
                )
            }
        } else {
            _state.update { it.copy(defaultAddress = defaultState) }
        }
    }

    override fun onClickAddAddress() {
        if (_state.value.errorState.cantAddAddress) {
            _state.update { it.copy(errorMessage = "Fill All Fields.") }
        } else {
            _state.update { it.copy(errorMessage = "") }
            if (_state.value.editAddress) {
                val currentAddressUiState = _state.value.addressUiState
                _state.update {
                    it.copy(
                        addressName = currentAddressUiState.name,
                        detailedAddress = currentAddressUiState.address,
                        selectedGovernment = currentAddressUiState.provinceId,
                        selectedCity = currentAddressUiState.cityId,
                        selectedRegion = currentAddressUiState.regionId,
                        postalCode = currentAddressUiState.postalCode,
                        contactNumber = currentAddressUiState.phone
                    )
                }
                editAddress(_state.value.addressUiState.toAddressModel())
            } else {
                addAddress()
            }
        }
    }

    private fun onGetAvailableCountries(countries: List<AppConfig.Country>) {
        _state.update { uiState ->
            uiState.copy(
                countries = countries.map { it.toMenuItemUiState() },
                countriesLoading = false,
                country = countries.find { it.id == _state.value.selectedCountry }?.title ?: "",
                clearGovCityRegion = false,
            )
        }
        _countries.value = countries.map { it.toMenuItemUiState() }
    }

    private fun onGetGovernmentsSuccess(governments: List<Province>) {
        _state.update { uiState ->
            uiState.copy(
                governments = governments.map { it.toMenuItemUiState() },
                governmentsLoading = false
            )
        }
        if (_state.value.editAddress && _state.value.setInitialAddressData) {
            getCitiesByGovernmentId(addressUiState.provinceId)
            _state.update {
                it.copy(
                    selectedGovernment = addressUiState.provinceId,
                    government = addressUiState.provinceName,
                    selectedCity = 0,
                    clearCity = false,
                    clearGovCityRegion = false,
                    clearCityRegion = false
                )
            }
        }
        _governments.value = governments.map { it.toMenuItemUiState() }
    }

    private fun onGetCitiesSuccess(cities: List<City>) {
        _state.update { uiState ->
            uiState.copy(
                cities = cities.map { it.toMenuItemUiState() },
                citesLoading = false
            )
        }
        if (_state.value.editAddress && _state.value.setInitialAddressData) {
            getRegionsByCityId(addressUiState.cityId)
            _state.update {
                it.copy(
                    selectedCity = addressUiState.cityId,
                    city = addressUiState.cityName,
                    clearRegion = false,
                    clearCity = false
                )
            }
        }
        _cites.value = cities.map { it.toMenuItemUiState() }
    }

    private fun onGetRegionsSuccess(regions: List<Region>) {
        _state.update { uiState ->
            uiState.copy(
                regions = regions.map { it.toMenuItemUiState() },
                regionsLoading = false
            )
        }
        if (_state.value.editAddress && _state.value.setInitialAddressData) {
            _state.update {
                it.copy(
                    selectedRegion = addressUiState.regionId,
                    region = addressUiState.regionName,
                    clearRegion = false,
                    setInitialAddressData = false
                )
            }
        }
        _regions.value = regions.map { it.toMenuItemUiState() }
    }

    private fun onEditAddressSuccess(addresses: List<AddressModel>) {
        sendNewEffect(AddAddressScreenUiEffect.OnNavigateBack)
    }
}