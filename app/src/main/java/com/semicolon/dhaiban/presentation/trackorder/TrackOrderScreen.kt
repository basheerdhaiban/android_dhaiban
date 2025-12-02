import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.home.container.MainContainer
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.orders.OrderProductUiState
import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.trackorder.OldOrNewChat
import com.semicolon.dhaiban.presentation.trackorder.TrackOrderScreenInteractionListener
import com.semicolon.dhaiban.presentation.trackorder.TrackOrderScreenModel
import com.semicolon.dhaiban.presentation.trackorder.TrackOrderScreenUiEffect
import com.semicolon.dhaiban.presentation.trackorder.TrackOrderScreenUiState
import com.semicolon.dhaiban.presentation.trackorder.composables.DeliveryStateComponent
import com.semicolon.dhaiban.presentation.trackorder.composables.RatingDialog
import com.semicolon.dhaiban.presentation.trackorder.composables.RefundError
import com.semicolon.dhaiban.presentation.trackorder.composables.RefundItemsBottomSheet
import com.semicolon.dhaiban.presentation.trackorder.composables.ReturnProductReasonsBottomSheet
import com.semicolon.dhaiban.presentation.utils.Constants.TRACK_ORDER_SCREEN
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class TrackOrderScreen(

    private val orderType: OrderType,
    private val orderID: Int,
    private val isComeFromNotification: Boolean = false,
) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<TrackOrderScreenModel>(parameters = {
            parametersOf(orderType, orderID)
        })
        Log.d("TrackOrderScreen", orderType.toString())
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: TrackOrderScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current


        LaunchedEffect(Unit) {
            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
            appScreenModel.setCurrentScreen(TRACK_ORDER_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
//                    TrackOrderScreenUiEffect.OnNavigateBack -> SafeNavigator.safeReplace(navigator, OrdersScreen(, lifecycleOwner))
                    TrackOrderScreenUiEffect.OnNavigateBack -> if (isComeFromNotification) {
                        SafeNavigator.safePush(
                            navigator,
                            MainContainer(),
                            lifecycleOwner = lifecycleOwner
                        )
                    } else if (navigator.canPop)
                        SafeNavigator.safePop(navigator, lifecycleOwner)

                    TrackOrderScreenUiEffect.OnNavigateToNotificationScreen -> SafeNavigator.safeReplace(
                        navigator,
                        NotificationScreen(),
                        lifecycleOwner = lifecycleOwner
                    )

                    is TrackOrderScreenUiEffect.OnNavigateToChatScreen -> {
                        SafeNavigator.safePush(
                            navigator,
                            ChatScreen(
                                price = state.orderProducts[effect.numberofItem].price,
                                image = state.orderProducts[effect.numberofItem].photo,
                                name = state.orderProducts[effect.numberofItem].productTitle,
                                productOwner = state.orderProducts[effect.numberofItem].productOwner,
                                productOwnerId = state.orderProducts[effect.numberofItem].productOwnerId,
                                productOwnerImg = state.orderProducts[effect.numberofItem].productImg,
                                oldOrNewChat = OldOrNewChat.NEW,
                                productId = state.orderProducts[effect.numberofItem].productId,
                                orderId = state.orderId,


                                ),
                            lifecycleOwner = lifecycleOwner
                        )
                    }

                    else -> {}
                }
            }
        }
        val order = screenModel.orderWithId.collectAsState()
        if (order.value.isLoading) {
            Log.d("TrackOrderScreen", "Loading")
            LoadingContent(state)
        } else {
            TrackOrderScreenContent(
                state = state,
                listener = screenModel,
                model = screenModel,
                orderID = orderID,
                orderType = orderType
            )
        }

        Log.d("iscomeFormNotif",isComeFromNotification.toString())
        BackHandler {
            Log.d("iscomeForsssssmNotif",isComeFromNotification.toString())

            if (state.showReturnProductDialog or state.showRefundItemsDialog or state.showRatingDialog) screenModel.onDismissBottomSheet()
            else if (isComeFromNotification) {
                Log.d("sssssss",isComeFromNotification.toString())

                SafeNavigator.safePush(
                    navigator,
                    MainContainer(),
                    lifecycleOwner = lifecycleOwner
                )
            } else if (navigator.canPop) SafeNavigator.safePop(navigator, lifecycleOwner)
        }
    }
}

