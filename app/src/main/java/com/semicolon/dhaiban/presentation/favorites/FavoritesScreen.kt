package com.semicolon.dhaiban.presentation.favorites

import ProductScreen
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.DhaibanProduct
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.cart.composables.ButtonForEmptyState
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants.FAVORITES_SCREEN
import kotlinx.coroutines.flow.collectLatest

class FavoritesScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<FavoritesScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: FavoritesScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(FAVORITES_SCREEN)
            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    is FavoritesScreenUiEffect.OnNavigateToProfileScreen -> {
                        if (navigator.canPop)
                            navigator.pop()
                    }

                    is FavoritesScreenUiEffect.OnNavigateToProductDetails -> {
                        navigator.push(ProductScreen(effect.productId))
                    }

                    FavoritesScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }
        }
        FavoritesScreenContent(state = state, listener = screenModel, appModel = appScreenModel)
    }
}

@Composable
private fun FavoritesScreenContent(
    state: FavoritesScreenUiState,
    listener: FavoritesScreenInteractionListener,
    appModel: AppScreenModel
) {
    Column(modifier = Modifier.safeDrawingPadding()) {
        AppBarWithIcon(
            countOFunReadNotifcation=state.countOfUnreadMessage ,
            title = Theme.strings.favourites,
            onClickUpButton = listener::onClickUpButton,
            onClickNotification = listener::onClickNotification
        )
        Spacer(modifier = Modifier.height(16.dp))
        AnimatedContent(targetState = state.isLoading, label = "") {
            if (it) {
                LoadingContent()
            } else {
                if (state.products.isNotEmpty()) {
                    LazyVerticalGrid(columns = GridCells.Fixed(2)) {
                        itemsIndexed(
                            items = state.products,
                            key = { _, item -> item.id }) { _, product ->
                            DhaibanProduct(productId = product.id,
                                imageUrl = product.imageUrl,
                                productTitle = product.productTitle,
                                price = product.unitPrice,
                                afterDiscount = product.afterDiscount,
                                currencySymbol = state.currency.symbol,
                                exchangeRate = state.currency.exchangeRate,
                                isFavourite = true,
                                onFavouriteClick = { productId, _ ->
                                    appModel.updateFavoriteState(true)
                                    listener.onClickFavorite(productId)
                                },
                                onProductClick = { productId ->
                                    listener.onClickProduct(productId)
                                })
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.favourite),
                                    contentDescription = "No Data Icon",
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = Theme.strings.noFavourite,
                                    style = Theme.typography.headline,

                                    )

                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = Theme.strings.You_have_nothing_on_your_list_yet,
                                    style = Theme.typography.body.copy(
                                        fontWeight = FontWeight(
                                            400
                                        )
                                    ),
                                    textAlign = TextAlign.Start,
                                    color = Theme.colors.dimGray
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = Theme.strings.its_never_too_late_to_change_it,
                                    style = Theme.typography.body.copy(
                                        fontWeight = FontWeight(
                                            400
                                        )
                                    ),
                                    textAlign = TextAlign.Start,
                                    color = Theme.colors.dimGray
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                ButtonForEmptyState("Go back"){
                                    listener.onClickUpButton()
                                }


                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun LoadingContent() {
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