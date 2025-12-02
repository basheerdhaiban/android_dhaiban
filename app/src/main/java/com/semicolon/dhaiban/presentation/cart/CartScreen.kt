import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.semicolon.dhaiban.presentation.cart.*


import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.home.container.HomeTab
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.payment.PaymentScreen
import com.semicolon.dhaiban.presentation.paymentstatus.PaymentStatusScreen
import com.semicolon.dhaiban.presentation.product.ColorUiState
import com.semicolon.dhaiban.presentation.utils.Constants.CART_SCREEN
import com.semicolon.dhaiban.utils.PaymentMethodsType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf
import kotlin.math.round

class CartScreen(
    private val addressId: Int = 0,
    private val addressName: String = "",
) : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<CartScreenModel>(parameters = {
            parametersOf(
                CartAddressUiState(
                    addressId, addressName
                )
            )
        })
        val homeScreenModel = getScreenModel<AppScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: CartScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current
        val context = LocalContext.current
        var showToast by remember { mutableStateOf(false) }
        val coroutineScope = rememberCoroutineScope()
        DisposableEffect(Unit) { onDispose { coroutineScope.cancel() } }
        LaunchedEffect(Unit) {
            screenModel.getDefaultData()
            screenModel.getUserData()
            screenModel.getCartProducts()
            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
            homeScreenModel.setCurrentScreen(CART_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    CartScreenUiEffect.OnFailedDelete -> Toast.makeText(
                        context, "Network Error", Toast.LENGTH_SHORT
                    ).show()

                    CartScreenUiEffect.OnNavigateToAddress -> SafeNavigator.safeReplace(
                        navigator, AddressScreen(fromProfile = false),
                        lifecycleOwner = lifecycleOwner
                    )

                    CartScreenUiEffect.OnNavigateToHome -> navigator.parent?.let { parentNav ->
                        SafeNavigator.safeReplace(navigator = parentNav,
                        screen = HomeTab, lifecycleOwner = lifecycleOwner)
                    }

                    is CartScreenUiEffect.OnNavigateToProductScreen -> SafeNavigator.safePush(
                        navigator,
                        ProductScreen(
                            effect.productId, effect.variant
                        ),
                        lifecycleOwner = lifecycleOwner
                    )

                    is CartScreenUiEffect.OnShowMessage -> {

                        showToast = true
                        coroutineScope.launch {
                            delay(2000)
                            showToast = false
                        }
                    }

                    CartScreenUiEffect.OnNavigateToPaymentScreen -> if (state.momo == PaymentMethodsType.MOMO.name) {
                        SafeNavigator.safePush(
                            navigator, PaymentStatusScreen(isComeFromCart = true),
                            lifecycleOwner = lifecycleOwner
                        )
                    } else {
                        Log.d("isValidDiscount", state.isValidToDiscount.toString())
                        SafeNavigator.safePush(
                            navigator,
                            PaymentScreen(
                                discountcode = if (state.isValidToDiscount) state.promoCode else "",
                                subTotal = state.subTotal,
                                discount = if (state.isValidToDiscount) state.discount else 0.0,
                                taxes = state.taxes,
                                total = state.total,
                                currencySymbol = state.currencySymbol,
                                currencyId = state.currencyId,
                                addressId = state.cartAddress.addressId,
                            ),
                            lifecycleOwner = lifecycleOwner
                        )
                    }

                    is CartScreenUiEffect.OnUpdateCartCount -> appScreenModel.updateCartItemsNumber(
                        effect.cartCount
                    )

                    CartScreenUiEffect.OnNavigateToNotificationScreen ->
                        SafeNavigator.safePush(
                            navigator, NotificationScreen(),
                            lifecycleOwner = lifecycleOwner
                        )

                }
            }
        }
