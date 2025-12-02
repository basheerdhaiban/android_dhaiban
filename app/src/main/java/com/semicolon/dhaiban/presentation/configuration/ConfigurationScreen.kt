package com.semicolon.dhaiban.presentation.configuration

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.DhaibanBottomSheet
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.main.MainActivity
import com.semicolon.dhaiban.presentation.onBoarding.OnBoardingScreen
import com.semicolon.dhaiban.presentation.welcome.WelcomeScreen
import com.semicolon.domain.utils.UserConfig
import kotlinx.coroutines.flow.collectLatest

class ConfigurationScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ConfigurationScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: ConfigurationUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
//        val bottomSheetNavigator = LocalBottomSheetNavigator.current
        val context = LocalContext.current
        val languages by screenModel.languages.collectAsState()
        val countries by screenModel.countries.collectAsState()
        val currencies by screenModel.currencies.collectAsState()

        LaunchedEffect(Unit) {
            if (state.onBoardingState) {
                navigator.replaceAll(WelcomeScreen())
            }
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    ConfigurationUiEffect.OnNavigateToOnBoardingScreen -> {
                        navigator.push(OnBoardingScreen())
                    }

                    is ConfigurationUiEffect.OnOpenCountriesBottomSheet -> {
//                        bottomSheetNavigator.show(ConfigurationSelectionScreen(effect.userConfig))
                    }

                    is ConfigurationUiEffect.OnOpenCurrenciesBottomSheet -> {
//                        bottomSheetNavigator.show(ConfigurationSelectionScreen(effect.userConfig))
                    }

                    is ConfigurationUiEffect.OnOpenLanguagesBottomSheet -> {
//                        bottomSheetNavigator.show(ConfigurationSelectionScreen(effect.userConfig))
                    }

                    is ConfigurationUiEffect.OnUpdateCurrencySymbol -> {
                        appScreenModel.updateCurrencySymbol(
                            effect.currencySymbol
                        )
                    }

                    is ConfigurationUiEffect.OnUpdateCurrency -> appScreenModel.updateCurrency(
                        effect.id
                    )

                    ConfigurationUiEffect.ShowNetworkError -> {}
                }
            }
        }

        ConfigurationScreenContent(
            state = state,
            currentList = when (state.userConfig) {
                UserConfig.COUNTRY -> countries.map { it.title to it.id }
                UserConfig.LANGUAGE -> languages.map { it.title to it.id }
                UserConfig.CURRENCY -> currencies.map { it.name to it.id }
            },
            listener = screenModel
        )

        BackHandler {
            if (state.showBottomSheet)
                screenModel.onDismissBottomSheet()
            else
                (context as? MainActivity)?.finish()
        }
    }

    @Composable
    fun ConfigurationScreenContent(
        state: ConfigurationUiState,
        currentList: List<Pair<String, Int>>,
        listener: ConfigurationScreenInteractionListener
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Theme.colors.splashBackground)
                .safeDrawingPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier.size(300.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "logo"
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.country,
                    onClick = listener::onClickCountryButton,
                    iconRes = R.drawable.icon_phone,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Theme.colors.primary100
                    )
                )

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.language,
                    onClick = listener::onClickLanguageButton,
                    iconRes = R.drawable.icon_language,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Theme.colors.primary
                    )
                )

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.currency,
                    onClick = listener::onClickCurrencyButton,
                    iconRes = R.drawable.icon_currency,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Theme.colors.primarySemiDark
                    )
                )

                PrimaryButton(
                    modifier = Modifier.fillMaxWidth(),
                    text = Theme.strings.confirm,
                    onClick = listener::onClickConfirmButton,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Theme.colors.primaryDark
                    )
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .noRippleEffect { listener.onClickSkipButton() },
                    text = Theme.strings.skip.lowercase(),
                    style = Theme.typography.titleLarge,
                    color = Theme.colors.black87,
                    textAlign = TextAlign.Center,
                )

            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            AnimatedVisibility(
                visible = state.showBottomSheet,
                enter = slideInVertically(
                    initialOffsetY = { it }
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }
                ) + fadeOut()
            ) {
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
                    onSelectItem = { item, id ->
                        listener.onItemSelected(item, id)
                    },
                    onClickCancel = { listener.onClickCancel() },
                    onClickConfirm = { listener.onClickConfirm() }
                )
            }
        }

    }

}

@Composable
@Preview(showBackground = true)
fun PreviewUserConfigsScreen() {
    val dummyListener = object : ConfigurationScreenInteractionListener {
        override fun onClickCountryButton() {}
        override fun onClickLanguageButton() {}
        override fun onClickCurrencyButton() {}
        override fun onClickConfirmButton() {}
        override fun onClickSkipButton() {}
        override fun onDismissBottomSheet() {}
        override fun onQueryValueChanged(query: String) {}
        override fun onItemSelected(item: String, id: Int) {}
        override fun onClickConfirm() {}
        override fun onClickCancel() {}
    }
    DhaibanTheme {
        ConfigurationScreen().ConfigurationScreenContent(
            state = ConfigurationUiState(),
            listOf(),
            listener = dummyListener
        )
    }
}