package com.semicolon.dhaiban.presentation.profile

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.toStringResource
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.chat.ChatUiEffect
import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.dhaiban.presentation.sharedUiState.toUiState
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageConnectivityUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.utils.BadRequestException
import com.semicolon.domain.utils.InternetException
import com.semicolon.domain.utils.UserConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileScreenModel(
    private val localConfigurationUseCase: LocalConfigurationUseCase,
    private val authentication: UserAuthenticationUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase,
    private val manageNetwork: ManageConnectivityUseCase,
    private val userConfiguration: LocalConfigurationUseCase,
) : BaseScreenModel<ProfileScreenUiState, ProfileScreenUiEffect>(
    ProfileScreenUiState()
), ProfileScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _query = MutableStateFlow(_state.value.queryValue)
    private val query = _query.asStateFlow()
    private val _isNetworkAvailable = MutableStateFlow<Boolean>(false)
    val isNetworkAvailable = _isNetworkAvailable.asStateFlow()
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

    private fun getCountOfUnreadNotification() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageNotificationUseCase.getUnReadNotificationCount() },
            onSuccess = {
                if (it != null) {
                    _state.update { screenUiState ->
                        screenUiState.copy(
                            isLoading = false,
                            countOfUnreadMessage = it.notifications
                        )
                    }
                }

            },
            onError = {},
        )
    }


    private fun checkNetwork() {
        viewModelScope.launch {
            _isNetworkAvailable.value = manageNetwork.isConnected()
        }
    }
    override fun tryToConnect() {
        getDefaultData()
        sendNewEffect(ProfileScreenUiEffect.NavigateToTryToConnect)

    }
    init {
        getUserData()
        getData()
        getCountOfUnreadNotification()
        checkNetwork()
    }

    private fun onError(exception: Exception) {
        _state.update { it.copy(isLoading = false, errorMessage = "Network Error") }
    }

    fun getUserData() {
        tryToExecute(
            function = {
                UserDataUiState(
                    username = localConfigurationUseCase.getUserName(),
                    imageUrl = localConfigurationUseCase.getImageUrl(),
                    isAuthenticated = localConfigurationUseCase.getUserToken().isNotEmpty()
                )
            },
            onSuccess = ::onGetUserDataSuccess,
            onError = ::onError
        )
    }

    private fun getData() {
        updateState { it.copy(isLoading = true) }
        tryToExecute(
            function = { authentication.getAppConfig().toUiState() },
            onSuccess = ::onGetListSuccess,
            onError = ::onError
        )
    }



    private fun saveLocalStringFile(langCode: String) {
        tryToExecute(
            function = { localConfigurationUseCase.saveLocalizationLanguage("", langCode) },
            onSuccess = {},
            onError = {}
        )
    }
    private fun getLatestStringFile() {
        viewModelScope.launch(Dispatchers.IO) {
            userConfiguration.getStrings().collectLatest { strings ->
                _state.update {
                    it.copy(
                        stringRes = strings.toStringResource(),
                        isLoading = false,
                        layoutDirection = userConfiguration.getLayoutDirection()
                    )
                }
            }
        }
    }
    private fun onGetUserDataSuccess(userData: UserDataUiState) {
        _state.update {
            it.copy(
                userData = UserDataUiState(
                    username = userData.username.ifEmpty { "Guest" },
                    imageUrl = userData.imageUrl,
                    isAuthenticated = userData.isAuthenticated
                )
            )
        }
    }
    private fun saveLocalStringFile(url: String, langCode: String) {
        tryToExecute(
            function = { userConfiguration.saveLocalizationLanguage(url, langCode) },
            onSuccess = ::onSaveLocalStringFileSuccess,
            onError = {}
        )
    }
    private fun onSaveLocalStringFileSuccess(unit: Unit) {
        getLatestStringFile()
        viewModelScope.launch { userConfiguration.saveStringResourceState(true) }
    }


     fun getDefaultData() {
        tryToExecute(
            function = { authentication.getAppConfig().toUiState() },
            onSuccess = ::onGetListSuccess,
            onError = ::onError
        )
    }
    private fun initiateLocalStrings() {

        _state.update { it.copy(isLoading = true) }

        screenModelScope.launch(Dispatchers.IO) {
            val currentLangCode = "ltr"
//                async { userConfiguration.getUserLanguage() }.await()
            val currentLayoutDirection =
                async { userConfiguration.getLayoutDirection() }.await()
            Log.d("currentLayoutDirectionf",currentLayoutDirection)

            val defaultLangCode =
                _state.value.appConfig.languages.find { it.id == _state.value.appConfig.defaultLang.id }?.code
                    ?: "en"
            val defaultLanguageUrl =
                _state.value.appConfig.defaultLang.url
            val defaultLayoutDirection = "ltr"
//                _state.value.appConfig.languages.find { it.id == _state.value.appConfig.defaultLang.id }?.dir
//                    ?: "ltr"
            Log.d("currentLayoutDirectionl",defaultLayoutDirection)
            val appConfig = _state.value.appConfig
            Log.d("appConfig", appConfig.toString())
            Log.d("defaultLangId", appConfig.defaultLang.id.toString())

// Log the languages list
            appConfig.languages.forEach { lang ->
                Log.d("language", "id: ${lang.id}, dir: ${lang.dir}")
            }
            val isStringResSaved = async { userConfiguration.readStringResourceState() }

            if (isStringResSaved.await()) {
                getLatestStringFile()
            } else {
                saveLocalStringFile(
                    url = defaultLanguageUrl,
                    langCode = currentLangCode.ifEmpty { defaultLangCode })
            }
            userConfiguration.saveLayoutDirection(currentLayoutDirection.ifEmpty { defaultLayoutDirection })
            _state.update {
                it.copy(
                    layoutDirection = currentLayoutDirection.ifEmpty { defaultLayoutDirection },
                )
            }
        }
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

        getUserDefaults()
    }
    private fun getUserDefaults() {
        tryToExecute(
            function = {
                DefaultPreferences(
                    language = localConfigurationUseCase.getUserLanguage(),
                    country = localConfigurationUseCase.getUserCountry(),
                    currency = localConfigurationUseCase.getUserCurrency()
                )
            },
            onSuccess = { defaultPreferences ->
                Log.e("defaultPreferences", defaultPreferences.toString())
                _state.update { uiState ->
                    uiState.copy(
                        selectedLanguage = _state.value.appConfig.languages
                            .firstOrNull { it.code == defaultPreferences.language }?.title
                            ?: "",
                        selectedCountry = _state.value.appConfig.countries
                            .firstOrNull { it.code == defaultPreferences.country }?.title ?: "",
                        selectedCurrency = _state.value.appConfig.currencies
                            .firstOrNull { it.symbol == defaultPreferences.currency }?.name
                            ?: "",
                    )
                }
                Log.e("selectedLanguage", _state.value.selectedLanguage)
            }, onError = {}
        )
    }

    private fun showLoginDialog() {
        _state.update { it.copy(loginDialogState = true) }
    }

    private fun dismissLoginDialog() {
        _state.update { it.copy(loginDialogState = false) }
    }


    override fun onClickFavoritesButton() {
        if (state.value.userData.isAuthenticated) {
            onDismissLoginDialog()
            sendNewEffect(ProfileScreenUiEffect.OnNavigateToFavoritesScreen)
        } else {
            showLoginDialog()
        }
    }

    override fun onClickOrdersButton() {
        if (state.value.userData.isAuthenticated) {
            onDismissLoginDialog()
            sendNewEffect(ProfileScreenUiEffect.OnNavigateToOrdersScreen)
        } else {
            showLoginDialog()
        }
    }

    override fun onClickLogout() {
        _state.update { it.copy(logOutExpandState = true) }
    }

    override fun onDismissDialog() {
        _state.update { it.copy(logOutExpandState = false) }
    }

    override fun onConfirmLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            localConfigurationUseCase.saveUserName("")
            localConfigurationUseCase.clearUserToken("")
            localConfigurationUseCase.saveEmail("")
            localConfigurationUseCase.saveContactNumber("")

            _state.update {
                it.copy(
                    userData = UserDataUiState("", "", false),
                    logOutExpandState = false
                )
            }
        }
        sendNewEffect(ProfileScreenUiEffect.OnLogoutSuccess)
    }

    override fun onMyProfileClick() {
        sendNewEffect(ProfileScreenUiEffect.OnNavigateToMyProfileScreen)
    }

    override fun onLanguageClick() {
        _state.update {
            it.copy(
                showBottomSheet = true,
                userConfig = UserConfig.LANGUAGE,
                queryValue = ""
            )
        }
    }

    override fun onCountryClick() {
        _state.update {
            it.copy(
                showBottomSheet = true,
                userConfig = UserConfig.COUNTRY,
                queryValue = ""
            )
        }
    }

    override fun onCurrencyClick() {
        _state.update {
            it.copy(
                showBottomSheet = true,
                userConfig = UserConfig.CURRENCY,
                queryValue = ""
            )
        }
    }

    override fun onDismissBottomSheet() {
        _state.update { it.copy(selectedCountry =  state.value.unChangedSelectedCountry, selectedCurrency = state.value.unChangedSelectedCurrency, selectedLanguage =  state.value.unChangedSelectedLanguage) }

        _state.update { it.copy(showBottomSheet = false, queryValue = "") }
        _query.value = ""
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
                    _state.update { it.copy(selectedCurrency = item, selectedCurrencyId = id) }
                }
            }
        }
    }

    override fun onClickConfirm() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(
                unChangedSelectedCountry =  state.value.selectedCountry,
                unChangedSelectedCurrency = state.value.selectedCurrency,
                unChangedSelectedLanguage =  state.value.selectedLanguage) }

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
                    val layoutDirection = if (languageCode == "ar") "rtl" else "ltr"
