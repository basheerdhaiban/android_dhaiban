package com.semicolon.dhaiban.presentation.profile

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.StringResources
import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.domain.utils.UserConfig

data class ProfileScreenUiState(
    val errorMessage: String = "no internet",
    val isLoading: Boolean = false,
    val userData: UserDataUiState = UserDataUiState(),
    val logOutExpandState: Boolean = false,
    val recreateState: Boolean = false,
    val showBottomSheet: Boolean = false,
    val showDeleteAccountDialog: Boolean = false,
    val userConfig: UserConfig = UserConfig.LANGUAGE,
    val appConfig: AppConfigUiState = AppConfigUiState(),
    val queryValue: String = "",
    val selectedLanguage: String = "",
    val selectedCountry: String = "",
    val selectedCurrency: String = "",
    val unChangedSelectedLanguage: String = "",
    val unChangedSelectedCountry: String = "",
    val unChangedSelectedCurrency: String = "",
    val defaults: DefaultPreferences = DefaultPreferences(),
    val selectedCurrencyId: Int = 0,
    val loginDialogState: Boolean = false,
    var countOfUnreadMessage :Int=0,
    val layoutDirection: String = "",
    val stringRes: StringResources = StringResources(),

    )

@Immutable
data class UserDataUiState(
    val username: String = "",
    val imageUrl:String = "",
    val isAuthenticated: Boolean = false
)
@Immutable
data class ProfileOptionUiState(
    val name: String = "",
    val icon: Int = 0
)

data class DefaultPreferences(
    val language: String = "",
    val country: String = "",
    val currency: String = ""
)