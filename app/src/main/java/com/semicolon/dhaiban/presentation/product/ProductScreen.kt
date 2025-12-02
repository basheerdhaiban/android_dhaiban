import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner

import android.graphics.Color.parseColor
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.DhaibanAlertDialog
import com.semicolon.dhaiban.designSystem.composables.DhaibanBottomDialog
import com.semicolon.dhaiban.designSystem.composables.DhaibanThreeDotLoadingIndicator
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.authuntication.login.LoginScreen
import com.semicolon.dhaiban.presentation.home.container.CartTab
import com.semicolon.dhaiban.presentation.product.ChoiceOptionUiState
import com.semicolon.dhaiban.presentation.product.ColorUiState
import com.semicolon.dhaiban.presentation.product.OptionUiState
import com.semicolon.dhaiban.presentation.product.PhotoUiState
import com.semicolon.dhaiban.presentation.product.ProductDetailsUiState
import com.semicolon.dhaiban.presentation.product.ProductScreenInteractionListener
import com.semicolon.dhaiban.presentation.product.ProductScreenModel
import com.semicolon.dhaiban.presentation.product.ProductScreenUiEffect
import com.semicolon.dhaiban.presentation.product.ProductScreenUiState
import com.semicolon.dhaiban.presentation.product.ReviewUiState
import com.semicolon.dhaiban.presentation.product.composables.ImageViewerComponent
import com.semicolon.dhaiban.presentation.product.composables.OptionComponent
import com.semicolon.dhaiban.presentation.product.composables.OptionContentComponent
import com.semicolon.dhaiban.presentation.product.composables.OptionType
import com.semicolon.dhaiban.presentation.product.composables.ProductImageComponent
import com.semicolon.dhaiban.presentation.product.composables.RecommendedProductsComponent
import com.semicolon.dhaiban.presentation.utils.Constants.PRODUCT_SCREEN
import com.semicolon.dhaiban.presentation.utils.root
import com.semicolon.dhaiban.utils.PaymentMethodsType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import java.text.NumberFormat