//        if (state.isLoadingForCoupon) {
//            Box(modifier = Modifier.fillMaxSize()){
//
//                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), trackColor = Theme.colors.primary)
//            }
//
//        } else
        DhaibanTheme(
            stringResources = state.stringRes, layoutDirection = state.layoutDirection
        ) {
            CartScreenContent(state = state, listener = screenModel, model = screenModel)
        }


        if (state.shouldShowCouponToast) {
            Log.d("shouldShowCouponToast", state.shouldShowCouponToast.toString())
            Log.d("shouldShowCouponToast", state.message.toString())
            Toast.makeText(
                context, state.message, Toast.LENGTH_LONG
            ).show()
            state.shouldShowCouponToast = false
        }

        if (showToast) {
            Toast.makeText(
                context, state.errorMessage, Toast.LENGTH_LONG
            ).show()
        }
        BackHandler {
            navigator.parent?.let { parentNav -> SafeNavigator.safeReplace(
                parentNav, HomeTab,
                lifecycleOwner = lifecycleOwner
            ) }
        }
    }


}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CartScreenContent(

    state: CartScreenUiState, listener: CartScreenInteractionListener, model: CartScreenModel?
) {
    val context = LocalContext.current
    var couponText by remember { mutableStateOf("") }
    LaunchedEffect(key1 = state.notValidAmountForCoupoan) {
        if (state.notValidAmountForCoupoan) {
            Toast.makeText(
                context,
                "The Discount bigger Than Price So not possable ",
                Toast.LENGTH_LONG
            ).show()
            state.notValidAmountForCoupoan=false
        }

    }
    Log.d("CategoryScreenContent2", state.userData.isAuthenticated.toString())
    val productsOFCoupon = state.items.map { it.toCouponModel() }
    AnimatedContent(targetState = state.isLoading, transitionSpec = {
        fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 700)) with
                fadeOut(animationSpec = tween(durationMillis = 300))
    }, label = "") { loading ->
        if (loading) {

        Box(modifier = Modifier.fillMaxSize()){
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Theme.colors.mediumBrown,

                )}
        }
        else {



        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {

            Card(
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
                shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = Theme.strings.cart, style = Theme.typography.headline)
                    if (state.userData.isAuthenticated) {

                        BadgedBox(badge = {
                            if (state.countOfUnreadMessage != 0) {
                                Icon(
                                    painter = painterResource(id = R.drawable.dot),
                                    contentDescription = null,
                                    tint = Theme.colors.mediumBrownTwo,
                                    modifier = Modifier.align(
                                        Alignment.TopStart
                                    )
                                )
                            }
                        }) {
                            Icon(
                                modifier = Modifier.noRippleEffect { listener.onClickNotification() },
                                painter = painterResource(id = R.drawable.notifcation),
                                tint = Theme.colors.mediumBrown,

                                contentDescription = null
                            )
                        }
                    }


                }
            }
            val softwareKeyboardController = LocalSoftwareKeyboardController.current

            val scope = rememberCoroutineScope()
            if (state.userData.isAuthenticated) {
                AnimatedContent(targetState = state.isLoading, label = "") { isLoading ->
                    if (isLoading) {
                        LoadingContent()
                    } else if (state.items.isNotEmpty()) {

                        LazyColumn {
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                                Column {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .noRippleEffect { listener.onClickAddress() },
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Image(
                                                    painter = painterResource(id = R.drawable.location_icon),
                                                    contentDescription = "Location Icon"
                                                )
                                                Text(
                                                    text = " " + Theme.strings.deliveryAddress,
                                                    style = Theme.typography.title
                                                )
                                            }
                                            Text(
                                                text = state.cartAddress.addressName,
                                                style = Theme.typography.caption,
                                                color = Theme.colors.dimGray
                                            )
                                        }
                                        Image(
                                            modifier = Modifier
                                                .scale(
                                                    scaleX = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f,
                                                    scaleY = 1f
                                                )
                                                .noRippleEffect { listener.onClickAddress() },
                                            painter = painterResource(id = R.drawable.open_location_icon),
                                            contentDescription = "Open Location Screen Icon"
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .height(1.dp)
                                            .background(Theme.colors.silver.copy(alpha = 0.5f))
                                    )
                                }
                                Spacer(modifier = Modifier.height(32.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = Theme.strings.itemsInYourCart,
                                        style = Theme.typography.title
                                    )
                                    Text(
                                        modifier = Modifier.noRippleEffect { listener.onClickAddMore() },
                                        text = Theme.strings.addMore,
                                        style = Theme.typography.caption,
                                        color = Theme.colors.mediumBrown
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                    val context = LocalContext.current

                                    val idsInList2 = state.items.map { it.product.id }.toSet()


                                    Log.d(
                                        "sassasaasa",
                                        "${state.products.count { it.id.toInt() in idsInList2 }}"
                                    )
                                    // Count the items in list1 that have matching IDs in list2
                                    state.CountOfProductHaveCoupon = state.products.count {
                                        it.id.toInt() in idsInList2 && it.discount != 0.0
                                    }


                                    state.items.forEach { cartItemUiState ->
                                        for (item in state.products) {
                                            if (item.id.toInt() == cartItemUiState.product.id) {
                                                if (cartItemUiState.afterDiscount * cartItemUiState.quantity > item.discount) {
                                                    state.isValidToDiscount = true

                                                } else {
                                                    if (state.showToast) {
                                                        Toast.makeText(
                                                            context,
                                                            "coupon bigger then 50% of product",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                                state.showToast = false

                                            }
                                        }
                                        CartItem(state = state,
                                            cartItemUiState = cartItemUiState,
                                            currencySymbol = state.currency.symbol,
                                            exchangeRate = 1.0,// becuase it applied and control in backend
                                            onPlusClick = { identifier, serializedOptions, variant, productId, quantity, availableStockState ->

                                                if (state.momo == PaymentMethodsType.MOMO.name) {
                                                    Toast.makeText(
                                                        context,
                                                        "You Should Finish Payment Process",
                                                        Toast.LENGTH_LONG
                                                    )
                                                } else {
                                                    listener.onCLickPlus(
                                                        identifier,
                                                        serializedOptions,
                                                        variant,
                                                        productId,
                                                        quantity,
                                                        availableStockState
                                                    )
                                                }
//                                                if (couponText.isNotBlank()) {
//                                                    model?.getDiscountCoupon(
//                                                        couponText, productsOFCoupon
//                                                    )
//                                                }
                                            },
                                            onDeleteClick = {

                                                if (state.momo == PaymentMethodsType.MOMO.name) {
                                                    Toast.makeText(
                                                        context,
                                                        "You Should Finish Payment Process",
                                                        Toast.LENGTH_LONG
                                                    )
                                                } else {
                                                    scope.launch {
                                                        listener.onClickDelete(
                                                            it
                                                        )
                                                    }
                                                }
                                                if (couponText.isNotBlank()) {
                                                    model?.getDiscountCoupon(
                                                        couponText, productsOFCoupon
                                                    )
                                                }
                                            },
                                            onMinusClick = {

                                                    identifier, serializedOptions, variant, productId, quantity ->

                                                if (state.momo == PaymentMethodsType.MOMO.name) {
                                                    Toast.makeText(
                                                        context,
                                                        "You Should Finish Payment Process",
                                                        Toast.LENGTH_LONG
                                                    )
                                                } else {
                                                    listener.onCLickMinus(
                                                        identifier,
                                                        serializedOptions,
                                                        variant,
                                                        productId,
                                                        quantity
                                                    )
                                                }
//                                                if (couponText.isNotBlank()) {
//                                                    model?.getDiscountCoupon(
//                                                        couponText, productsOFCoupon
//                                                    )
//                                                }
                                            }) { productId, variant ->
                                            listener.onClickCartItem(productId, variant)
                                        }
                                        Spacer(modifier = Modifier.height(16.dp))
                                    }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.Start,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = Theme.strings.promoCode,
                                        style = Theme.typography.title
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))

                                state.productsOFCoupon = productsOFCoupon
                                OutlinedTextField(
                                    modifier = Modifier
                                        .height(50.dp)
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    value = couponText,
                                    onValueChange = { couponText = it },
                                    leadingIcon = {
                                        Image(
                                            painter = painterResource(id = R.drawable.coupon_icon),
                                            contentDescription = "Coupon Icon"
                                        )
                                    },
                                    trailingIcon = {
                                        state.promoCode = couponText
                                        Box(
                                            Modifier
                                                .noRippleEffect {
                                                    model?.getDiscountCoupon(
                                                        couponText, productsOFCoupon
                                                    )
                                                    softwareKeyboardController?.hide()

                                                }
                                                .fillMaxHeight()
                                                .clip(
                                                    RoundedCornerShape(
                                                        topEnd = 10.dp, bottomEnd = 10.dp
                                                    )
                                                )
                                                .background(Theme.colors.mediumBrown)
                                                .padding(horizontal = 32.dp),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = Theme.strings.apply,
                                                color = Theme.colors.white,
                                                style = Theme.typography.caption
                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    placeholder = {
                                        Text(
                                            text = "Enter promo code",
                                            style = Theme.typography.caption,
                                            color = Theme.colors.silverGray
                                        )
                                    },
                                    singleLine = true,
                                    colors = TextFieldDefaults.colors(
                                        focusedIndicatorColor = Theme.colors.whiteTwo,
                                        unfocusedIndicatorColor = Theme.colors.whiteTwo,
                                        unfocusedContainerColor = Theme.colors.background,
                                        cursorColor = Theme.colors.mediumBrown,
                                        selectionColors = TextSelectionColors(
                                            handleColor = Theme.colors.mediumBrown,
                                            backgroundColor = Theme.colors.darkBeige
                                        )
                                    ),
                                )
                                Spacer(modifier = Modifier.height(24.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = Theme.strings.paymentDetails,
                                        style = Theme.typography.title
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                ) {
                                    val discount = state.discount

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = Theme.strings.subtotal,
                                            style = Theme.typography.body,
                                            color = Theme.colors.greyishBrown
                                        )
                                        Log.d("state.subTotal", state.subTotal.toString())
                                        val roundedNumber = state.subTotal

                                        Text(
                                            text = String.format(
                                                "%.2f", roundedNumber
                                            ) + " ${state.currency.symbol}",
                                            style = Theme.typography.body,
                                            color = Theme.colors.greyishBrown
                                        )
                                    }
                                    if (state.discount != 0.00 && state.isValidToDiscount) {
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = Theme.strings.promoCode,
                                                style = Theme.typography.body,
                                                color = Theme.colors.greyishBrown
                                            )
                                            Text(
                                                text = String.format(
                                                    "%.2f", -1 * discount
                                                ) + " ${state.currency.symbol}",
                                                style = Theme.typography.body,
                                                color = Color.Red
                                            )
                                        }

                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Log.d("shipping tax", state.cartAddress.shippingTax.toString())
                                    if (state.cartAddress.shippingTax != 0.0) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            //TODO ADD STRING
                                            Text(
                                                text = "shipping tax",
                                                style = Theme.typography.body,
                                                color = Theme.colors.greyishBrown
                                            )
                                            Text(
                                                text = String.format(
                                                    "%.2f", state.cartAddress.shippingTax
                                                ) + " ${state.currency.symbol}",
                                                style = Theme.typography.body,
                                                color = Theme.colors.greyishBrown
                                            )
                                        }
                                    }
                                    Log.d("shippingCost", state.cartAddress.shippingCost.toString())

                                    if (state.cartAddress.shippingCost != 0.0) {
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            //TODO ADD STRING
                                            Text(
                                                text = "Shipping Cost",
                                                style = Theme.typography.body,
                                                color = Theme.colors.greyishBrown
                                            )
                                            Text(
                                                text = String.format(
                                                    "%.2f", state.cartAddress.shippingCost
                                                ) + " ${state.currency.symbol}",
                                                style = Theme.typography.body,
                                                color = Theme.colors.greyishBrown
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = Theme.strings.taxAndOtherFees,
                                            style = Theme.typography.body,
                                            color = Theme.colors.greyishBrown
                                        )
                                        val roundedNumber = round(state.taxes)
                                        Text(
                                            text = String.format(
                                                "%.2f", state.taxes
                                            ) + " ${state.currency.symbol}",
                                            style = Theme.typography.body,
                                            color = Theme.colors.greyishBrown
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(Theme.colors.silver.copy(alpha = 0.5f))
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {

                                        Text(
                                            text = Theme.strings.total,
                                            style = Theme.typography.body.copy(
                                                fontWeight = FontWeight(
                                                    650
                                                )
                                            ),
                                            color = Theme.colors.black
                                        )
                                        Log.d("mmmm", state.discount.toString())
                                        if (state.isValidToDiscount) {
                                            Log.d(
                                                "isValidToDiscountss",
                                                state.CountOfProductHaveCoupon.toString()
                                            )
                                            Log.d("isValidToDiscountss22", discount.toString())

                                            Text(
                                                text = String.format(
                                                    "%.2f", state.total - discount
                                                ) + " ${state.currency.symbol}",
                                                style = Theme.typography.body.copy(
                                                    fontWeight = FontWeight(
                                                        650
                                                    )
                                                ),
                                                color = Theme.colors.black
                                            )
                                        } else {
                                            Text(
                                                text = String.format(
                                                    "%.2f", state.total
                                                ) + " ${state.currency.symbol}",
                                                style = Theme.typography.body.copy(
                                                    fontWeight = FontWeight(
                                                        650
                                                    )
                                                ),
                                                color = Theme.colors.black
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(32.dp))
                                Button(
                                    onClick = { listener.onClickContinueToPayment() },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Theme.colors.mediumBrown
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        text = Theme.strings.continueForPayment,
                                        style = Theme.typography.title,
                                        color = Theme.colors.white
                                    )
                                }
                            }
                        }

                    } else {
                        scope.launch {
                            delay(4000)
                        }
                        Box(
                            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Image(
                                        painter = painterResource(id = R.drawable.cart_empty_state),
                                        contentDescription = "No Data Icon",
                                    )
                                }

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = Theme.strings.YourCartIsEmpty,
                                        style = Theme.typography.headline,

                                        )

                                }
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = Theme.strings.noDataFound,
                                        style = Theme.typography.body.copy(
                                            fontWeight = FontWeight(
                                                400
                                            )
                                        ),
                                        textAlign = TextAlign.Start,
                                        color = Theme.colors.dimGray
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center
                                ) {
//                                    ButtonForEmptyState("Go back") {
//                                        listener.onClickNotification()
//                                    }


                                }
                            }
                        }
                    }
                }

            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.no_data),
                                contentDescription = "No Data Icon",
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = Theme.strings.needsLogin,
                                style = Theme.typography.headline.copy(fontWeight = FontWeight(500))
                            )
                        }
                    }
                }
            }

        }
    }}
}

