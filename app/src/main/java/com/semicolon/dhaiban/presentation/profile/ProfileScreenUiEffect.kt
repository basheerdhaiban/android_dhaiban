package com.semicolon.dhaiban.presentation.profile

import com.semicolon.dhaiban.presentation.chat.ChatUiEffect
import com.semicolon.dhaiban.presentation.myprofile.MyProfileUiEffect

sealed interface ProfileScreenUiEffect {
    data object OnNavigateToFavoritesScreen : ProfileScreenUiEffect
    data object OnNavigateToOrdersScreen : ProfileScreenUiEffect
    data object OnLogoutSuccess : ProfileScreenUiEffect
    data class OnUpdateCurrencySymbol(val currencySymbol: String) : ProfileScreenUiEffect
    data object OnNavigateToAddressScreen : ProfileScreenUiEffect
    data object OnNavigateTochatRoom : ProfileScreenUiEffect
    data object OnNavigateToMyProfileScreen : ProfileScreenUiEffect
    data class OnUpdateCurrency(val id: Int) : ProfileScreenUiEffect
    data object OnNavigateToSearchScreen : ProfileScreenUiEffect
    data object OnNavigateToLogin : ProfileScreenUiEffect
    data object OnNavigateToContactUs : ProfileScreenUiEffect
    data object OnNavigateToFaq : ProfileScreenUiEffect
    data object OnDeleteAccountSuccess : ProfileScreenUiEffect
    data object OnNavigateToRefundScreen : ProfileScreenUiEffect
    data object OnNavigateToWalletScreen : ProfileScreenUiEffect
    data object OnNavigateToNotificationScreen : ProfileScreenUiEffect
    data object NavigateToTryToConnect : ProfileScreenUiEffect

}