data class ProductScreen(
    private val productId: Int,
    private val variant: String = "",
    private val discountPrice: String = ""
) : Screen {
    override val key: ScreenKey = uniqueScreenKey


    @Composable
    override fun Content() {
        val screenModel =
            getScreenModel<ProductScreenModel>(parameters = { parametersOf(productId, variant) })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: ProductScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current

        val context = LocalContext.current
        val notAvailable = Theme.strings.notAvailable
        val addedSuccessfully = Theme.strings.addedSuccessfully
        val someThingWentWrong = Theme.strings.someThingWentWrong
        val chooseVariantFirst = Theme.strings.chooseVariantFirst

        LaunchedEffect(state.productDetails) {
//            lowerPrice = calculatePrice(
//                state.productDetails.lowerPrice,
//                state.productDetails.discount,
//                state.productDetails.discountType
//            )
//            upperPrice = calculatePrice(
//                state.productDetails.higherPrice,
//                state.productDetails.discount,
//                state.productDetails.discountType
//            )
//            variantPrice = calculatePrice(
//                state.productDetails.variantPrice,
//                state.productDetails.discount,
//                state.productDetails.discountType
//            )

        }

        LaunchedEffect(Unit) {

            appScreenModel.setCurrentScreen(PRODUCT_SCREEN)

            screenModel.getUserData()

            screenModel.updateCurrencySymbol(appScreenModel.getCurrencySymbol())
            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())

            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    is ProductScreenUiEffect.OnNavigateBack -> if (navigator.canPop) SafeNavigator.safePop(navigator, lifecycleOwner)
                    is ProductScreenUiEffect.OnNavigateToProductDetails -> navigator.apply {
                        push(ProductScreen(effect.productId))
                    }

                    ProductScreenUiEffect.OnVariantNotAvailable -> {
                        Toast.makeText(
                            context, notAvailable,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is ProductScreenUiEffect.OnAddedToCart -> {
                        if (effect.state) Toast.makeText(
                            context, addedSuccessfully,
                            Toast.LENGTH_SHORT
                        ).show()
                        else Toast.makeText(
                            context, someThingWentWrong,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    ProductScreenUiEffect.OnNeedVariant -> Toast.makeText(
                        context, chooseVariantFirst,
                        Toast.LENGTH_SHORT
                    ).show()

                    is ProductScreenUiEffect.OnShowMessage -> Toast.makeText(
                        context, effect.message, Toast.LENGTH_SHORT
                    ).show()

                    ProductScreenUiEffect.OnNavigateToWelcomeScreen -> {
                        // Use a safer navigation approach for login redirect
                        (navigator.parent?.parent ?: navigator).replaceAll(LoginScreen)
                    }

                    ProductScreenUiEffect.OnNavigateToCartScreen -> {
                        if (variant.isEmpty()) { //is from cart
                            if (navigator.canPop) SafeNavigator.safePop(navigator, lifecycleOwner)
                            navigator.parent?.let { parentNav -> SafeNavigator.safeReplace(
                                parentNav, CartTab,
                                lifecycleOwner = lifecycleOwner
                            ) }
                        } else if (navigator.canPop) {
                            SafeNavigator.safePop(navigator, lifecycleOwner)
                        }

                    }

                    is ProductScreenUiEffect.OnUpdateCartCount -> {
                        appScreenModel.updateCartItemsNumber(effect.cartCount)
                    }
                }
            }
        }

        ProductScreenContent(
            state = state,
            listener = screenModel,
            appModel = appScreenModel,
            screenModel = screenModel
        )

        BackHandler {
            if (state.isExpandImagesSelected) {
                screenModel.hideImages()
            } else if (navigator.canPop) {
                SafeNavigator.safePop(navigator, lifecycleOwner)
            }
        }

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ProductScreenContent(
    state: ProductScreenUiState,
    listener: ProductScreenInteractionListener,

    appModel: AppScreenModel? = null,
    screenModel: ProductScreenModel? = null,
) {
    Log.d("exchangeRatess",state.currency.exchangeRate.toString())
    var lowerPrice by remember { mutableStateOf<Double?>(null) }
    var upperPrice by remember { mutableStateOf<Double?>(null) }
    var variantPrice by remember { mutableStateOf<Double?>(null) }
    Log.d("ProductScreenContent", state.items.count().toString())
    val context = LocalContext.current
    var selectedOption by remember { mutableStateOf(OptionType.DESCRIPTION) }
    var productCount by remember { mutableIntStateOf(0) }
    var itemCount by remember {
        mutableStateOf(0)
    }
    itemCount = state.items.count()
    productCount = if (state.productDetails.identifier == 0L) {
        if (state.productDetails.stockCount > 0) 1 else 0
    } else {
        state.productDetails.cartQuantity
    }
    var showToast by remember { mutableStateOf(false) }
    var buttonState by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    DisposableEffect(Unit) { onDispose { coroutineScope.cancel() } }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (state.isExpandImagesSelected.not()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
                shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            modifier = Modifier
                                .scale(
                                    scaleX = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f,
                                    scaleY = 1f
                                )
                                .noRippleEffect { listener.onClickUpButton() },
                            painter = painterResource(id = R.drawable.bar_back_icon),
                            contentDescription = "navigation back icon"
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(0.80f),
                            text = state.productDetails.seller,
                            style = Theme.typography.headline,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1
                        )
                    }
                    Row(modifier = Modifier
                        .noRippleEffect {
                            listener.onClickCartIcon()
                        }) {
                        Log.d("item Count", itemCount.toString())
                        Box {
                            Box(
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.Center)
                            ) {
                                Image(
                                    modifier = Modifier.align(Alignment.Center),
                                    painter = painterResource(id = R.drawable.icon_cart_selected),
                                    contentDescription = "Cart Icon"
                                )
                            }
                            Badge(
                                modifier = Modifier.align(Alignment.TopEnd),
                                containerColor = Theme.colors.veryLightBrown,
                                contentColor = Theme.colors.white
                            ) {
                                Text(
                                    text =
                                    if (state.userData.isAuthenticated)
                                        itemCount.toString()
                                    else
                                        "0",
                                    color = Theme.colors.white,
                                    style = Theme.typography.caption
                                )
                            }
                        }
                    }
                }
            }

            AnimatedContent(targetState = state.isLoading, transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 800, delayMillis = 900)) with
                        fadeOut(animationSpec = tween(durationMillis = 300))
            }, label = "") { loading ->
                if (loading) {
                    LoadingContent()
                } else {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .verticalScroll(state = rememberScrollState())
                                .padding(bottom = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(32.dp))

                            ProductImageComponent(productId = state.productDetails.productId,
                                selectedImage = state.productDetails.selectedImage,
                                isFavourite = state.productDetails.isFavourite,
                                onFavouriteClick = { productId, favoriteState ->
                                    appModel!!.updateFavoriteState(true)
                                    listener.onClickProductFavorite(productId, favoriteState)
                                }) {
                                listener.onClickImage(state.isExpandImagesSelected)
                            }
                            Spacer(modifier = Modifier.height(16.dp))

                            ProductImages(
                                images = state.productDetails.productImages,
                                selectedImageId = state.productDetails.selectedImageId
                            ) { imageId ->
                                listener.onClickSmallImage(imageId)
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {


                                Text(
                                    text = state.productDetails.title,
                                    textAlign = TextAlign.Start,

                                    style = Theme.typography.headline,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {

                                Column(
                                    modifier = Modifier.fillMaxWidth(0.58f),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.Start
                                ) {

                                    Text(
                                        text = state.productDetails.shortDescription,
                                        style = Theme.typography.body,
                                        textAlign = TextAlign.Start,
                                        color = Theme.colors.dimGray
                                    )
                                    Log.d("brandNamess", state.productDetails.brandName)
                                    Log.d("brandphoto", state.productDetails.brandPhoto)
                                    if (state.productDetails.brandName.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(15.dp))
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Box(
                                                modifier = Modifier
                                                    .padding(all = 8.dp)
                                                    .clip(shape = RoundedCornerShape(8.dp))
                                                    .height(40.dp)
                                                    .width(50.dp)
                                                    .background(Theme.colors.transparent),
                                                contentAlignment = Alignment.Center

                                            ) {
                                                CoilImage(
                                                    url = "https://admin.dhaibantrading.com/" + state.productDetails.brandPhoto,
                                                    contentDescription = "Brand Logo",
                                                    shape = RoundedCornerShape(0.dp),
                                                    scaleType = ContentScale.Crop,
                                                )
                                            }
                                            Text(
                                                text = state.productDetails.brandName,
                                                style = Theme.typography.body,
                                                textAlign = TextAlign.Start,
                                                color = Theme.colors.dimGray
                                            )
                                        }
                                    }

                                    if (

                                        (state.productDetails.variantQuantity <= 0 && state.productDetails.choiceOptions.isNotEmpty())
                                        or
                                        (state.productDetails.stockCount <= 0 && state.productDetails.choiceOptions.isEmpty())
                                    ) {

                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = Theme.strings.outOfStock,
                                            style = Theme.typography.body,
                                            textAlign = TextAlign.Start,
                                            color = Theme.colors.rustyRed
                                        )
                                    } else {
                                    }
                                    if (state.productDetails.colors.isNotEmpty()) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.Start,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = "${Theme.strings.color}: ")
                                        }
                                        Spacer(modifier = Modifier.height(8.dp))
                                        ColorsComponent(
                                            colors = state.productDetails.colors,
                                            selectedColorId = state.productDetails.selectedColorId
                                        ) { colorId ->
                                            productCount = 0
                                            listener.onClickColor(colorId)
                                        }
                                    }
                                }
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalArrangement = Arrangement.Top,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Image(
                                            modifier = Modifier.padding(end = 2.dp),
                                            painter = painterResource(id = R.drawable.rate_icon),
                                            contentDescription = "FavoriteIcon"
                                        )
                                        Text(
                                            modifier = Modifier.padding(end = 4.dp),
                                            text = state.productDetails.rating.toInt().toString(),
                                            style = Theme.typography.caption,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,

                                            )
                                        Text(
                                            text = "(${state.productDetails.ratingCount} ${Theme.strings.reviews})",
                                            style = Theme.typography.caption,
                                            textAlign = TextAlign.Center,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        modifier = Modifier.padding(bottom = 2.dp),
                                        text = Theme.strings.price,
                                        style = Theme.typography.body,
                                        textAlign = TextAlign.Center,
                                        color = Theme.colors.greyishBrown
                                    )

                                    lowerPrice = calculatePrice(
                                        state.productDetails.lowerPrice,
                                        state.productDetails.discount,
                                        state.productDetails.discountType,
                                        state.currency.exchangeRate
                                    )
                                    upperPrice = calculatePrice(
                                        state.productDetails.higherPrice,
                                        state.productDetails.discount,
                                        state.productDetails.discountType,
                                        state.currency.exchangeRate
                                    )
                                    variantPrice = calculatePrice(
                                        state.productDetails.variantPrice,
                                        state.productDetails.discount,
                                        state.productDetails.discountType,
                                        state.currency.exchangeRate
                                    )


                                    // Create state holders for delayed price values

                                    // Helper function to format price with currency
                                    fun formatPrice(
                                        price: Double,
                                        exchangeRate: Double,
                                        symbol: String
                                    ): String {
                                        return NumberFormat.getNumberInstance()
                                            .format(price * exchangeRate) + " " + symbol
                                    }
                                    Log.d(
                                        "importantdetails",
                                        "${state.productDetails.lowerPrice.toString()}+ ${state.currency.exchangeRate.toString()}"
                                    )

                                    if (state.productDetails.discount != 0.0) {
                                        Log.d("statereview", "discount!=0")
                                        Text(
                                            text = if (state.productDetails.variantPrice == 0.0) String.format(
                                                "%.3f",
                                                state.productDetails.lowerPrice * state.currency.exchangeRate
                                            ) + " " + state.currency.symbol + " - " + String.format(
                                                "%.3f",
                                                state.productDetails.higherPrice * state.currency.exchangeRate
                                            ) + " " + state.currency.symbol
                                            else String.format(
                                                "%.3f",
                                                state.productDetails.variantPrice * state.currency.exchangeRate
                                            ) + " " + state.currency.symbol,

                                            textAlign = TextAlign.Center,
                                            textDecoration = TextDecoration.LineThrough,
                                            color = Theme.colors.silverGray,
                                            style = Theme.typography.caption.copy(fontSize = 11.sp)
                                        )
                                    }
                                    Log.d("sdaadsdsa","${state.currency.exchangeRate}")
                                    Text(
                                        text = if (state.productDetails.variantPrice == 0.0) String.format(
                                            "%.3f",
                                            lowerPrice?:0.0 *1
                                        ) + " " + state.currency.symbol + " - " + String.format(
                                            "%.3f",
                                            upperPrice?:0.0 * 1
                                        ) + " " + state.currency.symbol
                                        else String.format(
                                            "%.3f",
                                            variantPrice?:0.0* 1
                                        ) + " " + state.currency.symbol,
                                        style = Theme.typography.caption.copy(
                                            fontSize = 13.sp, fontWeight = FontWeight(800)
                                        ),
                                        textAlign = TextAlign.Center,
                                        color = Theme.colors.mediumBrown,
                                    )




                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                state.productDetails.choiceOptions.forEach { choiceOption ->
                                    ChoiceItem(
                                        choiceId = choiceOption.id,
                                        title = choiceOption.choiceTitle,
                                        options = choiceOption.options,
                                        selectedOptionId = choiceOption.selectedOptionId
                                    ) { choiceId, optionId ->
                                        productCount = 0
                                        listener.onClickOption(choiceId, optionId)
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            OptionComponent(modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .fillMaxWidth(),
                                onShareClick = {}) { optionType ->
                                selectedOption = optionType
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            OptionContentComponent(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                productDescription = state.productDetails.fullDescription,
                                productReviews = state.productReviews,
                                optionType = selectedOption,
                                properties = state.productDetails.properties
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            if (state.recommendedProducts.isNotEmpty()) {
                                Text(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp)
                                        .fillMaxWidth(),
                                    text = Theme.strings.recommendedForYou,
                                    style = Theme.typography.body.copy(fontWeight = FontWeight.Medium),
                                    textAlign = TextAlign.Start
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                RecommendedProductsComponent(
                                    products = state.recommendedProducts,
                                    currencySymbol = state.currency.symbol,
                                    exchangeRange = state.currency.exchangeRate
                                ) { productId ->
                                    listener.onClickRecommendedProduct(productId)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                        }
                        Card(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .background(Theme.colors.transparent)
                                .fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                            colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        ) {
                            Row {
                                Row(
                                    modifier = Modifier
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(0.35f)
                                            .padding(end = 16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = {
                                                if (productCount != 0) {
                                                    productCount--
                                                }
                                            },
                                            modifier = Modifier
                                                .padding(end = 16.dp)
                                                .size(30.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                contentColor = Theme.colors.buttonTextGray,
                                                containerColor = Theme.colors.antiqueLace
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 0.dp)

                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.minus_icon),
                                                contentDescription = "Minus Button"
                                            )
                                        }

                                        Text(
                                            text = productCount.toString(),
                                            modifier = Modifier.padding(end = 16.dp)
                                        )
                                        Button(
                                            onClick = {
                                                if (productCount < state.productDetails.stockCount) {
                                                    productCount++
                                                } else {
                                                    showToast = true
                                                    coroutineScope.launch {
                                                        delay(2000)
                                                        showToast = false
                                                    }
                                                }
                                            },
                                            modifier = Modifier.size(30.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                contentColor = Theme.colors.buttonTextGray,
                                                containerColor = Theme.colors.antiqueLace
                                            ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 0.dp)

                                        ) {
                                            if (showToast) {
                                                Toast.makeText(
                                                    context,
                                                    Theme.strings.notAvailable,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                            Image(
                                                painter = painterResource(id = R.drawable.plus_icon),
                                                contentDescription = "Plus Button"
                                            )
                                        }
                                    }

                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Button(
                                            onClick = {

                                                if (state.momo == PaymentMethodsType.MOMO.name) {
                                                    Toast.makeText(
                                                        context,
                                                        "You Should Finish Payment Process",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    if (productCount > 0) {
                                                        buttonState = false
                                                        itemCount++
                                                        listener.onCLickAddToCart(
                                                            state.productDetails.productId,
                                                            productCount
                                                        )
                                                    } else {
                                                        listener.showMessage("Quantity cant be zero")
                                                    }
                                                }
                                            },
                                            modifier = Modifier
                                                .height(40.dp)
                                                .fillMaxWidth(),
                                            colors =
                                            if (state.isInCart and state.userData.isAuthenticated)
                                                ButtonDefaults.buttonColors(
                                                    contentColor = Theme.colors.white,
                                                    containerColor = Theme.colors.darkBeige
                                                )
                                            else
                                                ButtonDefaults.buttonColors(
                                                    contentColor = Theme.colors.white,
                                                    containerColor = Theme.colors.mediumBrown
                                                ),
                                            shape = RoundedCornerShape(6.dp),
                                            contentPadding = PaddingValues(horizontal = 0.dp),
                                            enabled = buttonState
                                        ) {
                                            AnimatedContent(
                                                targetState = state.addToCartLoadingState,
                                                label = ""
                                            ) {
                                                if (it) {
                                                    DhaibanThreeDotLoadingIndicator(dotColor = Theme.colors.white)
                                                } else {
                                                    buttonState = true
                                                    Text(
                                                        text = stringResource(R.string.addToCart),
//                                                        if (state.isInCart and state.userData.isAuthenticated)
//                                                            Theme.strings.editCart
//                                                        else
//                                                            Theme.strings.addToCart
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                )
                                {
                                    Button(
                                        onClick = {
                                            if (productCount > 0) {
                                                listener.onCLickAddToCart(
                                                    state.productDetails.productId, productCount
                                                )
                                                itemCount++
                                            } else {
                                                listener.showMessage("Quantity cant be zero")
                                            }
                                        },
                                        modifier = Modifier
                                            .height(40.dp)
                                            .fillMaxWidth(),
                                        colors = if (state.isInCart) ButtonDefaults.buttonColors(
                                            contentColor = Theme.colors.white,
                                            containerColor = Theme.colors.darkBeige
                                        ) else ButtonDefaults.buttonColors(
                                            contentColor = Theme.colors.white,
                                            containerColor = Theme.colors.mediumBrown
                                        ),
                                        shape = RoundedCornerShape(6.dp),
                                        contentPadding = PaddingValues(horizontal = 0.dp),
                                    ) {
                                        AnimatedContent(
                                            targetState = state.addToCartLoadingState, label = ""
                                        ) {
                                            if (it) DhaibanThreeDotLoadingIndicator(dotColor = Theme.colors.white)
                                            else {
                                                Text(text = if (state.isInCart) Theme.strings.editCart else Theme.strings.addToCart)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }

                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        AnimatedContent(targetState = state.showDialog, label = "") {
                            if (it) {
                                DhaibanBottomDialog(
                                    title = Theme.strings.addingItemToCartRequiresLogin,
                                    image = R.drawable.authorization_image,
                                    positiveText = Theme.strings.logIn,
                                    negativeText = Theme.strings.back,
                                    onDismiss = { listener.onDismissAlert() },
                                    onPositive = {
                                        listener.onClickLogin()

                                    }
                                )
                            }
                        }
                    }
                    AnimatedVisibility(
                        visible = state.showCartDialog,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it },
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            DhaibanAlertDialog(
                                title = Theme.strings.addedSuccessfully,
                                body = {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.add_cart_success),
                                            contentDescription = "Payment Success Image"
                                        )
                                    }
                                },
                                positiveText = Theme.strings.checkCart,
                                negativeText = Theme.strings.continues,
                                onDismiss = { listener.onDismissCartDialog() },
                                onPositive = { listener.onClickCartIcon() }
                            )
                        }
                    }
                }
            }
        } else {
            ImageViewerComponent(
                imageId = state.productDetails.selectedImageId,
                images = state.productDetails.productImages
            ) {
                listener.onCloseImageViewer()
            }
        }
    }
}

@Composable
private fun ChoiceItem(
    modifier: Modifier = Modifier,
    choiceId: Int,
    title: String,
    options: List<OptionUiState>,
    selectedOptionId: Int,
    onClick: (Int, Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(text = "$title :")
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier.fillMaxWidth(),
        ) {
            items(items = options, key = { option ->
                option.id
            }) { option ->
                OptionItem(
                    id = option.id,
                    selected = option.id == selectedOptionId,
                    optionTitle = option.title
                ) { optionId ->
                    onClick(choiceId, optionId)
                }
            }
        }
    }
}

@Composable
private fun OptionItem(id: Int, optionTitle: String, selected: Boolean, onClick: (Int) -> Unit) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .padding(end = 8.dp)
            .noRippleEffect { onClick(id) },
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white)
    ) {
        Box(
            modifier = if (selected) Modifier
                .border(
                    1.dp, Theme.colors.mediumBrown, RoundedCornerShape(12.dp)
                )
                .padding(horizontal = 24.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
            else Modifier
                .padding(horizontal = 24.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(16.dp))
        ) {
            Text(
                text = optionTitle,
                style = Theme.typography.caption,
                color = Theme.colors.greyishBrown
            )
        }

    }
}

