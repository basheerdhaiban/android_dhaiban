package com.semicolon.dhaiban.presentation.brandproducts

import ProductScreen
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.home.HomeScreen
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat

class BrandProductsScreen(
    private val brandName: String,
    private val brandId: Int,
    private val fromHome: Boolean = false
) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel =
            getScreenModel<BrandProductsScreenModel>(parameters = { parametersOf(brandId) })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: BrandProductsScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(Constants.BRAND_PRODUCTS_SCREEN)
            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    BrandProductsScreenUiEffect.OnNavigateToBrandScreen -> {
                        if (fromHome) {
                            navigator.popUntil { navigator.lastItem == HomeScreen() }
                        } else if (navigator.canPop) {
                            navigator.pop()
                        }
                    }

                    is BrandProductsScreenUiEffect.OnNavigateToProductScreen -> navigator.push(
                        ProductScreen(effect.productId)
                    )

                    BrandProductsScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }
        }

        BrandProductsScreenContent(
            brandName = brandName,
            state = state,
            screenModel = screenModel,
            appModel = appScreenModel
        )

        BackHandler {
            if (fromHome) {
                navigator.popUntil { navigator.lastItem == HomeScreen() }
            } else if (navigator.canPop) {
                navigator.pop()
            }
        }
    }
}

@Composable
private fun BrandProductsScreenContent(
    brandName: String,
    state: BrandProductsScreenUiState,
    screenModel: BrandProductsScreenModel,
    appModel: AppScreenModel
) {
    val brandProducts = screenModel.brandProductsState.collectAsLazyPagingItems()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppBarWithIcon(
            title = brandName,
            onClickUpButton = screenModel::onClickBackButton,
            onClickNotification = screenModel::onClickNotification
        )
        AnimatedContent(targetState = state.isLoading, label = "") { isLoading ->
            if (isLoading) {
                LoadingContent()
            } else {

                if (brandProducts.itemCount != 0) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp
                        )
                    ) {
                        items(count = brandProducts.itemCount) { index ->
                            val brandProduct = brandProducts[index]!!
                            SalesItem(
                                id = brandProduct.id,
                                imageUrl = brandProduct.imageUrl,
                                isFavourite = brandProduct.isFavourite,
                                title = brandProduct.title,
                                description = brandProduct.description,
                                price = brandProduct.price,
                                afterDiscount = brandProduct.afterDiscount,
                                currencySymbol = state.currency.symbol,
                                exchangeRate = state.currency.exchangeRate,
                                onFavouriteClick = { productId, favoriteState ->
                                    appModel.updateFavoriteState(true)
                                    screenModel.onClickProductFavorite(productId, favoriteState)
                                },
                                onProductClick = { productId ->
                                    screenModel.onClickProduct(productId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.transparent),
        contentAlignment = Alignment.Center
    ) {
        if (!state.isProductsLoading) {
            if (brandProducts.itemCount == 0) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.image_error_icon),
                        contentDescription = ""
                    )
                }
            }
        } else {
            if (brandProducts.itemCount == 0) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = Theme.colors.primary
                    )
                }
            }
        }
    }
}


@Composable
fun SalesItem(
    id: Int,
    imageUrl: String,
    isFavourite: Boolean,
    title: String,
    description: String,
    price: Double,
    afterDiscount: Double,
    currencySymbol: String,
    exchangeRate: Double,
    onFavouriteClick: (Int, Boolean) -> Unit,
    onProductClick: (Int) -> Unit
) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(240.dp)
            .padding(end = 8.dp, start = 8.dp, bottom = 8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colors.white)
            .clickable {
                onProductClick(id)
            },
        contentAlignment = Alignment.BottomStart
    ) {
        Box(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            CoilImage(
                modifier = Modifier
                    .fillMaxHeight(0.50f)
                    .widthIn(max = 120.dp, min = 90.dp),
                url = imageUrl,
                contentDescription = "Product Image",
                scaleType = ContentScale.Fit
            )
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopEnd
        ) {
            val iconPainter =
                painterResource(
                    id = if (isFavourite) R.drawable.favourite_clicked
                    else R.drawable.favourite_unclicked
                )
            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .size(30.dp)
                    .noRippleEffect {
                        onFavouriteClick(id, isFavourite)
                    },
                painter = iconPainter,
                contentDescription = "FavoriteIcon"
            )
        }
        Column(modifier = Modifier.fillMaxHeight(0.47f)) {
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Theme.colors.black87,
                style = Theme.typography.caption
            )
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Theme.colors.black87,
                style = Theme.typography.caption
            )
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 2.dp
                ),
                text = NumberFormat.getNumberInstance().format(price * exchangeRate),
                textDecoration = TextDecoration.LineThrough,
                color = Theme.colors.silverGray,
                style = Theme.typography.caption.copy(fontSize = 11.sp)
            )
            Row(
                Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = "$currencySymbol ${
                        NumberFormat.getNumberInstance().format(afterDiscount * exchangeRate)
                    }",
                    color = Theme.colors.primary,
                    style = Theme.typography.title,
                )
            }
        }
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