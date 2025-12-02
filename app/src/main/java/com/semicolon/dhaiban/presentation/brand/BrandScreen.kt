package com.semicolon.dhaiban.presentation.brand

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.brandproducts.BrandProductsScreen
import com.semicolon.dhaiban.presentation.home.HomeScreen
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants
import kotlinx.coroutines.flow.collectLatest

class BrandScreen : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<BrandScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: BrandScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(Constants.BRAND_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    BrandScreenUiEffect.OnNavigateToHomeScreen -> {
                        navigator.popUntil { navigator.lastItem == HomeScreen() }
                    }

                    is BrandScreenUiEffect.OnNavigateToBrandProducts -> navigator.push(
                        BrandProductsScreen(effect.brandName, effect.brandId)
                    )

                    BrandScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }
        }
        BrandScreenContent(state = state, listener = screenModel)

        BackHandler {
            navigator.popUntil { navigator.lastItem == HomeScreen() }
        }
    }
}

@Composable
private fun BrandScreenContent(
    state: BrandScreenUiState,
    listener: BrandScreenModel
) {
    Column(modifier = Modifier.fillMaxSize()) {
        AppBarWithIcon(
            title = Theme.strings.topBrands,
            onClickUpButton = listener::onClickBackButton,
            onClickNotification = listener::onClickNotification
        )

        AnimatedContent(targetState = state.isLoading, label = "") { isLoading ->
            if (isLoading) {
                LoadingContent()
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(horizontal = 12.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    items(state.brands) { brand ->
                        BrandItem(
                            id = brand.id,
                            title = brand.title,
                            image = brand.image,
                            backColor = Theme.colors.black60,
                            textColor = Theme.colors.white,
                            onClick = { id ->
                                listener.onClickBrand(id, brand.title)
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun BrandItem(
    id: Int,
    title: String,
    image: String,
    backColor: Color,
    textColor: Color,
    onClick: (Int) -> Unit
) {
    Box(
        Modifier
            .size(180.dp)
            .clip(RoundedCornerShape(16.dp))
            .padding(start = 6.dp, end = 6.dp, bottom = 12.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onClick(id) },
        contentAlignment = Alignment.Center
    ) {
        CoilImage(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(16.dp)),
            url = image,
            contentDescription = "",
            scaleType = ContentScale.Fit
        )
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .fillMaxSize()
                .background(backColor)
        ) {}
        Text(
            text = title,
            style = Theme.typography.titleLarge,
            color = textColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun LoadingContent() {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(10) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
    }
}