package com.semicolon.dhaiban.presentation.configurationSelection

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.DhaibanSearchBar
import com.semicolon.domain.utils.UserConfig
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class ConfigurationSelectionScreen(private val userConfig: UserConfig) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ConfigurationSelectionScreenModel>(
            parameters = { parametersOf(userConfig) }
        )
        val state: ConfigurationSelectionScreenUiState by screenModel.state.collectAsState()
        val bottomSheetNavigator = LocalBottomSheetNavigator.current

        LaunchedEffect(Unit) {
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    ConfigurationSelectionUiEffect.OnDismissBottomSheet -> {
                        bottomSheetNavigator.hide()
                    }
                }
            }
        }

        ConfigurationSelectionScreenContent(state = state, listener = screenModel)
    }

}

@Composable
fun ConfigurationSelectionScreenContent(
    state: ConfigurationSelectionScreenUiState, listener: ConfigurationSelectionInteractionListener
) {
    val headerTitle = when (state.currentUserConfig) {
        UserConfig.COUNTRY -> Theme.strings.country
        UserConfig.LANGUAGE -> Theme.strings.language
        UserConfig.CURRENCY -> Theme.strings.currency
    }

    val headerIconRes = when (state.currentUserConfig) {
        UserConfig.COUNTRY -> R.drawable.icon_language
        UserConfig.LANGUAGE -> R.drawable.icon_language
        UserConfig.CURRENCY -> R.drawable.icon_currency
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .safeDrawingPadding()

    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            HeaderContent(headerIconRes = headerIconRes, headerTitle = headerTitle)

            DhaibanSearchBar(
                modifier = Modifier.padding(bottom = 16.dp),
                value = state.searchValue,
                hint = Theme.strings.search,
                onQueryChanged = listener::onQueryChanged
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {

                item {
                    AnimatedVisibility(
                        visible = state.isLoading,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier
                                .height(300.dp)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Theme.colors.primary)
                        }
                    }
                }

                items(items = state.currentOptions) { item ->
                    RadioButtonItem(
                        option = item,
                        isSelected = (item == state.selectedOption),
                        onClick = { listener.onSelectItem(item) }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Theme.colors.black38)
                    )
                }
            }
        }
    }
}

@Composable
private fun RadioButtonItem(
    option: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = option, style = Theme.typography.title)
        RadioButton(
            selected = isSelected,
            onClick = { onClick(option) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Theme.colors.primary,
                unselectedColor = Theme.colors.black38
            )
        )
    }
}

@Composable
private fun HeaderContent(
    modifier: Modifier = Modifier,
    headerIconRes: Int,
    headerTitle: String
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(headerIconRes),
            contentDescription = "header icon"
        )
        Text(
            text = headerTitle,
            style = Theme.typography.headline,
            color = Theme.colors.black87
        )
    }
}

@Composable
@Preview(showBackground = true)
fun PreviewConfigurationSelectionBottomSheet() {
    val dummyListener = object : ConfigurationSelectionInteractionListener {
        override fun onSelectItem(item: String) {}
        override fun onQueryChanged(query: String) {}
    }
    DhaibanTheme {
        ConfigurationSelectionScreenContent(
            state = ConfigurationSelectionScreenUiState(), listener = dummyListener
        )
    }
}