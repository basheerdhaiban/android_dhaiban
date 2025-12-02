package com.semicolon.dhaiban.presentation.refund

import TrackRefundScreen
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants
import kotlinx.coroutines.flow.collectLatest

class RefundScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<RefundScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {

            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())

            appScreenModel.setCurrentScreen(Constants.REFUND_SCREEN)

            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    RefundScreenUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    is RefundScreenUiEffect.OnNavigateToTrackRefund -> navigator.push(
                        TrackRefundScreen(effect.refundItem, effect.requestType,effect.refundItem.id)
                    )

                    RefundScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }
        }

        RefundScreenContent(state = state, listener = screenModel)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun RefundScreenContent(
    state: RefundScreenUiState,
    listener: RefundScreenInteractionListener,
) {
    Column(modifier = Modifier.safeDrawingPadding()) {

        AppBarWithIcon(
            countOFunReadNotifcation=state.countOfUnreadMessage,
            title = Theme.strings.refund,
            onClickUpButton = listener::onClickUpButton,
            onClickNotification = listener::onClickNotification
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
                        .noRippleEffect { listener.onClickRequestType(RequestType.CURRENT) }
                        .padding(vertical = 16.dp), contentAlignment = Alignment.Center)
                    {
                        Text(
                            text = Theme.strings.currentRequests,
                            color = Theme.colors.black,
                            style = Theme.typography.title.copy(
                                fontWeight = when (state.requestType) {
                                    RequestType.CURRENT -> FontWeight.SemiBold
                                    RequestType.PREVIOUS -> FontWeight.Normal
                                }
                            )
                        )
                    }
                    Box(modifier = Modifier
                        .weight(1f)
                        .noRippleEffect { listener.onClickRequestType(RequestType.PREVIOUS) }
                        .padding(vertical = 16.dp), contentAlignment = Alignment.Center)
                    {
                        Text(
                            text = Theme.strings.previousRequests,
                            color = Theme.colors.black,
                            style = Theme.typography.title.copy(
                                fontWeight = when (state.requestType) {
                                    RequestType.CURRENT -> FontWeight.Normal
                                    RequestType.PREVIOUS -> FontWeight.SemiBold
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
            } else {
                Log.d("Refund Screen",state.currentRefundItems.toString())
                if (state.requestType == RequestType.CURRENT) {
                    item {
                        /*AnimatedVisibility(visible = state.currentOrders.isEmpty()) {
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
                        }*/
                    }
                }
                if (state.requestType == RequestType.PREVIOUS) {
                    item {
                        /*AnimatedVisibility(visible = previousOrders.itemCount == 0) {
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
                        }*/

                    }

                }
                if (state.requestType == RequestType.CURRENT) {
                    items(items = state.currentRefundItems, key = { it.id }) {
                        AnimatedVisibility(visible = true) {
                            CurrentRefundItem(refundItemUiState = it, currency = state.currency) {
                                listener.onClickTrackOrder(it, RequestType.CURRENT)
                            }
                        }
                    }
                } else {
                    items(items = state.previousRefundItems, key = { it.id }) {
                        AnimatedVisibility(visible = true) {
                            PreviousRefundItem(refundItemUiState = it, currency = state.currency) {
                                listener.onClickTrackOrder(it, RequestType.PREVIOUS)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentRefundItem(
    refundItemUiState: RefundItemUiState,
    currency: RefundCurrencyUiState,
    onClick: () -> Unit
) {
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.weight(3f),
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
                        text = refundItemUiState.orderNumber,
                        color = Theme.colors.white,
                        style = Theme.typography.caption.copy(fontSize = 11.sp),
                        textAlign = TextAlign.Center
                    )
                }

                Row(
                    modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = when (refundItemUiState.refundStatus) {
                            RefundStatus.PENDING -> Theme.strings.pending
                            RefundStatus.ACCEPTED -> Theme.strings.accepted
                            else -> Theme.strings.declined
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
                    text = Theme.strings.receiver,
                    color = Theme.colors.white,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = refundItemUiState.receiver,
                    color = Theme.colors.white,
                    style = Theme.typography.caption
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${Theme.strings.product}: " + refundItemUiState.product.title,
                    color = Theme.colors.white,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = refundItemUiState.refundProduct.title,
                    color = Theme.colors.white,
                    style = Theme.typography.caption
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Payment Details",
                    color = Theme.colors.white,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.totalPrice,
                    color = Theme.colors.white,
                    style = Theme.typography.caption
                )
                Text(
                    text = String.format(
                        "%.3f",
                        refundItemUiState.totalPrice *currency.exchangeRate
                    ) + " " + currency.symbol,
                    color = Theme.colors.white,
                    style = Theme.typography.caption
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
private fun PreviousRefundItem(
    refundItemUiState: RefundItemUiState,
    currency: RefundCurrencyUiState,
    onClick: () -> Unit
) {
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.weight(3f),
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
                        text = refundItemUiState.orderNumber,
                        color = Theme.colors.black,
                        style = Theme.typography.caption.copy(fontSize = 11.sp),
                        textAlign = TextAlign.Center
                    )
                }

                Row(
                    modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = when (refundItemUiState.refundStatus) {
                            RefundStatus.PENDING -> Theme.strings.pending
                            RefundStatus.ACCEPTED -> Theme.strings.accepted
                            else-> Theme.strings.declined
                        },
                        color = when (refundItemUiState.refundStatus) {
                            RefundStatus.PENDING -> Theme.colors.black
                            RefundStatus.ACCEPTED -> Theme.colors.emeraldGreen
                            else -> Theme.colors.rustyRedTwo
                        },
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
                    text = Theme.strings.receiver,
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = refundItemUiState.receiver,
                    color = Theme.colors.black,
                    style = Theme.typography.caption
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.product + " : "+ refundItemUiState.product.title,
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = refundItemUiState.refundProduct.title,
                    color = Theme.colors.black,
                    style = Theme.typography.caption
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Payment Details",
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = Theme.strings.totalPrice,
                    color = Theme.colors.black,
                    style = Theme.typography.caption
                )
                Text(
                    text = String.format(
                        "%.3f",
                        refundItemUiState.totalPrice * currency.exchangeRate
                    ) + " " + currency.symbol,
                    color = Theme.colors.black,
                    style = Theme.typography.caption
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

@Preview(showBackground = true)
@Composable
fun CurrentOrderItemPreview() {
    DhaibanTheme {
        CurrentRefundItem(
            refundItemUiState = RefundItemUiState(
                0,
                "23578454-545645849649849849684684184984988498",
                "Sarah Hamdi",
                "2/1/2024",
                "Visa",
                refundStatus = RefundStatus.ACCEPTED,
                totalPrice = 0.0,
                refundProduct = RefundProduct(),
                addressId = 0
            ),
            currency = RefundCurrencyUiState()
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun PreviousRefundItemPreview() {
    DhaibanTheme {
        PreviousRefundItem(
            refundItemUiState = RefundItemUiState(
                0,
                "23578454-545645849649849849684684184984988498",
                "Sarah Hamdi",
                "2/1/2024",
                "Visa",
                refundStatus = RefundStatus.ACCEPTED,
                totalPrice = 0.0,
                refundProduct = RefundProduct(),
                addressId = 0
            ),
            currency = RefundCurrencyUiState()
        ) {}
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
    val dummyListener = object : RefundScreenInteractionListener {
        override fun onClickUpButton() {}
        override fun onClickRequestType(requestType: RequestType) {}
        override fun onClickTrackOrder(refundItem: RefundItemUiState, requestType: RequestType) {}
        override fun onClickNotification() {}
    }
    DhaibanTheme {
        RefundScreenContent(state = RefundScreenUiState(), listener = dummyListener)
    }
}