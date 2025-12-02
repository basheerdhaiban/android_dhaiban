package com.semicolon.dhaiban.presentation.profile

import AddressScreen
import MyProfileScreen
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.DhaibanBottomDialog
import com.semicolon.dhaiban.designSystem.composables.DhaibanBottomSheet
import com.semicolon.dhaiban.designSystem.composables.SecondDhaibanAlertDialog
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.contactus.ContactUsScreen
import com.semicolon.dhaiban.presentation.faq.FaqScreen
import com.semicolon.dhaiban.presentation.favorites.FavoritesScreen
import com.semicolon.dhaiban.presentation.home.container.HomeTab
import com.semicolon.dhaiban.presentation.list_of_chat.ListOfChatScreen
import com.semicolon.dhaiban.presentation.main.MainActivity
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.orders.OrdersScreen
import com.semicolon.dhaiban.presentation.profile.composables.OptionsComponent
import com.semicolon.dhaiban.presentation.profile.composables.ProfileHeaderComponent
import com.semicolon.dhaiban.presentation.profile.composables.ProfileOptionsComponent
import com.semicolon.dhaiban.presentation.refund.RefundScreen
import com.semicolon.dhaiban.presentation.search.SearchScreen
import com.semicolon.dhaiban.presentation.utils.Constants.PROFILE_SCREEN
import com.semicolon.dhaiban.presentation.utils.root
import com.semicolon.dhaiban.presentation.wallet.WalletScreen
import com.semicolon.dhaiban.presentation.welcome.WelcomeScreen
import com.semicolon.domain.utils.UserConfig
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileScreen (private val comeFromProfile:Boolean =false) : Screen {

    override val key: ScreenKey = uniqueScreenKey
    fun finishActivity(activity: Activity) {
        activity.finish()
    }
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ProfileScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: ProfileScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val languages by screenModel.languages.collectAsState()
        val countries by screenModel.countries.collectAsState()
        val currencies by screenModel.currencies.collectAsState()
        val appScreenState by appScreenModel.state.collectAsState()
        LaunchedEffect(Unit) {
            screenModel.getUserData()
            if (appScreenState.stringRes.myProfile.isBlank()){
            appScreenModel.getDefaultData()}
            appScreenModel.setCurrentScreen(PROFILE_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    ProfileScreenUiEffect.OnNavigateToFavoritesScreen -> {
                        navigator.push(FavoritesScreen())
                    }

                    ProfileScreenUiEffect.OnNavigateToOrdersScreen -> {
                        navigator.push(OrdersScreen())
                    }

                    ProfileScreenUiEffect.OnLogoutSuccess -> {
                        navigator.root?.replaceAll(WelcomeScreen())
                    }

                    is ProfileScreenUiEffect.OnUpdateCurrencySymbol -> {
                        appScreenModel.updateCurrencySymbol(
                            effect.currencySymbol
                        )
                    }

                    ProfileScreenUiEffect.OnNavigateToAddressScreen -> navigator.push(
                        AddressScreen(fromProfile = true)
                    )

                    ProfileScreenUiEffect.OnNavigateTochatRoom -> navigator.push(
                        ListOfChatScreen()
                    )


                    ProfileScreenUiEffect.OnNavigateToMyProfileScreen -> navigator.push(
                        MyProfileScreen()
                    )

                    is ProfileScreenUiEffect.OnUpdateCurrency -> appScreenModel.updateCurrency(
                        effect.id
                    )

                    is ProfileScreenUiEffect.OnNavigateToSearchScreen -> navigator.push(SearchScreen())

                    ProfileScreenUiEffect.OnNavigateToLogin -> navigator.root?.replaceAll(
                        WelcomeScreen()
                    )

                    ProfileScreenUiEffect.OnNavigateToContactUs -> navigator.push(ContactUsScreen())

                    ProfileScreenUiEffect.OnNavigateToFaq -> navigator.push(FaqScreen())

                    ProfileScreenUiEffect.OnNavigateToRefundScreen -> navigator.push(RefundScreen())

                    ProfileScreenUiEffect.OnNavigateToWalletScreen -> navigator.push(WalletScreen())

                    ProfileScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )

                    ProfileScreenUiEffect.OnDeleteAccountSuccess -> {
                        // Use a safer navigation approach for logout/delete account
                        (navigator.parent?.parent ?: navigator).replaceAll(
                            WelcomeScreen()
                        )
                    }

                    ProfileScreenUiEffect.NavigateToTryToConnect ->  navigator.push(ProfileScreen(comeFromProfile = true))

                    else -> {}
                }
            }
        }
        Log.d("profileScreen",appScreenState.stringRes.myProfile.isBlank().toString())
        Log.d("profileScreen2",appScreenState.stringRes.myProfile.isEmpty().toString())

        val isConnected = screenModel.isNetworkAvailable?.collectAsState()

        Log.d("AnimatedVisibility", isConnected?.value.toString())
        if (isConnected?.value == true) {

            ProfileScreenContent(
                model = screenModel,
                appScreenModel,
                authorized = state.userData.isAuthenticated,
                currentList = when (state.userConfig) {
                    UserConfig.COUNTRY -> countries.map { it.title to it.id }
                    UserConfig.LANGUAGE -> languages.map { it.title to it.id }
                    UserConfig.CURRENCY -> currencies.map { it.name to it.id }
                },
                state = state,
                listener = screenModel
            )

        } else {
            val context = LocalContext.current
            Box(modifier = Modifier.fillMaxSize()) {
                isConnected?.value?.let {
                    AnimatedVisibility(
                        visible = !it,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it },
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            SecondDhaibanAlertDialog(
                                title = "There is no connection",
                                body = {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.noconnection),
                                            contentDescription = "Payment Success Image"
                                        )
                                    }
                                },
                                positiveText = "Try to connect",
                                negativeText = "Exit",
                                onDismiss = { },
                                onPositive = {
                                    val intent = Intent(context, MainActivity::class.java).apply {
                                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                                    }
                                    context.startActivity(intent)
                                    screenModel.tryToConnect()


                                }
                            )
                        }
                    }
                }
            }
        }


        BackHandler {
            if (state.showBottomSheet)
                screenModel.onDismissBottomSheet()
            else
                navigator.parent?.replace(HomeTab)
        }
    }

}

