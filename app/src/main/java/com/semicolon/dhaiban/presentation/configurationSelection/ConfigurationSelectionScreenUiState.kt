package com.semicolon.dhaiban.presentation.configurationSelection

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.configuration.ConfigurationUiState
import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.utils.UserConfig

@Immutable
data class ConfigurationSelectionScreenUiState(
    val searchValue: String = "",
    val isLoading: Boolean = false,
    val currentOptions: List<String> = emptyList(),
    val oldOptions: List<String> = emptyList(),
    val full: List<String> = emptyList(),
    val selectedOption: String = "",
    val currentUserConfig: UserConfig = UserConfig.COUNTRY,
    val appConfig: AppConfigUiState = AppConfigUiState(),
)