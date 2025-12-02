package com.semicolon.dhaiban.presentation.app

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.dhaiban.presentation.sharedUiState.toUiState
import com.semicolon.domain.entity.DefaultCurrency
import com.semicolon.domain.entity.Status
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.ManageCartUseCase
import com.semicolon.domain.usecase.ManageConnectivityUseCase
import com.semicolon.domain.usecase.ManageCurrencyUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.utils.BadRequestException
import com.semicolon.domain.utils.InternetException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.coroutines.CoroutineContext

interface AppUiEffect
class AppScreenModel(
    private val userConfiguration: LocalConfigurationUseCase,
    private val authentication: UserAuthenticationUseCase,
    private val manageNetwork: ManageConnectivityUseCase,
    private val manageCurrencyUseCase: ManageCurrencyUseCase,
    private val manageCartUseCase: ManageCartUseCase
) : BaseScreenModel<AppUiState, AppUiEffect>(
    AppUiState()
) {
    override val viewModelScope: CoroutineScope = screenModelScope
    lateinit var mContext: Context

    init {
        getUserCurrency()
        observeNetwork()
        getDefaultData()
        getCartItemsCount()
        getInitScreen()
        getIsActive()
    }

    private fun initiateLocalStrings() {

        _state.update { it.copy(isLoading = true) }
        screenModelScope.launch(Dispatchers.IO) {
            val currentLangCode = LayoutDirection.Ltr.name
//                async { userConfiguration.getUserLanguage() }.await()
            val currentLayoutDirection = async { userConfiguration.getLayoutDirection() }.await()
            Log.d("currentLayoutDirectionf",currentLayoutDirection)

            val defaultLangCode = _state.value.appConfig.languages.find {
                it.id == _state.value.appConfig.defaultLang.id }?.code ?: "en"

            val defaultLanguageUrl = _state.value.appConfig.defaultLang.url

            val defaultLayoutDirection = LayoutDirection.Ltr.name
//                _state.value.appConfig.languages.find { it.id == _state.value.appConfig.defaultLang.id }?.dir
//                    ?: "ltr"
            val layoutDirection = if (isRtlLanguage(Locale.getDefault())){
                LayoutDirection.Rtl
            } else {
                LayoutDirection.Ltr
            }
            Log.d("currentLayoutDirection 1 ",defaultLayoutDirection)
            Log.d("currentLayoutDirection 2 ",layoutDirection.name)
            val appConfig = _state.value.appConfig
            Log.d("appConfig", appConfig.toString())
            Log.d("defaultLangId", appConfig.defaultLang.id.toString())

            // Log the languages list
            appConfig.layoutDirection = layoutDirection
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

    private fun isRtlLanguage(locale: Locale): Boolean {
        return when (locale.language) {
            "ar", "he", "fa", "ur" -> true // Add other RTL languages as needed
            else -> false
        }
    }
    private fun getInitScreen() {
        screenModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) } // Set loading to true
            val isFirstTimeOpenApp = async { userConfiguration.readOnBoardingState() }.await()
            _state.update {
                it.copy(
                    isOnboardingCompleted = isFirstTimeOpenApp,
                    isLoading = false // Reset loading after completion
                )
            }
        }
    }
    private fun getIsActive() {
        screenModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) } // Set loading to true
            val isActive = async { userConfiguration.getIsActive() }.await()
            _state.update {
                it.copy(
                    isActive = isActive,
                    isLoading = false // Reset loading after completion
                )
            }
            Log.d("IsActive", isActive.toString())
        }
    }

    private fun saveLocalStringFile(url: String, langCode: String) {
        tryToExecute(
            function = { userConfiguration.saveLocalizationLanguage(url, langCode) },
            onSuccess = ::onSaveLocalStringFileSuccess,
            onError = ::onSaveLocalStringFileError
        )
    }

    private fun getUserCurrency() {
        viewModelScope.launch(Dispatchers.IO) {
            val userCurrency = userConfiguration.getUserCurrency()
            val userCurrencyId = userConfiguration.getCurrencyId()
            if (userCurrency == "" && userCurrencyId == 0) {
                tryToExecute(
                    function = { manageCurrencyUseCase.getDefaultCurrency() },
                    onSuccess = ::onGetUserCurrencySuccess,
                    onError = {}
                )
            } else {
                val selectedCurrency =
                    _state.value.appConfig.currencies.find { it.id == userCurrencyId }
                Log.e("selectedCurrency", selectedCurrency.toString())
                _state.update {
                    it.copy(
                        currency = selectedCurrency?.toCurrencyUiState() ?: AppCurrencyUiState()
                    )
                }
                Log.e("state", state.value.currency.toString())
            }
        }
    }

    private fun getCartItemsCount() {
        viewModelScope.launch(Dispatchers.IO) {
            val isAuthorized = userConfiguration.getUserToken().isNotEmpty()
            if (isAuthorized) {
                tryToExecute(
                    function = { manageCartUseCase.getCartProducts().cartProducts.size },
                    onSuccess = { cartCount ->
                        _state.update { it.copy(cartItemsNumber = cartCount) }
                    },
                    onError = { exception ->
                        _state.update { it.copy(isLoading = false) }
                        Log.e("CartError", exception.message.toString())
                    }
                )
            }
        }
    }

    private fun onGetUserCurrencySuccess(defaultCurrency: DefaultCurrency) {
        _state.update { appUiState ->
            appUiState.copy(
                currency = defaultCurrency.toCurrencyUiState()
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            userConfiguration.saveUserCurrency(defaultCurrency.symbol)
            userConfiguration.saveCurrencyId(defaultCurrency.id)
            userConfiguration.saveNameUserCurrency(defaultCurrency.code)
            Log.d("currenyNameAppScreenModelscreen",defaultCurrency.code)

        }
    }

    private fun onSaveLocalStringFileSuccess(unit: Unit) {
        getLatestStringFile()
        viewModelScope.launch { userConfiguration.saveStringResourceState(true) }
    }

    private fun onSaveLocalStringFileError(exception: Exception) {
        updateState { it.copy(showSnackBar = false) }
        if (exception is BadRequestException) {
            showSnackBar()
        }

        if (exception is InternetException) {
            showSnackBar()
        }
    }

     fun getDefaultData() {
        tryToExecute(
            function = { authentication.getAppConfig().toUiState() },
            onSuccess = ::onGetListSuccess,
            onError = ::onError
        )
    }

    private fun onGetListSuccess(appConfig: AppConfigUiState) {
        Log.e("AppConfigAppScreenModel", appConfig.defaultLang.toString())
        updateState { it.copy(appConfig = appConfig) }
        initiateLocalStrings()
        getUserCurrency()
    }

    private fun onError(exception: Exception) {
        when (exception is BadRequestException) {
            true -> {
                showSnackBar()
            }

            false -> {}
        }
    }

    private fun showSnackBar() {
        viewModelScope.launch {
            updateState { it.copy(showSnackBar = true) }
            delay(3000)
            updateState { it.copy(showSnackBar = false) }
        }
    }

    private fun observeNetwork() {
        tryToCollect(
            function = { manageNetwork.getNetworkStatus() },
            onNewValue = ::onCollectNetworkStateSuccess,
            onError = ::onCollectNetworkError
        )
    }

    private fun onCollectNetworkStateSuccess(networkState: Status) {
        screenModelScope.launch {
            when (networkState) {
                Status.Available -> {}
                Status.Unavailable -> {
                    showSnackBar()
                }

                Status.Losing -> showSnackBar()
                Status.Lost -> showSnackBar()
            }
        }
    }

    private fun onCollectNetworkError(exception: Exception) {}

    private fun getLatestStringFile() {
        viewModelScope.launch(Dispatchers.IO) {
            userConfiguration.getStrings().collectLatest { strings ->
                _state.update {
                    it.copy(
                        stringRes = strings.toStringResource(),
                        secondStringRes = strings.SecondStringResources(),
                        isLoading = false,
                        layoutDirection = userConfiguration.getLayoutDirection()
                    )
                }
            }
        }
    }

    fun setContext(context:Context) {
        this.mContext = context
    }
    fun setCurrentScreen(screen: String) {
        _state.update { it.copy(currentScreen = screen) }
    }

    fun getCurrencySymbol(): String {
        return _state.value.currency.symbol
    }

    fun getCurrency(): AppCurrencyUiState =
        _state.value.currency

    fun updateCurrency(id: Int) {
        val selectedCurrency = _state.value.appConfig.currencies.find { it.id == id }
        selectedCurrency?.let { currency ->
            _state.update { it.copy(currency = currency.toCurrencyUiState()) }
        }
    }


    fun updateCurrencySymbol(currencySymbol: String) {
        _state.update { it.copy(currency = it.currency.copy(symbol = currencySymbol)) }
    }

    fun updateCartItemsNumber(count: Int) {
        _state.update { it.copy(cartItemsNumber = count) }
    }

    fun updateFavoriteState(state: Boolean) {
        _state.update { it.copy(userChangedFavoriteFromFavoriteScreen = state) }
    }

    fun getFavoriteState() = _state.value.userChangedFavoriteFromFavoriteScreen
}