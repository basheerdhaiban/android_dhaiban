import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
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
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.DhaibanProduct
import com.semicolon.dhaiban.designSystem.composables.DhaibanSearchBar
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.home.HomeScreen
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.search.SearchScreen
import com.semicolon.dhaiban.presentation.sharedUiState.SliderItemType
import com.semicolon.dhaiban.presentation.subCategory.SubCategoryScreenModel
import com.semicolon.dhaiban.presentation.subCategory.SubCategoryScreenUiEffect
import com.semicolon.dhaiban.presentation.subCategory.SubCategoryScreenUiState
import com.semicolon.dhaiban.presentation.subCategory.composables.FilterComponent
import com.semicolon.dhaiban.presentation.subCategory.composables.SliderComponent
import com.semicolon.dhaiban.presentation.subCategory.composables.SubCategoryComponent
import com.semicolon.dhaiban.presentation.utils.Constants.SUBCATEGORY_SCREEN
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class SubCategoryScreen(
    private val id: Int,
    private val title: String
) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<SubCategoryScreenModel>(parameters = { parametersOf(id) })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: SubCategoryScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current

        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(SUBCATEGORY_SCREEN)
            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    is SubCategoryScreenUiEffect.OnNavigateToCategories -> {
                        navigator.popUntil { navigator.lastItem == HomeScreen() }
                    }

                    is SubCategoryScreenUiEffect.OnNavigateToProductDetailsScreen -> {
                        SafeNavigator.safePush(
                            navigator, ProductScreen(effect.productId),
                            lifecycleOwner = lifecycleOwner
                        )
                    }

                    SubCategoryScreenUiEffect.OnNavigateToSearchScreen -> {
                        SafeNavigator.safePush(navigator, SearchScreen(),
                            lifecycleOwner = lifecycleOwner)
                    }

                    SubCategoryScreenUiEffect.OnNavigateToNotificationScreen -> SafeNavigator.safePush(navigator, 
                        NotificationScreen(),
                        lifecycleOwner = lifecycleOwner
                    )
                }
            }
        }

        SubCategoryScreenContent(
            authorized = state.isAuthorized,
            title = title,
            state = state,
            screenModel = screenModel,
            categoryId = id
        )

        BackHandler {
            if (state.isFilterSelected) {
                screenModel.hideFilter()
            } else {
                navigator.popUntil { navigator.lastItem == HomeScreen() }
            }
        }
    }
}

