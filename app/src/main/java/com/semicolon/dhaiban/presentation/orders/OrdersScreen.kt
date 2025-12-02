package com.semicolon.dhaiban.presentation.orders

import TrackOrderScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.profile.ProfileScreen
import com.semicolon.dhaiban.presentation.utils.Constants.ORDERS_SCREEN
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.parameter.parametersOf

class OrdersScreen(val orderId: Int = Int.MIN_VALUE) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<OrdersScreenModel>(parameters = { parametersOf(orderId) })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: OrdersScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(ORDERS_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    OrdersScreenUiEffect.OnNavigateBack -> navigator.popUntil { it == ProfileScreen() }
                    is OrdersScreenUiEffect.OnNavigateToTrackOrder -> {
                        when (effect.orderType) {
                            OrderType.CURRENT -> {
                                navigator.push(
                                    TrackOrderScreen( OrderType.CURRENT, effect.order.id)
                                )
                            }

                            OrderType.PREVIOUS -> {
                                navigator.push(
                                    TrackOrderScreen( OrderType.PREVIOUS, effect.order.id)
                                )
                            }
                        }
                    }

                    OrdersScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }
        }
        OrdersScreenContent(state = state, screenModel = screenModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun OrdersScreenContent(
    state: OrdersScreenUiState, screenModel: OrdersScreenModel? = null
) {
    val scope = rememberCoroutineScope()

    val previousOrders = screenModel!!.previousOrdersState.collectAsLazyPagingItems()
    Column(modifier = Modifier.safeDrawingPadding()) {
        AppBarWithIcon(
            countOFunReadNotifcation=state.countOfUnreadMessage,
            title = Theme.strings.orders,
            onClickUpButton = screenModel::onClickUpButton,
            onClickNotification = screenModel::onClickNotification
        )
        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            stickyHeader {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Theme.colors.background),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier
                        .weight(1f)
                        .noRippleEffect { screenModel.onClickOrderType(OrderType.CURRENT) }
                        .padding(vertical = 16.dp), contentAlignment = Alignment.Center)
                    {
                        Text(
                            text = Theme.strings.currentOrders,
                            color = Theme.colors.black,
                            style = Theme.typography.title.copy(
                                fontWeight = when (state.orderType) {
                                    OrderType.CURRENT -> FontWeight.SemiBold
                                    OrderType.PREVIOUS -> FontWeight.Normal
                                }
                            )
                        )
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .noRippleEffect { screenModel.onClickOrderType(OrderType.PREVIOUS) }
                        .padding(vertical = 16.dp), contentAlignment = Alignment.Center)
                    {
                        Text(
                            text = Theme.strings.previousOrders,
                            color = Theme.colors.black,
                            style = Theme.typography.title.copy(
                                fontWeight = when (state.orderType) {
                                    OrderType.CURRENT -> FontWeight.Normal
                                    OrderType.PREVIOUS -> FontWeight.SemiBold
                                }
                            )
                        )
                    }
                }
            }

            if (state.isLoading) {
                item {
                    LoadingContent()
                }
            }

            else {
                if (state.orderType == OrderType.CURRENT) {
                    scope.launch {
                        delay(5000)
                    }
                    item {

                        AnimatedVisibility(visible = state.currentOrders.isEmpty() &&state.gotResponse,enter = fadeIn(animationSpec = tween(durationMillis = 4000, delayMillis = 4000)),
                            exit = fadeOut(animationSpec = tween(durationMillis = 300))) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center
                                ) {
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
                                            text = Theme.strings.noDataFound,
                                            style = Theme.typography.titleLarge.copy(
                                                fontWeight = FontWeight(
                                                    400
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                if (state.orderType == OrderType.PREVIOUS) {
                    item {
                        AnimatedVisibility(visible = previousOrders.itemCount == 0

                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.Center
                                ) {
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
                                            text = Theme.strings.noDataFound,
                                            style = Theme.typography.titleLarge.copy(
                                                fontWeight = FontWeight(
                                                    400
                                                )
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                if (state.orderType == OrderType.CURRENT) {
                    items(items = state.currentOrders, key = { it.id }) {
                        AnimatedVisibility(visible = true) {
                            CurrentOrderItem(order = it) {
                                screenModel.onClickTrackOrder(it, OrderType.CURRENT)
                            }
                        }
                    }
                } else {
                    val listSize = previousOrders.itemCount
                    items(previousOrders.itemCount) { index ->
                        AnimatedVisibility(visible = state.orderType == OrderType.PREVIOUS) {
                            val reversedIndex = listSize - index - 1 // Calculate reversed index
                            val order = previousOrders[reversedIndex]
                            order?.let {
                                PreviousOrderItem(order = it) {
                                    screenModel.onClickTrackOrder(it, OrderType.PREVIOUS)
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
private fun CurrentOrderItem(order: OrderUiState, onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.mediumBrown),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = Theme.strings.receiver,
                        color = Theme.colors.white,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    )
                    Text(
                        text = order.receiver,
                        color = Theme.colors.white,
                        style = Theme.typography.caption
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = when (order.deliveryStatus) {
                            DeliveryStatus.PENDING -> Theme.strings.pending
                            DeliveryStatus.ON_REVIEW -> Theme.strings.underReview
                            DeliveryStatus.READY_TO_DELIVER -> Theme.strings.readyForShipping
                            DeliveryStatus.ON_DELIVERY -> Theme.strings.onDelivery
                            else -> Theme.strings.delivered
                        },
                        color = Theme.colors.white,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        ),
                        maxLines = 1
                    )
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.orderDate,
                    color = Theme.colors.white,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = if (order.orderDate.isNotEmpty()) order.orderDate.split(" ")[0] else "",
                    color = Theme.colors.white,
                    style = Theme.typography.caption
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.paymentMethod,
                    color = Theme.colors.white,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = order.paymentMethod,
                    color = Theme.colors.white,
                    style = Theme.typography.caption
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.paymentStatus,
                    color = Theme.colors.white,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = order.paymentStatus,
                    color = Theme.colors.white,
                    style = Theme.typography.caption
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.orderNumber,
                    color = Theme.colors.white,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = order.orderNumber,
                    color = Theme.colors.white,
                    style = Theme.typography.caption.copy(fontSize = 11.sp),
                    textAlign = TextAlign.Center
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = { onClick() }, modifier = Modifier
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Theme.colors.buttonTextGray,
                        containerColor = Theme.colors.white
                    ), shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp)
                ) {
                    Text(
                        text = Theme.strings.trackOrder,
                        style = Theme.typography.caption.copy(fontWeight = FontWeight.W500),
                        color = Theme.colors.black
                    )
                }
            }
        }
    }
}

@Composable
private fun PreviousOrderItem(order: OrderUiState,   onClick: () -> Unit) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.splashBackground),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = Theme.strings.receiver,
                        color = Theme.colors.black,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    )
                    Text(
                        text = order.receiver,
                        color = Theme.colors.greyishBrown,
                        style = Theme.typography.caption
                    )
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Text(
                        text = when (order.deliveryStatus) {
                            DeliveryStatus.PENDING -> Theme.strings.pending
                            DeliveryStatus.ON_REVIEW -> Theme.strings.underReview
                            DeliveryStatus.READY_TO_DELIVER -> Theme.strings.readyForShipping
                            DeliveryStatus.ON_DELIVERY -> Theme.strings.onDelivery
                            else -> Theme.strings.delivered
                        },
                        color = Theme.colors.emeraldGreen,
                        style = Theme.typography.body.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        ),
                        maxLines = 1
                    )
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.orderDate,
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = if (order.orderDate.isNotEmpty()) order.orderDate.split(" ")[0] else "",
                    color = Theme.colors.greyishBrown,
                    style = Theme.typography.caption
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
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = order.paymentMethod,
                    color = Theme.colors.greyishBrown,
                    style = Theme.typography.caption
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.orderNumber,
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = order.orderNumber,
                    color = Theme.colors.greyishBrown,
                    style = Theme.typography.caption.copy(fontSize = 11.sp),
                    textAlign = TextAlign.Center
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                Button(
                    onClick = { onClick() }, modifier = Modifier
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Theme.colors.mediumBrown,
                        containerColor = Theme.colors.mediumBrown
                    ), shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp)
                ) {
                    Text(
                        text = Theme.strings.trackOrder,
                        style = Theme.typography.caption.copy(fontWeight = FontWeight.W500),
                        color = Theme.colors.white
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun LoadingContent() {
    Column {
        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OrdersScreenContentPreview() {
    DhaibanTheme {
        OrdersScreenContent(state = OrdersScreenUiState(), screenModel = null)
    }
}

@Preview(showBackground = true)
@Composable
fun CurrentOrderItemPreview() {
    DhaibanTheme {
        CurrentOrderItem(
            order = OrderUiState(
                0,
                "23578454-545645849649849849684684184984988498",
                "Sarah Hamdi",
                "2/1/2024",
                "Visa",
                "Paid",
                DeliveryStatus.ON_REVIEW
            )
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviousOrderItemPreview() {
    DhaibanTheme {
        PreviousOrderItem(
            order = OrderUiState(
                0,
                "23578454-545645",
                "Sarah Hamdi",
                "2/1/2024",
                "Visa",
                "Paid",
                DeliveryStatus.ON_REVIEW
            )
        ) {}
    }
}

@Preview
@Composable
private fun OrdersContentPreview() {
    DhaibanTheme {
        LoadingContent()
    }
}