@Composable
private fun ProductImages(
    images: List<PhotoUiState>, selectedImageId: Int, onImageClick: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(), contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(images) {
            ProductImageItem(
                imageUrl = it.imageUrl,
                id = it.id,
                selected = it.id == selectedImageId,
                onClick = onImageClick
            )
        }
    }
}

@Composable
private fun ProductImageItem(
    imageUrl: String, id: Int, selected: Boolean = false, onClick: (Int) -> Unit
) {
    CoilImage(url = imageUrl,
        contentDescription = "Product Image",
        modifier = if (selected) Modifier
            .size(115.dp)
            .padding(end = 8.dp)
            .border(
                2.dp, Theme.colors.mediumBrown, RoundedCornerShape(16.dp)
            )
            .noRippleEffect { onClick(id) }
        else Modifier
            .size(115.dp)
            .padding(end = 8.dp)
            .noRippleEffect { onClick(id) })
}

@Composable
fun ColorsComponent(
    colors: List<ColorUiState>, selectedColorId: Int, onClick: (Int) -> Unit
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (colors.get(0).id != 0 && colors.get(0).colorCode != "") {
            colors.forEach { colorUiState ->
                Box(modifier = if (selectedColorId == colorUiState.id) Modifier
                    .padding(end = 4.dp)
                    .size(24.dp)
                    .border(1.dp, Theme.colors.primarySemiDark, CircleShape)
                    .padding(2.dp)
                    .background(
                        color = Color(parseColor(colorUiState.colorCode)), shape = CircleShape
                    )
                    .clip(CircleShape)
                    .noRippleEffect {
                        onClick(colorUiState.id)
                    }
                else Modifier
                    .padding(end = 4.dp)
                    .size(24.dp)
                    .background(
                        color = Color(parseColor(colorUiState.colorCode)), shape = CircleShape
                    )
                    .clip(CircleShape)
                    .noRippleEffect {
                        onClick(colorUiState.id)
                    })
            }
        }

    }
}


