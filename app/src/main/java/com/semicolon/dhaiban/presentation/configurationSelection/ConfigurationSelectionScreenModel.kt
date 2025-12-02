package com.semicolon.dhaiban.presentation.configurationSelection

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.dhaiban.presentation.sharedUiState.toUiState
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.utils.UserConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ConfigurationSelectionScreenModel(
    private val userConfig: UserConfig,
    private val localConfigurationUseCase: LocalConfigurationUseCase,
    private val authentication: UserAuthenticationUseCase,
) : BaseScreenModel<ConfigurationSelectionScreenUiState, ConfigurationSelectionUiEffect>(
    ConfigurationSelectionScreenUiState()
), ConfigurationSelectionInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        updateState { it.copy(currentUserConfig = userConfig) }
        getData()
    }

    override fun onSelectItem(item: String) {

        updateState { it.copy(selectedOption = item) }
        viewModelScope.launch(Dispatchers.IO) {
            when (userConfig) {
                UserConfig.COUNTRY -> {
                    val countryCode =
                        state.value.appConfig.countries.find { it.title == item }?.code ?: ""
                    val countryId =
                        state.value.appConfig.countries.find { it.title == item }?.id ?: 0
                    localConfigurationUseCase.saveUserCountry(countryCode)
                    localConfigurationUseCase.saveCountryId(countryId)
                }

                UserConfig.LANGUAGE -> {
                    val languageCode =
                        _state.value.appConfig.languages.find { it.title == item }?.code?.uppercase()
                            ?: ""
                    val layoutDirection =
                        _state.value.appConfig.languages.find { it.title == item }?.dir ?: ""
                    localConfigurationUseCase.saveLanguageTemp(languageCode)
                    localConfigurationUseCase.saveLayoutDirection(layoutDirection)
                    Log.d("ConfigurationlayoutDirection",layoutDirection)
                    localConfigurationUseCase.getUserLanguage()
                }

                UserConfig.CURRENCY -> {
                    val currencynameSymbol =
                        state.value.appConfig.currencies.find { it.name == item }?.code ?: ""
                    Log.d("currenyNameConfigScreenModelscreen",currencynameSymbol)

                    val currencySymbol =
                        state.value.appConfig.currencies.find { it.name == item }?.symbol ?: ""
                    val currencyId =
                        state.value.appConfig.currencies.find { it.name == item }?.id ?: 0
                    localConfigurationUseCase.saveUserCurrency(currencySymbol)
                    localConfigurationUseCase.saveCurrencyId(currencyId)
                    localConfigurationUseCase.saveNameUserCurrency(currencynameSymbol)
                    Log.d("currenyNameconfigScreenmonModelscreen",currencynameSymbol)

                }
            }
            sendNewEffect(ConfigurationSelectionUiEffect.OnDismissBottomSheet)
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
        val currentOptions = when (userConfig) {
            UserConfig.COUNTRY -> {
                appConfig.countries.map { it.title }
            }

            UserConfig.LANGUAGE -> {
                appConfig.languages.map { it.title }
            }

            UserConfig.CURRENCY -> {
                appConfig.currencies.map { it.name }
            }
        }
        updateState {
            it.copy(
                currentOptions = currentOptions,
                oldOptions = currentOptions,
                isLoading = false,
                appConfig = appConfig
            )
        }
    }

    private fun onError(exception: Exception) {
        updateState { it.copy(isLoading = false) }
    }

    override fun onQueryChanged(query: String) {
        val newOptions = if (query.isBlank()) {
            state.value.oldOptions
        } else {
            state.value.oldOptions.filter { option ->
                option.contains(query, ignoreCase = true)
            }
        }
        updateState { it.copy(searchValue = query, currentOptions = newOptions) }
    }
}