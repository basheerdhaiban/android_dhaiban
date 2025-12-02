package com.semicolon.dhaiban.presentation.home

import ProductScreen
import SubCategoryScreen
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.compose.collectAsLazyPagingItems
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.DhaibanAlertDialog
import com.semicolon.dhaiban.designSystem.composables.DhaibanSnackBar
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.brand.BrandScreen
import com.semicolon.dhaiban.presentation.brandproducts.BrandProductsScreen
import com.semicolon.dhaiban.presentation.flashsale.FlashSaleScreen
import com.semicolon.dhaiban.presentation.home.composable.BrandsComponent
import com.semicolon.dhaiban.presentation.home.composable.CategoriesComponent
import com.semicolon.dhaiban.presentation.home.composable.CustomDropdownMenu
import com.semicolon.dhaiban.presentation.home.composable.HeaderComponent
import com.semicolon.dhaiban.presentation.home.composable.ProductGridItem
import com.semicolon.dhaiban.presentation.home.composable.SalesComponent
import com.semicolon.dhaiban.presentation.home.composable.SliderComponent
import com.semicolon.dhaiban.presentation.home.composable.TopProducts
import com.semicolon.dhaiban.presentation.home.container.CategoriesTab
import com.semicolon.dhaiban.presentation.home.container.HomeTab
import com.semicolon.dhaiban.presentation.home.container.ProfileTab
import com.semicolon.dhaiban.presentation.main.MainActivity
import com.semicolon.dhaiban.presentation.orders.OrdersScreen
import com.semicolon.dhaiban.presentation.search.SearchScreen
import com.semicolon.dhaiban.presentation.sharedUiState.SliderItemType
import com.semicolon.dhaiban.presentation.utils.Constants.HOME_SCREEN
import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import com.semicolon.dhaiban.presentation.utils.gridPagingItems
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.semicolon.dhaiban.presentation.utils.gridItems
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeScreen : Screen {
    override val key: ScreenKey = uniqueScreenKey
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<HomeScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        appScreenModel.setContext(LocalContext.current)
        val state: HomeScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val searchProducts by screenModel.searchProducts.collectAsState()
        val appScreenState by appScreenModel.state.collectAsState()

        LaunchedEffect(Unit) {
//            screenModel.getUserData()
            Log.d("HomeScreen",appScreenState.stringRes.myProfile.isBlank().toString())
            if (appScreenState.stringRes.myProfile.isBlank()){
                appScreenModel.getDefaultData()}
            if (appScreenModel.getFavoriteState()) {
                screenModel.updateHomeData()
            }
            screenModel.getCountry()

            screenModel.getUserData()
            screenModel.getMainCategories()
            screenModel.getFeaturedBrands()
            screenModel.getSliderItems()
            screenModel.getSaleProducts()
            screenModel.getProductTypes()
//            screenModel.getNewProducts()

            appScreenModel.setCurrentScreen(HOME_SCREEN)

            screenModel.updateCurrencySymbol(appScreenModel.getCurrencySymbol())

            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())

            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    HomeScreenUiEffect.OnNavigateToCategoriesScreen -> {
                        navigator?.parent?.let { parentNav ->
                            SafeNavigator.safeReplace(
                                parentNav, CategoriesTab,
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }

                    is HomeScreenUiEffect.OnNavigateToProductDetailsScreen -> {
                        navigator?.let { nav ->
                            SafeNavigator.safePush(
                                nav, ProductScreen(effect.productId),
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }

                    is HomeScreenUiEffect.OnNavigateToSubCategoryScreen -> {
                        navigator?.let { nav ->
                            SafeNavigator.safePush(
                                nav, SubCategoryScreen(effect.productId, effect.categoryTitle),
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }

                    HomeScreenUiEffect.OnNotifyDataUpdated -> {
                        appScreenModel.updateFavoriteState(false)
                    }

                    HomeScreenUiEffect.OnNavigateToBrandsScreen -> {
                        navigator?.let { nav ->
                            SafeNavigator.safePush(
                                nav, BrandScreen(),
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }
                    HomeScreenUiEffect.OnNavigateToFlashSaleScreen -> {
                        navigator?.let { nav ->
                            SafeNavigator.safePush(
                                nav, FlashSaleScreen(),
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }
                    is HomeScreenUiEffect.OnNavigateToBrandProductsScreen -> {
                        navigator?.let { nav ->
                            SafeNavigator.safePush(
                                nav, BrandProductsScreen(effect.brandTitle, effect.brandId, true),
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }

                    HomeScreenUiEffect.OnNavigateToProfileScreen -> {
                        navigator?.parent?.let { parentNav ->
                            SafeNavigator.safeReplace(
                                parentNav, ProfileTab,
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }
                    HomeScreenUiEffect.OnNavigateToHome -> {
                        navigator?.let { nav ->
                            SafeNavigator.safePop(
                                nav,
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                        navigator?.parent?.let { parentNav ->
                            SafeNavigator.safeReplace(
                                parentNav, HomeTab,
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }

                    is HomeScreenUiEffect.OnNavigateToTrackOrder -> {
                        navigator?.let { nav ->
                            SafeNavigator.safeReplace(
                                nav, OrdersScreen(orderId = effect.orderId),
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }
                    HomeScreenUiEffect.OnNavigateToSearchScreen -> {
                        navigator?.let { nav ->
                            SafeNavigator.safePush(
                                nav, SearchScreen(),
                                lifecycleOwner = lifecycleOwner
                            )
                        }
                    }
                }
            }
        }

        HomeScreenContent(
            authorized = state.userData.isAuthenticated,
            searchProducts = searchProducts,
            state = state,
            screenModel = screenModel,
        )
        DhaibanSnackBar(
            icon = R.drawable.icon_language,
            iconBackgroundColor = Color.Transparent,
            iconTint = Theme.colors.white,
            isVisible = state.errorMessage.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .safeDrawingPadding()
        ) {
            Text(
                text = state.errorMessage, style = Theme.typography.body, color = Theme.colors.white
            )
        }
        DhaibanSnackBar(
            icon = R.drawable.icon_language,
            iconBackgroundColor = Color.Transparent,
            iconTint = Theme.colors.white,
            isVisible = state.errorMessage.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .safeDrawingPadding()
        ) {
            Text(
                text = state.errorMessage,
                style = Theme.typography.body,
                color = Theme.colors.white
            )
        }
        AnimatedVisibility(
            visible = state.statusPaymentMomo=="SUCCESSFUL",
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
            ) {
                DhaibanAlertDialog(title = Theme.strings.paymentSuccess,
                    body = {
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = Theme.strings.thankYou, style = Theme.typography.title
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Image(
                                    painter = painterResource(id = R.drawable.heart_icon),
                                    contentDescription = "Heart Icon"
                                )
                            }
                            Image(
                                painter = painterResource(id = R.drawable.payment_success_icon),
                                contentDescription = "Payment Success Image"
                            )
                            Text(text = "${Theme.strings.orderNumber} ${state.orderUiState.orderId}")
                        }
                    },
                    positiveText = Theme.strings.trackOrder,
                    negativeText = Theme.strings.backToHome,
                    onDismiss = { screenModel.onClickBackToHome() },
                    onPositive = {
                        screenModel.onClickTrackOrder(state.orderUiState.orderId)
                    })
            }

        }

        BackHandler {
            if (searchProducts.isNotEmpty()) {
                screenModel.cancelSearch()
            } else {
                screenModel.onClickBack()
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenContent(
    authorized: Boolean,
    searchProducts: List<ProductUiState>,
    state: HomeScreenUiState,
    screenModel: HomeScreenModel,
) {

    Log.d("exchangeRate",state.homeCurrencyUiState.exchangeRate.toString())
    var selectedOptionId by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    var showToast by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(Unit) { onDispose { coroutineScope.cancel() } }
    val pullToRefreshState = rememberPullToRefreshState()

    val productList = screenModel.productsState.collectAsLazyPagingItems()
    val newProductList = screenModel.newProductsState.collectAsLazyPagingItems()

    AnimatedContent(targetState = state.isLoading, label = "") { isLoading ->
        if (isLoading)
            LoadingContent()
        else {
            PullToRefreshBox(
                isRefreshing =state._isRefreshing,
                onRefresh = screenModel::onPullToRefreshTrigger,

            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .safeDrawingPadding(),
                    contentPadding = PaddingValues(vertical = 16.dp),
                    userScrollEnabled = searchProducts.isEmpty()
                ) {
                    item {
                        HeaderComponent(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            profile = state.userData.username,
                            image = state.userData.imageUrl
                        ) {
                            screenModel.onClickProfile()
                        }
                        if (state.errorMessage.isEmpty()) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    enabled = false,
                                    modifier = Modifier
                                        .noRippleEffect { screenModel.onClickSearch() }
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .height(56.dp),
                                    value = state.queryValue,
                                    onValueChange = {
                                        screenModel.onChangeSearchValue(it)
                                    },
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Theme.colors.primary,
                                        unfocusedIndicatorColor = Theme.colors.primary,
                                        unfocusedContainerColor = Theme.colors.background,
                                        disabledContainerColor = Theme.colors.background,
                                        disabledIndicatorColor = Theme.colors.primary,
                                        focusedContainerColor = Theme.colors.splashBackground,
                                        cursorColor = Theme.colors.mediumBrown,
                                        selectionColors = TextSelectionColors(
                                            handleColor = Theme.colors.mediumBrown,
                                            backgroundColor = Theme.colors.darkBeige
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp),
                                    placeholder = {
                                        Text(Theme.strings.search, style = Theme.typography.body,
                                            color = Theme.colors.black38)
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(id = R.drawable.search_icon),
                                            contentDescription = "search icon",
                                            tint = LocalContentColor.current.copy(alpha = 0.5f)
                                        )
                                    }
                                )
                                CustomDropdownMenu(
                                    options = searchProducts,
                                    shape = RoundedCornerShape(16.dp),
                                    onOptionSelected = { screenModel.onClickProduct(it) }
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            CategoriesComponent(
                                categories = state.categories,
                                onViewAllCLicked = { screenModel.onClickViewAllCategories() })
                            { categoryId, categoryTitle ->
                                screenModel.onClickCategory(categoryId, categoryTitle)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            if (state.sliderItems.isNotEmpty()) {
                                SliderComponent(
                                    sliderItems = state.sliderItems,
                                    modifier = Modifier
                                        .height(180.dp)
                                        .padding(horizontal = 16.dp)
                                ) { type, id, title ->
                                    when (type) {
                                        SliderItemType.PRODUCT -> screenModel.onClickProduct(id)
                                        SliderItemType.CATEGORY -> screenModel.onClickCategory(
                                            id,
                                            title
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            BrandsComponent(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                brands = state.brands,
                                onViewAllCLicked = { screenModel.onClickViewAllBrands() })
                            { brandId, brandTitle ->
                                screenModel.onClickBrand(brandId, brandTitle)
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            SalesComponent(
                                salesProducts = state.saleProducts,
                                currencySymbol = state.homeCurrencyUiState.symbol,
                                exchangeRate = state.homeCurrencyUiState.exchangeRate,
                                onViewAllCLicked = { screenModel.onClickViewAllSaleProducts() },
                                onFavouriteClick = { productId, isFavorite ->
                                    if (authorized) {
                                        screenModel.onClickProductFavorite(
                                            productId,
                                            true,
                                            isFavorite
                                        )
                                    } else {
                                        showToast = true
                                        coroutineScope.launch {
                                            delay(2000)
                                            showToast = false
                                        }
                                    }
                                },
                                onAddToCartClick = {},
                            ) { productId ->
                                screenModel.onClickProduct(productId )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    if (state.errorMessage.isEmpty()) {
                        stickyHeader {
                            TopProducts(
                                selectedId = selectedOptionId,
                                productTypes = state.productTypes,
                                onNewProductsClick = {
                                    selectedOptionId = 0
                                    screenModel.onClickNewProducts()
                                },
                                onProductTypeClicked = {
                                selectedOptionId = it
                                screenModel.onClickProductType(it)
                            })
                        }
                        /*if (selectedOptionId == 0) {
                            if (state.selectedList.isNotEmpty()) { // New Product View
                                gridItems(
                                    data = state.selectedList, 2, modifier = Modifier.padding(16.dp)
                                ) { itemData ->
                                    ProductGridItem(
                                        productId = itemData.id,
                                        imageUrl = itemData.imageUrl,
                                        productTitle = itemData.title,
                                        price = itemData.price,
                                        afterDiscount = itemData.afterDiscount,
                                        currencySymbol = state.homeCurrencyUiState.symbol,
                                        exchangeRate = state.homeCurrencyUiState.exchangeRate,
                                        isFavourite = itemData.isFavourite,
                                        onFavouriteClick = { productId, isFavorite ->
                                            if (authorized) {
                                                screenModel.onClickProductFavorite(
                                                    productId,
                                                    false,
                                                    isFavorite
                                                )
                                            } else {
                                                showToast = true
                                                coroutineScope.launch {
                                                    delay(2000)
                                                    showToast = false
                                                }
                                            }
                                        },
                                        onAddToCartClick = {},
                                        onProductClick = { productId ->
                                            screenModel.onClickProduct(productId)
                                        }
                                    )
                                }
                                gridPagingItems(
                                    modifier = Modifier.padding(16.dp),
                                    isLoading = state.isNewProductsLoading,
                                    data = newProductList,
                                    columnCount = 2,
                                ) { itemData ->
                                    ProductGridItem(
                                        productId = itemData.id,
                                        imageUrl = itemData.imageUrl,
                                        productTitle = itemData.title,
                                        price = itemData.price,
                                        afterDiscount = itemData.afterDiscount,
                                        currencySymbol = state.homeCurrencyUiState.symbol,
                                        exchangeRate = state.homeCurrencyUiState.exchangeRate,
                                        isFavourite = itemData.isFavourite,
                                        onFavouriteClick = { productId, isFavorite ->
                                            if (authorized) {
                                                screenModel.onClickProductFavorite(
                                                    productId,
                                                    false,
                                                    isFavorite
                                                )
                                            } else {
                                                showToast = true
                                                coroutineScope.launch {
                                                    delay(2000)
                                                    showToast = false
                                                }
                                            }
                                        },
                                        onAddToCartClick = {},
                                    ) { productId ->
                                        screenModel.onClickProduct(productId)
                                    }
                                }
                            } else {
                                item {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(25.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.image_error_icon),
                                            contentDescription = ""
                                        )
                                    }
                                }
                            }

                        }
                        else {*/
                            gridPagingItems(
                                modifier = Modifier.padding(16.dp),
                                isLoading = state.isGroupProductsLoading,
                                data = productList,
                                columnCount = 2,
                            ) { itemData ->
                                ProductGridItem(
                                    productId = itemData.id,
                                    imageUrl = itemData.imageUrl,
                                    productTitle = itemData.title,
                                    price = itemData.price,
                                    afterDiscount = itemData.afterDiscount,
                                    currencySymbol = state.homeCurrencyUiState.symbol,
                                    exchangeRate = state.homeCurrencyUiState.exchangeRate,
                                    isFavourite = itemData.isFavourite,
                                    onFavouriteClick = { productId, isFavorite ->
                                        if (authorized) {
                                            screenModel.onClickProductFavorite(
                                                productId,
                                                false,
                                                isFavorite
                                            )
                                        } else {
                                            showToast = true
                                            coroutineScope.launch {
                                                delay(2000)
                                                showToast = false
                                            }
                                        }
                                    },
                                    onAddToCartClick = {},
                                ) { productId ->
                                    screenModel.onClickProduct(productId)
                                }
                            }
//                        }
                    }
                }

//                PullToRefreshBox(
//                    state = pullToRefreshState,
//                    modifier = Modifier.align(Alignment.TopCenter),
//                    contentColor = Theme.colors.primary
//                )
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

    AnimatedVisibility(
        visible = state.showExitDialog,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            DhaibanAlertDialog(
                title = Theme.strings.areYouSure,
                body = {
                    Column(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.payment_success_icon),
                            contentDescription = "Payment Success Image"
                        )
                    }
                },
                positiveText = Theme.strings.exit,
                negativeText = Theme.strings.stay,
                onDismiss = { screenModel.onDismissExit() },
                onPositive = {
                    (context as? MainActivity)?.finish()
                }
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(18.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(180.dp)
                        .height(80.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(60.dp)
                        .height(40.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
            Box(
                modifier = Modifier
                    .width(50.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(180.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeContentPreview() {
    DhaibanTheme {
        LoadingContent()
    }
}