@Composable
private fun CartItem(
    state: CartScreenUiState,
    cartItemUiState: CartItemUiState,
    currencySymbol: String,
    exchangeRate: Double,
    onPlusClick: (Long, SerializedOptionsUiState, String, Int, Int, Boolean) -> Unit,
    onMinusClick: (Long, SerializedOptionsUiState, String, Int, Int) -> Unit,
    onDeleteClick: (Int) -> Unit,
    onCartItemClick: (Int, String) -> Unit
) {

    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onCartItemClick(cartItemUiState.product.id, cartItemUiState.variant) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white)
    ) {
        val context = LocalContext.current
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(3f)) {
                    CoilImage(
                        modifier = Modifier
                            .weight(1f)
                            .height(120.dp),
                        url = cartItemUiState.product.photo,
                        contentDescription = "Product Image",
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(2f)
                            .padding(top = 8.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(0.70f),
                                text = cartItemUiState.product.title,
                                style = Theme.typography.body,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Image(
                                modifier = Modifier.noRippleEffect {
                                    if (state.momo == PaymentMethodsType.MOMO.name) {
                                        Log.d("delete", "delete")

                                        Toast.makeText(
                                            context,
                                            "You Should Finish Payment Process",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    } else {
                                        onDeleteClick(cartItemUiState.id)
                                    }
                                },
                                painter = painterResource(id = R.drawable.delete_icon),
                                contentDescription = "Minus Button"
                            )
                        }
                        Log.d("cartItemUiStatess", cartItemUiState.serializedOptions.colorTitle)
                        if (cartItemUiState.serializedOptions.colorTitle.isNotEmpty()) {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = "${Theme.strings.color}: " + cartItemUiState.serializedOptions.colorTitle)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            ColorsComponent(
                                colors = listOf<ColorUiState>(
                                    ColorUiState(
                                        cartItemUiState.serializedOptions.colorId, 0
                                    )
                                ), selectedColorId = 0
                            ) { colorId ->

                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                item { Spacer(modifier = Modifier.fillMaxWidth(0.30f)) }
                                items(items = cartItemUiState.serializedOptions.choices,
                                    key = { it.id }) {
                                    VariantItem(parentTitle = it.parentTitle, childTitle = it.title)
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val context = LocalContext.current
                            var price = 0.0

                            Log.d("aassasasa", cartItemUiState.afterDiscount.toString())

                            val formattedNumber =
                                String.format(
                                    "%.3f",
                                    cartItemUiState.afterDiscount * cartItemUiState.quantity
                                )

                            Text(
                                text = (formattedNumber).toString() + " $currencySymbol",
                                style = Theme.typography.caption,
                                color = Theme.colors.mediumBrown
                            )
                            Row(modifier = Modifier.align(Alignment.Bottom)) {
                                val context = LocalContext.current
                                Button(
                                    onClick = {
                                        if (state.momo == PaymentMethodsType.MOMO.name) {
                                            Toast.makeText(
                                                context,
                                                "You Should Finish Payment Process",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            onMinusClick(
                                                cartItemUiState.identifier,
                                                cartItemUiState.serializedOptions,
                                                cartItemUiState.variant,
                                                cartItemUiState.product.id,
                                                cartItemUiState.quantity
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .padding(end = 16.dp, bottom = 8.dp)
                                        .size(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Theme.colors.buttonTextGray,
                                        containerColor = Theme.colors.antiqueLace
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 0.dp)

                                ) {
                                    Image(
                                        modifier = Modifier.size(10.dp),
                                        painter = painterResource(id = R.drawable.minus_icon),
                                        contentDescription = "Minus Button"
                                    )
                                }

                                Text(
                                    text = cartItemUiState.quantity.toString(),
                                    modifier = Modifier.padding(end = 16.dp)
                                )
                                Button(
                                    onClick = {
                                        if (state.momo == PaymentMethodsType.MOMO.name) {
                                            Toast.makeText(
                                                context,
                                                "You Should Finish Payment Process",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        } else {
                                            onPlusClick(
                                                cartItemUiState.identifier,
                                                cartItemUiState.serializedOptions,
                                                cartItemUiState.variant,
                                                cartItemUiState.product.id,
                                                cartItemUiState.quantity,
                                                cartItemUiState.quantity < cartItemUiState.product.currentStock
                                            )
                                            Log.d(
                                                "debugcurrentStock",
                                                cartItemUiState.product.currentStock.toString()
                                            )
                                            Log.d(
                                                "debugcurrentStockq",
                                                cartItemUiState.quantity.toString()
                                            )
                                        }
                                    },
                                    modifier = Modifier.size(20.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        contentColor = Theme.colors.buttonTextGray,
                                        containerColor = Theme.colors.mediumBrown
                                    ),
                                    shape = RoundedCornerShape(6.dp),
                                    contentPadding = PaddingValues(horizontal = 0.dp)

                                ) {
                                    Image(
                                        modifier = Modifier.size(10.dp),
                                        painter = painterResource(id = R.drawable.plus_icon),
                                        contentDescription = "Plus Button",
                                        colorFilter = ColorFilter.tint(Theme.colors.white)
                                    )
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
private fun VariantItem(parentTitle: String, childTitle: String) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.padding(end = 8.dp, start = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            Text(
                text = "$parentTitle : $childTitle",
                style = Theme.typography.caption,
                color = Theme.colors.greyishBrown
            )
        }

    }
}

@Preview
@Composable
fun VariantIemPreview() {
    DhaibanTheme { VariantItem(parentTitle = "Bla", childTitle = "Bla Bla") }
}


@Composable
private fun LoadingContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
            Image(
                modifier = Modifier.scale(
                    scaleX = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f,
                    scaleY = 1f
                ),
                painter = painterResource(id = R.drawable.open_location_icon),
                contentDescription = "Open Location Screen Icon",
                colorFilter = ColorFilter.tint(Theme.colors.primary100)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(1.dp)
                .background(Theme.colors.silver.copy(alpha = 0.5f))
        )
        Spacer(modifier = Modifier.height(32.dp))
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
        Column(modifier = Modifier.fillMaxWidth()) {
            repeat(3) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .fillMaxWidth()
                        .height(100.dp)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
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
                    .width(100.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(50.dp)
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
                    .width(150.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .shimmerEffect()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
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
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(120.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Theme.colors.silver.copy(alpha = 0.5f))
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .width(80.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .shimmerEffect()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .height(50.dp)
                .shimmerEffect()
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartContentPreview() {
    DhaibanTheme { LoadingContent() }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CartScreenContentPreview() {
    val dummyListener = object : CartScreenInteractionListener {
        override suspend fun onClickDelete(cartItemId: Int) {}
        override fun onCLickPlus(
            identifier: Long,
            serializedOptionsUiState: SerializedOptionsUiState,
            variant: String,
            productId: Int,
            quantity: Int,
            availableStockState: Boolean
        ) {
        }

        override fun onCLickMinus(
            identifier: Long,
            serializedOptionsUiState: SerializedOptionsUiState,
            variant: String,
            productId: Int,
            quantity: Int
        ) {
        }

        override fun onClickAddress() {}
        override fun onClickContinueToPayment() {}
        override fun onClickAddMore() {}
        override fun onClickCartItem(productId: Int, variant: String) {}
        override fun onClickNotification() {}
    }
    DhaibanTheme {
        CartScreenContent(state = CartScreenUiState(), listener = dummyListener, model = null)
    }
}

@Preview(showBackground = true)
@Composable
fun CartItemPreview() {
//    DhaibanTheme {
//        CartItem(CartItemUiState(), "", 0.1, { _, _, _, _, _, _ -> }, { _, _, _, _, _ -> }, {})
//        { _, _ -> }
//    }
}