@Composable
private fun ProfileScreenContent(
    model: ProfileScreenModel?,
    appScreenModel: AppScreenModel? = null,
    authorized: Boolean,
    currentList: List<Pair<String, Int>>,
    state: ProfileScreenUiState,
    listener: ProfileScreenInteractionListener
) {

    val context = LocalContext.current
    var showToast by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(Unit) { onDispose { coroutineScope.cancel() } }
    val optionList = mutableListOf(
        ProfileOptionUiState(
            Theme.strings.myProfile,
            icon = R.drawable.profile_icon
        ),
        ProfileOptionUiState(Theme.strings.address, icon = R.drawable.address_icon),
        ProfileOptionUiState(
            Theme.strings.language,
            icon = R.drawable.language_icon
        ),
        ProfileOptionUiState(Theme.strings.country, icon = R.drawable.country_icon),
        ProfileOptionUiState(
            Theme.strings.currency,
            icon = R.drawable.currency_icon
        ),

        ProfileOptionUiState(
            Theme.strings.chatRoom,
            icon = R.drawable.charroom
        )

    )
    val optionListWithoutRoom = mutableListOf(
        ProfileOptionUiState(
            Theme.strings.myProfile,
            icon = R.drawable.profile_icon
        ),
        ProfileOptionUiState(
            Theme.strings.language,
            icon = R.drawable.language_icon
        ),
        ProfileOptionUiState(Theme.strings.country, icon = R.drawable.country_icon),
        ProfileOptionUiState(
            Theme.strings.currency,
            icon = R.drawable.currency_icon
        ),


        )
    if (state.userData.isAuthenticated.not()) {
        optionList.remove(
            ProfileOptionUiState(
                Theme.strings.myProfile,
                icon = R.drawable.profile_icon
            )
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(Modifier.fillMaxSize()) {
            item {
                ProfileHeaderComponent(
                    username = state.userData.username,
                    image = if (state.userData.isAuthenticated) state.userData.imageUrl else "",
                    onSearchChanged = {},
                    state = state,
                    countOFunReadNotifcation = state.countOfUnreadMessage,
                    onNotificationsClick = { listener.onClickNotification() }
                ) {
                    listener.onClickSearch()
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            if (state.userData.isAuthenticated) {
                item {
                    OptionsComponent(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        onOrdersClick = { listener.onClickOrdersButton() },
                        onFavoritesClick = { listener.onClickFavoritesButton() },
                        onWalletClick = { listener.onClickWallet() },
                        onRefundClick = { listener.onClickRefund() }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            items(1) {
                ProfileOptionsComponent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = Theme.strings.account,
                    options = if (state.userData.isAuthenticated.not()) {
                        optionListWithoutRoom.toList()
                    } else {
                        optionList.toList()
                    },
                    onMyProfileClick = {
                        if (authorized) {
                            listener.onMyProfileClick()
                        } else {
                            showToast = true
                            coroutineScope.launch {
                                delay(2000)
                                showToast = false
                            }
                        }
                    },
                    onLanguageClick = {
                        listener.onLanguageClick()
                    },
                    onCountryClick = {
                        listener.onCountryClick()
                    },
                    onCurrencyClick = {
                        listener.onCurrencyClick()
                    },
                    onAddressClick = {
                        listener.onClickAddress()
                    },
                    onChatRoomClick = { listener.onClickChatRoom() }
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProfileOptionsComponent(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    title = Theme.strings.reachUs,
                    options = listOf(

                        ProfileOptionUiState(
                            Theme.strings.contactUs,
                            icon = R.drawable.contact_us_icon
                        ),
                        ProfileOptionUiState(Theme.strings.fAQ, icon = R.drawable.faq_icon),
                        ProfileOptionUiState(
                            Theme.strings.termsConditions,
                            icon = R.drawable.terms_conditions_icon
                        ),
                        ProfileOptionUiState(
                            Theme.strings.privacyPolicy,
                            icon = R.drawable.privacy_policy_icon
                        )
                    ),
                    onPrivacyClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://dhaibantrading.com/policy.html")
                        )
                        context.startActivity(intent)
                    },
                    onTermsClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://dhaibantrading.com/policy.html")
                        )
                        context.startActivity(intent)
                    },
                    onContactUsClick = {
                        listener.onClickContactUs()
                    },
                    onFaqClick = {
                        listener.onClickFaq()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .noRippleEffect {
                            if (state.userData.isAuthenticated) listener.onClickLogout()
                            else listener.onClickLogin()
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.logout_icon),
                            contentDescription = "Logout icon"
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = if (state.userData.isAuthenticated) Theme.strings.logOut
                            else Theme.strings.logIn,
                            color = Theme.colors.black,
                            style = Theme.typography.body.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        if (state.userData.isAuthenticated) {
                            Text(
                                modifier = Modifier.noRippleEffect { listener.onClickDeleteAccount() },
                                text = Theme.strings.deleteAccount,
                                color = Theme.colors.rustyRed,
                                style = Theme.typography.title.copy(fontWeight = FontWeight.W400)
                            )
                        }

                        Spacer(modifier = Modifier.width(6.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        AnimatedVisibility(
            visible = state.logOutExpandState,
            enter = slideInVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            exit = slideOutHorizontally(
                spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
        ) {
            DhaibanBottomDialog(
                title = Theme.strings.areYouSureYouWantToLogOut,
                positiveText = Theme.strings.logOut,
                negativeText = Theme.strings.back,
                onDismiss = { listener.onDismissDialog() },
                onPositive = {
                    listener.onConfirmLogout()
                    appScreenModel!!.updateFavoriteState(true)
                }
            )
        }
    }

    AnimatedVisibility(
        visible = state.showBottomSheet,
        enter = slideInVertically(
            initialOffsetY = { it }
        ),
        exit = slideOutVertically(
            targetOffsetY = { it }
        ) + fadeOut()
    ) {
        Log.d("DhaibanBottomSheet", state.queryValue)
        DhaibanBottomSheet(
            userConfig = state.userConfig,
            items = currentList,
            selectedOption = when (state.userConfig) {
                UserConfig.COUNTRY -> state.selectedCountry
                UserConfig.LANGUAGE -> state.selectedLanguage
                UserConfig.CURRENCY -> state.selectedCurrency
            },
            searchValue = state.queryValue,
            isLoading = state.isLoading,
            onDismiss = { listener.onDismissBottomSheet() },
            onQueryChanged = { listener.onQueryValueChanged(it) },
            onSelectItem = { title, id ->
                listener.onItemSelected(title, id)
            },
            onClickCancel = { listener.onClickCancel() },
            onClickConfirm = { listener.onClickConfirm() }
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.showDeleteAccountDialog,
            enter = slideInVertically(
                initialOffsetY = { it }
            ),
            exit = slideOutVertically(
                targetOffsetY = { it }
            ) + fadeOut()
        ) {
            DhaibanBottomDialog(

                title = Theme.strings.messageOfDelete
                ,
                positiveText = Theme.strings.delete,

                negativeText = Theme.strings.back,
                onDismiss = { listener.onDismissDeleteDialog() },
                onPositive = { listener.onConfirmDeleteAccount() }
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedContent(targetState = state.loginDialogState, label = "") {
            if (it) {
                DhaibanBottomDialog(
                    title = Theme.strings.addingItemToCartRequiresLogin,
                    image = R.drawable.authorization_image,
                    positiveText = Theme.strings.logIn,
                    negativeText = Theme.strings.cancel,
                    onDismiss = { listener.onDismissLoginDialog() },
                    onPositive = { listener.onClickLogin() }
                )
            }
        }
    }

    if (showToast) {
        Toast.makeText(
            context,
            Theme.strings.needsLogin,
            Toast.LENGTH_SHORT
        )
            .show()
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProfileScreenContentPreview() {
    val dummyListener = object : ProfileScreenInteractionListener {
        override fun onClickFavoritesButton() {}
        override fun onClickLogout() {}
        override fun onDismissDialog() {}
        override fun onConfirmLogout() {}
        override fun onLanguageClick() {}
        override fun onClickDeleteAccount() {

        }

        override fun onDismissDeleteDialog() {
        }

        override fun onConfirmDeleteAccount() {
        }

        override fun tryToConnect() {
            TODO("Not yet implemented")
        }

        override fun onCountryClick() {}
        override fun onCurrencyClick() {}
        override fun onDismissBottomSheet() {}
        override fun onQueryValueChanged(query: String) {}
        override fun onItemSelected(item: String, id: Int) {}
        override fun onClickConfirm() {}
        override fun onClickCancel() {}
        override fun onClickAddress() {}
        override fun onClickChatRoom() {
        }

        override fun onMyProfileClick() {}
        override fun onClickSearch() {}
        override fun onClickOrdersButton() {}
        override fun onClickLogin() {}
        override fun onClickContactUs() {}
        override fun onClickFaq() {}
        override fun onClickRefund() {}
        override fun onClickWallet() {}
        override fun onDismissLoginDialog() {}
        override fun onClickNotification() {}
    }
    DhaibanTheme {
        ProfileScreenContent(
            null,
            null,
            authorized = false,
            currentList = listOf(),
            state = ProfileScreenUiState(),
            dummyListener
        )
    }
}