@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .height(220.dp)
                .fillMaxWidth(0.80f)
                .shimmerEffect()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .size(115.dp)
                        .fillMaxWidth(0.80f)
                        .shimmerEffect()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(150.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier
                        .width(50.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .size(24.dp)
                        .clip(CircleShape)
                        .shimmerEffect()
                ) {

                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            repeat(4) {
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .width(45.dp)
                        .height(30.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .shimmerEffect()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(3) {
                Box(
                    modifier = Modifier
                        .padding(end = 4.dp)
                        .width(90.dp)
                        .height(45.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .shimmerEffect()
                )
            }
            Box(
                modifier = Modifier
                    .padding(end = 4.dp)
                    .size(30.dp)
                    .clip(CircleShape)
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column {

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.85f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.50f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.35f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row {
            repeat(10) {
                Box(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .width(115.dp)
                        .height(200.dp)
                        .fillMaxWidth(0.80f)
                        .shimmerEffect()
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductScreenContentPreview() {
    val dummyListener = object : ProductScreenInteractionListener {
        override fun onClickSmallImage(imageId: Int) {}
        override fun onClickUpButton() {}
        override fun onClickColor(colorId: Int) {}
        override fun onClickRecommendedProduct(productId: Int) {}
        override fun onClickOption(parentChoiceId: Int, optionId: Int) {}
        override fun onClickImage(visibility: Boolean) {}
        override fun onClickProductFavorite(productId: Int, isFavorite: Boolean) {}
        override fun onCLickAddToCart(productId: Int, quantity: Int) {}
        override fun showMessage(message: String) {}
        override fun onDismissAlert() {}
        override fun onClickLogin() {}
        override fun onClickCartIcon() {}
        override fun onCloseImageViewer() {}
        override fun onDismissCartDialog() {}
    }
    DhaibanTheme {
        ProductScreenContent(

            state = ProductScreenUiState(
                productDetails = ProductDetailsUiState(
                    productImages = listOf(
                        PhotoUiState(1, ""),
                        PhotoUiState(2, ""),
                        PhotoUiState(3, ""),
                        PhotoUiState(4, "")
                    ),
                    choiceOptions = listOf(
                        ChoiceOptionUiState(
                            1,
                            "Size",
                            listOf(
                                OptionUiState(1, "XL"),
                                OptionUiState(2, "S"),
                                OptionUiState(3, "L"),
                                OptionUiState(4, "XL"),
                                OptionUiState(6, "S"),
                                OptionUiState(7, "XXL"),
                                OptionUiState(8, "XL"),
                                OptionUiState(9, "s"),
                                OptionUiState(10, "XXL"),
                                OptionUiState(11, "XL"),
                                OptionUiState(12, "s"),
                                OptionUiState(13, "XXL"),
                            )
                        )
                    )
                ),
                productReviews = listOf(
                    ReviewUiState(
                        1,
                        4.7,
                        "Bla Bla",
                        "",
                        "",
                        "Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla" +
                                "Bla Bla Bla Bla Bla Bla Bla Bla" +
                                "Bla Bla Bla Bla Bla Bla Bla Bla" +
                                "Bla Bla Bla Bla Bla Bla Bla Bla"
                    )
                )
            ), listener = dummyListener
        )
    }
}

fun calculatePrice(
    basePrice: Double,
    discount: Double,
    discountType: String,
    exchangeRate: Double
): Double {

    var price = when {
        discountType == "amount" -> {
            if (
                (discount) >basePrice
            ){
                basePrice*exchangeRate
            }
            else{
            Log.d("calculatePrice", "${basePrice - discount}")
            basePrice*exchangeRate - discount}
        }

        discount != 0.0 -> {
            if (
                (discount / 100.0 ) >basePrice
            ){
                basePrice*exchangeRate
            }
            else{
            Log.d("calculatePrice", "${basePrice - basePrice * discount / 100.0}")
            (basePrice*exchangeRate) - (basePrice*exchangeRate)  * discount / 100.0}
        }

        else -> {
            Log.d("calculatePrice", "$basePrice")
            basePrice*exchangeRate
        }
    }

    // Introduce delay
//                                        delay(delayMs)

    return price
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductContentPreview() {
    DhaibanTheme {
        LoadingContent()
    }
}

data class ProductDetails(
    val lowerPrice: Double,
    val higherPrice: Double,
    val variantPrice: Double,
    val discount: Double,
    val discountType: String
)

data class Currency(val exchangeRate: Double, val symbol: String)
data class ProductState(val productDetails: ProductDetails, val currency: Currency)

@Preview(showBackground = true)
@Composable
fun OptionItemPreview() {
    DhaibanTheme {
        OptionItem(1, "XL", true) {}
    }
}