@Composable
private fun SubCategoryScreenContent(
    authorized: Boolean,
    title: String,
    categoryId: Int,
    state: SubCategoryScreenUiState,
    screenModel: SubCategoryScreenModel
) {
    var searchInput by remember { mutableStateOf("") }
    var sortBySearchId by remember { mutableIntStateOf(0) }
    var categorySearchId by remember { mutableIntStateOf(categoryId) }
    var colorSearchId by remember { mutableIntStateOf(0) }
    var priceSearchValues by remember { mutableStateOf(0 to 1000) }
    var searchAttributes = mutableMapOf<Int, MutableList<String>>()
    val context = LocalContext.current
    var showToast by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(Unit) { onDispose { coroutineScope.cancel() } }
    val subCategoryProducts = screenModel.subCategoryProductsState.collectAsLazyPagingItems()

    AnimatedContent(targetState = state.isFilterSelected, label = "") { isFilterSelected ->
        if (isFilterSelected) {
            FilterComponent(
                categoryId = categoryId,
                attributes = state.attributes,
                colors = state.colors,
                categories = state.subCategories,
                maxPrice = state.maxPrice,
                minPrice = state.minPrice,
                currencySymbol = state.currency.symbol,
                onBackClick = { screenModel.hideFilter() },
                onChooseCategoryItem = { categoryId ->
                    categorySearchId = categoryId
                },
                onChooseSortByItem = { sortId ->
                    sortBySearchId = sortId
                },
                onChoosePrice = { minPrice, maxPrice ->
                    priceSearchValues = minPrice.toInt() to maxPrice.toInt()
                },
                onChooseColorItem = { colorId ->
                    colorSearchId = colorId
                },
                onClearDynamicOption = { parentId, optionId ->
                    searchAttributes[parentId]?.remove(optionId.toString())
                    if (searchAttributes[parentId].isNullOrEmpty()) {
                        searchAttributes.remove(parentId)
                    }
                    Log.e("remove", searchAttributes.toString())
                },
                onChooseDynamicOption = { parentId, optionId ->
                    if (searchAttributes.containsKey(parentId)) {
                        searchAttributes[parentId]?.add(optionId.toString())
                    } else {
                        searchAttributes[parentId] = mutableListOf(optionId.toString())
                    }
                    Log.e("add", searchAttributes.toString())
                }, onAllCleared = {
                    searchAttributes = mutableMapOf()
                }
            ) {
                screenModel.onClickApplyFilter(
                    categorySelected = categorySearchId != categoryId,
                    categorySearchId,
                    sortBySearchId,
                    colorSearchId,
                    priceSearchValues.first,
                    priceSearchValues.second,
                    searchAttributes
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AppBarWithIcon(
                    title = title,
                    onClickUpButton = screenModel::onClickBackButton,
                    onClickNotification = screenModel::onClickNotification
                )
                AnimatedContent(targetState = state.isLoading, label = "") { isLoading ->
                    if (isLoading) {
                        LoadingContent()
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .safeDrawingPadding(),
                        ) {

                            Spacer(modifier = Modifier.height(16.dp))

                            AnimatedVisibility(visible = state.firstItemIsVisible) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    DhaibanSearchBar(
                                        modifier = Modifier
                                            .fillMaxWidth(0.80f)
                                            .noRippleEffect { screenModel.onClickSearch() },
                                        value = searchInput,
                                        hint = Theme.strings.search,
                                        onQueryChanged = { searchInput = it },
                                        enabled = false,
                                        colors = TextFieldDefaults.colors(
                                            focusedIndicatorColor = Theme.colors.primary,
                                            unfocusedIndicatorColor = Theme.colors.primary100,
                                            focusedContainerColor = Theme.colors.background,
                                            unfocusedContainerColor = Theme.colors.background,
                                            cursorColor = Theme.colors.primary,
                                            focusedTextColor = Theme.colors.black87,
                                            unfocusedTextColor = Theme.colors.black87
                                        ),
                                    )
                                    Image(
                                        modifier = Modifier
                                            .padding(8.dp)
                                            .size(30.dp)
                                            .noRippleEffect {
                                                screenModel.onClickFilter(state.isFilterSelected)
                                            },
                                        painter = painterResource(id = R.drawable.filter_icon),
                                        contentDescription = "FavoriteIcon"
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                            }

                            SubCategoryComponent(
                                subCategories = state.subCategories,
                                selectedSubCategory = state.selectedSubCategory
                            ) { subCategoryId ->
                                if (subCategoryId == -1)
                                    screenModel.onClickSubCategory(categoryId)
                                else {
                                    if (subCategoryId != state.selectedSubCategory) {
                                        screenModel.onClickSubCategory(subCategoryId)
                                    } else {
                                        screenModel.onClickSubCategory(categoryId)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            AnimatedVisibility(visible = state.sliderItems.isNotEmpty() && state.firstItemIsVisible) {
                                SliderComponent(
                                    modifier = Modifier
                                        .height(150.dp)
                                        .padding(horizontal = 16.dp),
                                    sliderItems = state.sliderItems
                                ) { type, id, _ ->
                                    when (type) {
                                        SliderItemType.PRODUCT -> screenModel.onClickProduct(id)
//                                            SliderItemType.CATEGORY -> screenModel.onClickCategory(
//                                                id,
//                                                title
//                                            )
                                        else -> {}
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            val subCategoryListState = rememberLazyGridState()
                            val firstVisibleItem by remember { derivedStateOf { subCategoryListState.firstVisibleItemIndex == 0 } }
                            screenModel.updateFirstItemState(firstVisibleItem) //update the state to inspect if the first item of the products is shown or not
                            if (subCategoryProducts.itemCount != 0) {
                                LazyVerticalGrid(
                                    columns = GridCells.Fixed(2),
                                    contentPadding = PaddingValues(
                                        vertical = 8.dp,
                                        horizontal = 16.dp
                                    ),
                                    state = subCategoryListState
                                ) {
                                    items(count = subCategoryProducts.itemCount) { index ->
                                        val subCategoryProduct = subCategoryProducts[index]!!
                                        Log.d("SubCategoryScreen",subCategoryProduct.price.toString() +subCategoryProduct.title.toString())

                                        DhaibanProduct(
                                            productId = subCategoryProduct.id,
                                            imageUrl = subCategoryProduct.imageUrl,
                                            productTitle = subCategoryProduct.title,
                                            price = subCategoryProduct.price,
                                            afterDiscount = subCategoryProduct.afterDiscount,
                                            currencySymbol = state.currency.symbol,
                                            exchangeRate = state.currency.exchangeRate,
                                            isFavourite = subCategoryProduct.isFavourite,
                                            onFavouriteClick = { productId, isFavorite ->
                                                if (authorized) {
                                                    screenModel.onClickProductFavorite(
                                                        productId,
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
                                        ) { productId ->
                                            screenModel.onClickProduct(productId)
                                        }
                                    }


                                }
                            } else {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {

                                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center, modifier = Modifier.padding(bottom = 80.dp)) {
                                    Image(
                                        painter = painterResource(id = R.drawable.thereisnotprrodcut),
                                        contentDescription = ""
                                    )
                                    Text(
                                        text = Theme.SecondStrings.thereIsNoProduct  ,
                                        color = Theme.colors.black,
                                        style = Theme.typography.body.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            fontSize = 13.sp
                                        )
                                    )
                                }

//                                    Image(
//                                        painter = painterResource(id = R.drawable.image_error_icon),
//                                        contentDescription = ""
//                                    )
                                }

                            }
                        }
                    }
                }

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


@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.80f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.width(32.dp))
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                repeat(3) {
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            repeat(10) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(55.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .width(30.dp)
                            .height(4.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shimmerEffect()
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .padding(end = 8.dp)
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(16.dp))

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
        Spacer(modifier = Modifier.height(16.dp))

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
fun SubCategoryContentPreview() {
    DhaibanTheme {
        LoadingContent()
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun SubCategoryScreenContentPreview() {
//    val dummyListener = object : SubCategoryScreenInteractionListener {
//        override fun onClickBackButton() {}
//    }
//    DhaibanTheme {
//        SubCategoryScreenContent(
//            title = "Fashion",
//            state = SubCategoryScreenUiState(
//                subCategories = listOf(
//                    SubCategoryUiState(1, "Women", ""),
//                    SubCategoryUiState(2, "Men", ""),
//                    SubCategoryUiState(3, "Kids", ""),
//                    SubCategoryUiState(4, "Shoes", ""),
//                ),
//                subCategoryProducts = listOf()
//            ),
//            screenModel = dummyListener
//        )
//
//    }
//}