@Composable
private fun LoadingContent(state: TrackOrderScreenUiState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        AppBarWithIcon(title = when (state.orderType) {
            OrderType.CURRENT -> Theme.strings.currentOrders
            OrderType.PREVIOUS -> Theme.strings.previousOrders
        }, onClickUpButton = { }, onClickNotification = {})
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(5) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .shimmerEffect()
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
//            Card(
//                modifier = Modifier.fillMaxWidth().height(120.dp).padding(horizontal = 16.dp).shimmerEffect(),
//                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
////                colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
//                shape = RoundedCornerShape(8.dp)
//            ) {}
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 16.dp)
                    .clip(
                        RoundedCornerShape(25.dp)
                    )
                    .shimmerEffect()
            )
            Spacer(modifier = Modifier.height(30.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(horizontal = 35.dp)
                    .shimmerEffect()
            )
        }

    }

}


@Composable
private fun TrackOrderScreenContent(

    state: TrackOrderScreenUiState,
    orderID: Int,
    listener: TrackOrderScreenInteractionListener,
    model: TrackOrderScreenModel? = null,
    orderType: OrderType
) {
    val order = model?.orderWithId?.collectAsState()
    if (order?.value?.isLoading == true) {

    }

    order?.value?.currentOrder?.orderNumber?.let { Log.d("TrackOrderScreenContent", it) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        AppBarWithIcon(
            title = when (orderType) {
                OrderType.CURRENT -> Theme.strings.currentOrders
                OrderType.PREVIOUS -> Theme.strings.previousOrders
            },
            onClickUpButton = listener::onClickUpButton,
            onClickNotification = listener::onClickNotification
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Spacer(modifier = Modifier.height(16.dp))

            DeliveryStateComponent(modifier = Modifier.fillMaxWidth(), state = state.deliveryStatus)

            OrderInfo(
                modifier = Modifier.padding(horizontal = 16.dp),
                orderNumber = state.orderNumber,
                receiver = state.receiver,
                paymentMethod = state.paymentMethod,
                orderDate = state.orderDate,
                paymentStatus = state.paymentStatus
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = Theme.strings.Products,
                    style = Theme.typography.title, color = Theme.colors.black
                )
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                state.orderProducts.forEachIndexed { index, orderProduct ->
                    ProductItem(orderType,
                        orderProduct = orderProduct,
                        state = state,
                        currencySymbol = state.currency.symbol,
                        exchangeRate = state.currency.exchangeRate,
                        onClickReview = { listener.onClickReview(it) },
                        onClickReturn = {
                            listener.onClickReturnProduct(it)
                        },
                        onClickChat = { listener.onClickChat(index) })
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = Theme.strings.paymentDetails, style = Theme.typography.title
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = Theme.strings.subtotal,
                        style = Theme.typography.body,
                        color = Theme.colors.greyishBrown
                    )
                    Text(
                        text = String.format(
                            "%.3f", state.subTotal * state.currency.exchangeRate
                        ) + " ${state.currency.symbol}",
                        style = Theme.typography.body,
                        color = Theme.colors.greyishBrown
                    )
                }
//            Spacer(modifier = Modifier.height(8.dp))
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = Theme.strings.promoCode,
//                    style = Theme.typography.body,
//                    color = Theme.colors.greyishBrown
//                )
//                Text(
//                    text = String.format(
//                        "%.2f",
//                        state.promoCode * state.currency.exchangeRate
//                    ) + " ${state.currency.symbol}",
//                    style = Theme.typography.body,
//                    color = Theme.colors.greyishBrown
//                )
//            }
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
                    Text(
                        text = String.format(
                            "%.3f", state.taxes * state.currency.exchangeRate
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
                    Log.d("TrackOrderScreenpri", state.totalPrice.toString())
                    Log.d("TrackOrderScreen", state.currency.exchangeRate.toString())
                    Log.d("TrackOrderScreen", state.totalPrice.toString())
                    Text(
                        text = Theme.strings.total, style = Theme.typography.body.copy(
                            fontWeight = FontWeight(
                                650
                            )
                        ), color = Theme.colors.black
                    )
                    Text(
                        text = String.format(
                            "%.3f", state.totalPrice * state.currency.exchangeRate
                        ) + " ${state.currency.symbol}", style = Theme.typography.body.copy(
                            fontWeight = FontWeight(
                                650
                            )
                        ), color = Theme.colors.black
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
    if (state.showRatingDialog) {
        if (model != null) {
            RatingDialog(onPositive = {
                model?.SetReview(
                    state.orderId, state.orderProductIdToRefund, state.rate, state.review
                )
                listener.onDismissReviewDailog()
            },
                onDismissRequest = { listener.onDismissReviewDailog() },
                productImage = state.orderProducts.find { it.id == state.orderProductIdToRefund }?.photo
                    ?: "",
                productName = state.orderProducts.find { it.id == state.orderProductIdToRefund }?.productTitle
                    ?: "",
                currencySymbol = state.currency.symbol,
                totalPrice = state.orderProducts.find { it.id == state.orderProductIdToRefund }?.price
                    ?: 0.0,
                state = state,
                model = model
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.showRefundItemsDialog,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            RefundItemsBottomSheet(orderNumber = state.orderNumber,
                productName = state.orderProducts.find { it.id == state.orderProductIdToRefund }?.productTitle
                    ?: "",
                productImage = state.orderProducts.find { it.id == state.orderProductIdToRefund }?.photo
                    ?: "",
                quantity = state.orderProducts.find { it.id == state.orderProductIdToRefund }?.quantity
                    ?: 0,
                totalPrice = state.orderProducts.find { it.id == state.orderProductIdToRefund }?.price
                    ?: 0.0,
                exchangeRate = state.currency.exchangeRate,
                currencySymbol = state.currency.symbol,
                errorMessage = "",
                loadingState = false,
                onDismiss = {
                    listener.onDismissRefundItemsBottomSheet()
                },
                onClickNext = {
                    listener.onClickNext()
                })
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = state.showReturnProductDialog,
            enter = slideInVertically(initialOffsetY = { it }),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            ReturnProductReasonsBottomSheet(refundReasons = state.refundReasons,
                errorMessage = state.refundErrorMessage,
                loadingState = state.refundLoading,
                onSelectReason = { listener.onSelectRefundReason(it) },
                onDismiss = { listener.onDismissReturnProductBottomSheet() },
                onClickSend = { selectedReasonId, userComment, errorState ->
                    when (errorState) {
                        RefundError.UNSELECTED -> listener.onRefundError(RefundError.UNSELECTED)
                        RefundError.EMPTY_COMMENT -> listener.onRefundError(RefundError.EMPTY_COMMENT)
                        RefundError.ALL_REQUIRED -> listener.onRefundError(RefundError.ALL_REQUIRED)
                        RefundError.NONE -> {
                            listener.onClickSend(selectedReasonId, userComment)
                            listener.onRefundError(RefundError.NONE)
                        }
                    }
                })
        }
    }
}

@Composable
private fun OrderInfo(
    modifier: Modifier = Modifier,
    orderNumber: String,
    receiver: String,
    paymentMethod: String,
    orderDate: String,
    paymentStatus: String
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = Theme.strings.orderNumber,
                        color = Theme.colors.black,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                        )
                    )
                    Text(
                        text = orderNumber,
                        color = Theme.colors.greyishBrown,
                        style = Theme.typography.caption.copy(fontSize = 10.sp)
                    )
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .height(1.dp)
                        .background(Theme.colors.silver.copy(alpha = 0.5f))
                        .align(Alignment.Center)
                ) {}
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = Theme.strings.receiver,
                        color = Theme.colors.black,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                        )
                    )
                    Text(
                        text = receiver,
                        color = Theme.colors.greyishBrown,
                        style = Theme.typography.caption.copy(fontSize = 10.sp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = Theme.strings.paymentMethod,
                        color = Theme.colors.black,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                        )
                    )
                    Text(
                        text = paymentMethod,
                        color = Theme.colors.greyishBrown,
                        style = Theme.typography.caption.copy(fontSize = 10.sp)
                    )
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = Theme.strings.orderDate,
                        color = Theme.colors.black,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                        )
                    )
                    Text(
                        text = if (orderDate.isNotEmpty()) orderDate.split(" ")[0] else "",
                        color = Theme.colors.greyishBrown,
                        style = Theme.typography.caption.copy(fontSize = 10.sp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = Theme.strings.paymentStatus,
                        color = Theme.colors.black,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                        )
                    )
                    Text(
                        text = paymentStatus,
                        color = Theme.colors.greyishBrown,
                        style = Theme.typography.caption.copy(fontSize = 10.sp)
                    )
                }
            }
        }
    }
}


