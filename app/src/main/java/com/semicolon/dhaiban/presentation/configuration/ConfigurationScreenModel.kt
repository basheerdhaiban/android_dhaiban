package com.semicolon.dhaiban.presentation.configuration

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.dhaiban.presentation.sharedUiState.toUiState
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.utils.BadRequestException
import com.semicolon.domain.utils.UserConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfigurationScreenModel(
    private val localConfigurationUseCase: LocalConfigurationUseCase,
    private val authentication: UserAuthenticationUseCase,
) :
    BaseScreenModel<ConfigurationUiState, ConfigurationUiEffect>(
        ConfigurationUiState()
    ), ConfigurationScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _query = MutableStateFlow(_state.value.queryValue)
    private val query = _query.asStateFlow()

    private val _languages = MutableStateFlow(_state.value.appConfig.languages)
    val languages = query.combine(_languages) { text, languages ->
        if (text.isBlank()) {
            languages
        } else {
            languages.filter {
                it.doesMatchSearchQuery(text)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _languages.value)

    private val _countries = MutableStateFlow(_state.value.appConfig.countries)
    val countries = query.combine(_countries) { text, countries ->
        if (text.isBlank()) {
            countries
        } else {
            countries.filter {
                it.doesMatchSearchQuery(text)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _countries.value)

    private val _currencies = MutableStateFlow(_state.value.appConfig.currencies)
    val currencies = query.combine(_currencies) { text, currencies ->
        if (text.isBlank()) {
            currencies
        } else {
            currencies.filter {
                it.doesMatchSearchQuery(text)
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), _currencies.value)


    init {
        getOnBoardingState()
        getData()
    }

    private fun getOnBoardingState() {
        tryToExecute(
            function = { localConfigurationUseCase.readOnBoardingState() },
            onSuccess = { onBoardingState ->
                _state.update { it.copy(onBoardingState = onBoardingState) }
            }, onError = {}
        )
    }

    override fun onClickCountryButton() {
        _state.update {
            it.copy(
                showBottomSheet = true,
                userConfig = UserConfig.COUNTRY,
                queryValue = ""
            )
        }
    }

    override fun onClickLanguageButton() {
        _state.update {
            it.copy(
                showBottomSheet = true,
                userConfig = UserConfig.LANGUAGE,
                queryValue = ""
            )
        }
    }

    override fun onClickCurrencyButton() {
        _state.update {
            it.copy(
                showBottomSheet = true,
                userConfig = UserConfig.CURRENCY,
                queryValue = ""
            )
        }
    }

    override fun onDismissBottomSheet() {
        _state.update { it.copy(showBottomSheet = false) }
    }

    override fun onQueryValueChanged(query: String) {
        _state.update { it.copy(queryValue = query) }
        _query.value = query
    }

    override fun onItemSelected(item: String, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            when (_state.value.userConfig) {
                UserConfig.COUNTRY -> {
                    _state.update { it.copy(selectedCountry = item) }
                }

                UserConfig.LANGUAGE -> {
                    _state.update { it.copy(selectedLanguage = item) }
                }

                UserConfig.CURRENCY -> {
                    _state.update {
                        it.copy(selectedCurrency = item, selectedCurrencyId = id)
                    }
                }
            }
        }
    }

    override fun onClickConfirm() {
        viewModelScope.launch(Dispatchers.IO) {
            when (_state.value.userConfig) {
                UserConfig.COUNTRY -> {
                    val countryCode =
                        state.value.appConfig.countries.find { it.title == _state.value.selectedCountry }?.code
                            ?: ""
                    val countryId =
                        state.value.appConfig.countries.find { it.title == _state.value.selectedCountry }?.id
                            ?: 0
                    localConfigurationUseCase.saveUserCountry(countryCode)
                    localConfigurationUseCase.saveCountryId(countryId)
                }

                UserConfig.LANGUAGE -> {
                    val languageCode =
                        _state.value.appConfig.languages.find { it.title == _state.value.selectedLanguage }?.code
                            ?: ""
                    val layoutDirection =
                        _state.value.appConfig.languages.find { it.title == _state.value.selectedLanguage }?.dir
                            ?: ""
                    saveLocalStringFile(languageCode)
                    localConfigurationUseCase.saveUserLanguage(languageCode)
                    localConfigurationUseCase.saveLayoutDirection(layoutDirection)
                }

                UserConfig.CURRENCY -> {

                    val currencycode =
                        state.value.appConfig.currencies.find { it.name == _state.value.selectedCurrency }?.code
                            ?: ""
                    Log.d("currenyNameconfigScreenmonModelscreenss",currencycode)

                    val currencySymbol =
                        state.value.appConfig.currencies.find { it.name == _state.value.selectedCurrency }?.symbol
                            ?: ""
                    val currencyId =
                        state.value.appConfig.currencies.find { it.name == _state.value.selectedCurrency }?.id
                            ?: 0
                    localConfigurationUseCase.saveUserCurrency(currencySymbol)
                    localConfigurationUseCase.saveNameUserCurrency(currencycode)
                    localConfigurationUseCase.saveCurrencyId(currencyId)
                    sendNewEffect(ConfigurationUiEffect.OnUpdateCurrencySymbol(currencySymbol))
                    sendNewEffect(ConfigurationUiEffect.OnUpdateCurrency(_state.value.selectedCurrencyId))
                }
            }
            _state.update { it.copy(showBottomSheet = false) }
        }
    }

    override fun onClickCancel() {
        _state.update { it.copy(showBottomSheet = false) }
    }

    override fun onClickConfirmButton() {
        saveConfiguration()
        sendNewEffect(ConfigurationUiEffect.OnNavigateToOnBoardingScreen)
    }

    override fun onClickSkipButton() {
//        saveConfiguration()
        sendNewEffect(ConfigurationUiEffect.OnNavigateToOnBoardingScreen)
    }

    private fun saveConfiguration() {
        viewModelScope.launch(Dispatchers.IO) {
            val selectedLanguageCode = localConfigurationUseCase.getUserLanguage()
            val selectedCurrencySymbol = localConfigurationUseCase.getUserCurrency()

            val defaultLanguageCode = _state.value.defaultLangCode.ifEmpty { "en" }
            val defaultCurrency = _state.value.defaultCurrencySymbol
            val defaultLayoutDirection = _state.value.layoutDirection

            saveLocalStringFile(selectedLanguageCode.ifEmpty { defaultLanguageCode })

            localConfigurationUseCase.saveUserLanguage(selectedLanguageCode.ifEmpty { defaultLanguageCode })
            localConfigurationUseCase.saveUserCurrency(selectedCurrencySymbol.ifEmpty { defaultCurrency })
            if (selectedLanguageCode.isEmpty()) {
                localConfigurationUseCase.saveLayoutDirection(defaultLayoutDirection)
            }
        }
    }

    private fun saveLocalStringFile(langCode: String) {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            function = { localConfigurationUseCase.saveLocalizationLanguage("", langCode) },
            onSuccess = ::onSaveLocalStringFileSuccess,
            onError = ::onSaveLocalStringFileError
        )
    }

    private fun onSaveLocalStringFileSuccess(unit: Unit) {
        updateState { it.copy(isLoading = false) }
    }

    private fun onSaveLocalStringFileError(exception: Exception) {
        updateState { it.copy(isLoading = false) }
        when (exception is BadRequestException) {
            true -> sendNewEffect(ConfigurationUiEffect.ShowNetworkError)
            false -> sendNewEffect(ConfigurationUiEffect.ShowNetworkError)
        }
    }

    private fun getData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            function = { authentication.getAppConfig().toUiState() },
            onSuccess = ::onGetListSuccess,
            onError = ::onError
        )
    }

    private fun onGetListSuccess(appConfig: AppConfigUiState) {
        updateState {
            it.copy(
                appConfig = appConfig,
                isLoading = false
            )
        }
        _countries.value = appConfig.countries
        _languages.value = appConfig.languages
        _currencies.value = appConfig.currencies
    }

    private fun onError(exception: Exception) {
        updateState { it.copy(isLoading = false) }
    }
}