//                    val layoutDirection =
//                        _state.value.appConfig.languages.find { it.title == _state.value.selectedLanguage }?.dir
//                            ?: ""

                    saveLocalStringFile(languageCode)
                    localConfigurationUseCase.saveUserLanguage(languageCode)
                    localConfigurationUseCase.saveLayoutDirection(layoutDirection)
                }

                UserConfig.CURRENCY -> {
                    val currenyName= state.value.appConfig.currencies.find { it.name == _state.value.selectedCurrency }?.code
                        ?: ""
                    Log.d("currenyNameprfoilescreen",currenyName)
                    val currencySymbol =
                        state.value.appConfig.currencies.find { it.name == _state.value.selectedCurrency }?.symbol
                            ?: ""
                    val currencyId =
                        state.value.appConfig.currencies.find { it.name == _state.value.selectedCurrency }?.id
                            ?: 0
                    async {
                        localConfigurationUseCase.saveUserCurrency(currencySymbol)
                        localConfigurationUseCase.saveCurrencyId(currencyId)
                        localConfigurationUseCase.saveNameUserCurrency(currenyName)
                    }.await()

                    sendNewEffect(ProfileScreenUiEffect.OnUpdateCurrency(_state.value.selectedCurrencyId))
                }
            }
            _query.value = ""
            _state.update { it.copy(showBottomSheet = false, queryValue = "") }
        }
    }

    override fun onClickCancel() {
        _state.update { it.copy(selectedCountry =  state.value.unChangedSelectedCountry, selectedCurrency = state.value.unChangedSelectedCurrency, selectedLanguage =  state.value.unChangedSelectedLanguage) }

        _state.update { it.copy(showBottomSheet = false) }
        _query.value = ""
    }


    override fun onClickAddress() {
        sendNewEffect(ProfileScreenUiEffect.OnNavigateToAddressScreen)
    }

    override fun onClickChatRoom() {
        sendNewEffect(ProfileScreenUiEffect.OnNavigateTochatRoom)
    }

    override fun onClickSearch() {
        sendNewEffect(ProfileScreenUiEffect.OnNavigateToSearchScreen)
    }

    override fun onClickLogin() {
        sendNewEffect(ProfileScreenUiEffect.OnNavigateToLogin)
    }

    override fun onClickContactUs() {
        sendNewEffect(ProfileScreenUiEffect.OnNavigateToContactUs)
    }

    override fun onClickFaq() {
        sendNewEffect(ProfileScreenUiEffect.OnNavigateToFaq)
    }

    override fun onClickDeleteAccount() {
        _state.update { it.copy(showDeleteAccountDialog = true) }
    }

    override fun onDismissDeleteDialog() {
        _state.update { it.copy(showDeleteAccountDialog = false) }
    }

    override fun onConfirmDeleteAccount() {
        viewModelScope.launch(Dispatchers.IO) {
            localConfigurationUseCase.saveUserName("")
            localConfigurationUseCase.clearUserToken("")
            _state.update {
                it.copy(
                    showDeleteAccountDialog = false
                )
            }
        }
        sendNewEffect(ProfileScreenUiEffect.OnDeleteAccountSuccess)
    }

    override fun onClickRefund() {
        if (state.value.userData.isAuthenticated) {
            onDismissLoginDialog()
            sendNewEffect(ProfileScreenUiEffect.OnNavigateToRefundScreen)
        } else {
            showLoginDialog()
        }
    }

    override fun onClickWallet() {
        if (state.value.userData.isAuthenticated) {
            onDismissLoginDialog()
            sendNewEffect(ProfileScreenUiEffect.OnNavigateToWalletScreen)
        } else {
            showLoginDialog()
        }
    }

    override fun onDismissLoginDialog() {
        _state.update { it.copy(loginDialogState = false) }
    }

    override fun onClickNotification() {
        sendNewEffect(ProfileScreenUiEffect.OnNavigateToNotificationScreen)
    }
}