@Composable
private fun ProductItem(
    orderType: OrderType,
    orderProduct: OrderProductUiState,
    currencySymbol: String,
    exchangeRate: Double,
    state: TrackOrderScreenUiState,
    onClickReturn: (Int) -> Unit,
    onClickReview: (Int) -> Unit,
    onClickChat: (Int) -> Unit
) {
    Log.d("ProductItem", orderType.toString())
    Card(
        modifier = Modifier.clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white)
    ) {
        Box(
            modifier = Modifier
                .padding(end = 1.dp)
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
                            .height(130.dp),
                        url = orderProduct.photo,
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
                                modifier = Modifier.fillMaxWidth(),
                                text = orderProduct.productTitle,
                                style = Theme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                item { Spacer(modifier = Modifier.fillMaxWidth(0.30f)) }
                                items(items = orderProduct.serializedOptionsUiState.choices,
                                    key = { it.id }) {
                                    VariantItem(parentTitle = it.parentTitle, childTitle = it.title)
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Log.d("aaaaaaa","${orderProduct.price}")
                            Text(
                                text = String.format(
                                    "%.3f",
                                   ( orderProduct.price*state.currency.exchangeRate) *orderProduct.quantity
                                ) + " $currencySymbol",
                                style = Theme.typography.caption,
                                color = Theme.colors.mediumBrown
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = Theme.strings.Quantity + ": ",
                                    color = Theme.colors.black,
                                    style = Theme.typography.caption.copy(
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                                Text(
                                    text = orderProduct.quantity.toString(),
                                    color = Theme.colors.black,
                                    style = Theme.typography.caption.copy(fontSize = 10.sp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }


                            if (orderProduct.productRefundable && orderType == OrderType.PREVIOUS) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.refund_product),
                                        contentDescription = null,
                                        tint = Theme.colors.mediumBrown
                                    )
                                    Spacer(modifier = Modifier.width(2.dp))
                                    Text(
                                        modifier = Modifier.noRippleEffect {
                                            onClickReturn(
                                                orderProduct.id
                                            )
                                        },
                                        text = Theme.strings.returnProduct,
                                        color = Theme.colors.mediumBrown,
                                        style = Theme.typography.body.copy(
                                            fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                                        )

                                    )
                                }
                            }
                        }
                        if (orderType == OrderType.PREVIOUS) {
                            Divider(thickness = 1.5.dp)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 7.dp, bottom = 7.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val context = LocalContext.current
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.review),
                                        contentDescription = null,
                                        tint = Theme.colors.mediumBrown
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(text = Theme.strings.review,
                                        color = Theme.colors.mediumBrown,
                                        style = Theme.typography.body.copy(
                                            fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                                        ),
                                        modifier = Modifier.noRippleEffect {
                                            if (orderProduct.rated==1){
                                                Toast.makeText(context,"You have previously rated this product. ",Toast.LENGTH_SHORT).show()
                                            }
                                            else{
                                            onClickReview(orderProduct.id)}
                                        })
                                }
                                VerticalDivider(
                                    thickness = 1.5.dp,
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(start = 5.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.chat),
                                        contentDescription = null,
                                        tint = Theme.colors.mediumBrown
                                    )
                                    Spacer(modifier = Modifier.width(5.dp))
                                    Text(
                                        modifier = Modifier.noRippleEffect {
                                            onClickChat(
                                                orderProduct.id
                                            )
                                        },
                                        text = Theme.strings.chat,
                                        color = Theme.colors.mediumBrown,
                                        style = Theme.typography.body.copy(
                                            fontWeight = FontWeight.SemiBold, fontSize = 13.sp
                                        )
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

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun RefundItemsBottomSheetPreview() {
    DhaibanTheme {
        RefundItemsBottomSheet("", "", "", 5, 50.0, 1.0, "$", "", false, {}) {}
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun OrdersScreenContentPreview() {
    val dummyListener = object : TrackOrderScreenInteractionListener {
        override fun onReviewTextChange(text: String) {

        }

        override fun onRateChange(rate: Float) {

        }

        override fun onClickUpButton() {}
        override fun onClickReturnProduct(orderProductId: Int) {}
        override fun onClickReview(orderProductId: Int) {

        }

        override fun onDismissRefundItemsBottomSheet() {}
        override fun onDismissReviewDailog() {

        }

        override fun onSelectRefundReason(optionId: Int) {}
        override fun onClickNext() {}
        override fun onDismissReturnProductBottomSheet() {}
        override fun onClickSend(selectedReasonId: Int, userComment: String) {}
        override fun onRefundError(refundError: RefundError) {}
        override fun onDismissBottomSheet() {}
        override fun onClickNotification() {}
        override fun onClickChat(item: Int) {

        }


    }
    DhaibanTheme {
        TrackOrderScreenContent(
            state = TrackOrderScreenUiState(),
            listener = dummyListener,
            orderID = Int.MIN_VALUE,
            orderType = OrderType.CURRENT
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShimmerEffectPreview() {
    DhaibanTheme {
        LoadingContent(TrackOrderScreenUiState())
    }
}


@Preview(showBackground = true)
@Composable
fun ProductItem() {
    val orderUstate = OrderProductUiState()
//    DhaibanTheme {
//        ProductItem(OrderType.PREVIOUS,
//            orderUstate,
//            "",
//            12.0,
//            onClickReturn = {},
//            onClickReview = {},
//            onClickChat = {})
//    }
}

@Preview(showBackground = true)
@Composable
fun OrderInfoPreview() {
    DhaibanTheme {
        OrderInfo(Modifier, "", "", "", "", "")
    }
}