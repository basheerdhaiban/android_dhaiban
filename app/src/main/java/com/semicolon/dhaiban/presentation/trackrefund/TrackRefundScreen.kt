import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.home.container.MainContainer
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.refund.RefundItemUiState
import com.semicolon.dhaiban.presentation.refund.RefundProductUiState
import com.semicolon.dhaiban.presentation.refund.RequestType
import com.semicolon.dhaiban.presentation.trackrefund.TrackRefundScreenInteractionListener
import com.semicolon.dhaiban.presentation.trackrefund.TrackRefundScreenModel
import com.semicolon.dhaiban.presentation.trackrefund.TrackRefundUiEffect
import com.semicolon.dhaiban.presentation.trackrefund.TrackRefundUiState
import com.semicolon.dhaiban.presentation.trackrefund.composables.RefundStateComponent
import com.semicolon.dhaiban.presentation.utils.Constants.TRACK_REFUND_SCREEN
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class TrackRefundScreen(
    private val refundItem: RefundItemUiState = RefundItemUiState(),
    private val requestType: RequestType = RequestType.PREVIOUS,
    private val refundID: Int = Int.MIN_VALUE,
    private val isComeFromNotification: Boolean = false,

    ) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<TrackRefundScreenModel>(parameters = {
            parametersOf(refundItem, refundID)
        })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: TrackRefundUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current
        LaunchedEffect(Unit) {
            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
            appScreenModel.setCurrentScreen(TRACK_REFUND_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    TrackRefundUiEffect.OnNavigateBack -> if (isComeFromNotification) {
                        SafeNavigator.safePush(
                            navigator,
                            MainContainer(),
                            lifecycleOwner = lifecycleOwner
                        )
                    } else if (navigator.canPop) SafeNavigator.safePop(navigator, lifecycleOwner)

                    TrackRefundUiEffect.OnNavigateToNotificationScreen -> SafeNavigator.safePush(
                        navigator,
                        NotificationScreen(),
                        lifecycleOwner = lifecycleOwner
                    )
                }
            }
        }

        TrackRefundScreenContent(requestType = requestType, state = state, listener = screenModel)
        BackHandler {
            if (isComeFromNotification) {
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
private fun TrackRefundScreenContent(
    requestType: RequestType,
    state: TrackRefundUiState,
    listener: TrackRefundScreenInteractionListener
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        AppBarWithIcon(
            title = when (requestType) {
                RequestType.CURRENT -> Theme.strings.currentRefunds
                RequestType.PREVIOUS -> Theme.strings.previousRefunds
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

            RefundStateComponent(modifier = Modifier.fillMaxWidth(), state = state.refundStatus)

            RefundInfo(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                orderNumber = state.orderNumber,
                receiver = state.receiver,
                orderDate = state.orderDate,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                ProductItem(
                    orderProduct = state.refundProduct,
                    price = state.totalPrice ,
                    currencySymbol = state.currency.symbol,
                    exchangeRate = state.currency.exchangeRate,
                )
            }

        }
    }
}

@Composable
private fun RefundInfo(
    modifier: Modifier = Modifier,
    orderNumber: String,
    receiver: String,
    orderDate: String,
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
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
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
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    )
                    Text(
                        text = receiver,
                        color = Theme.colors.greyishBrown,
                        style = Theme.typography.caption.copy(fontSize = 10.sp)
                    )
                }
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    Text(
//                        text = Theme.strings.paymentMethod,
//                        color = Theme.colors.black,
//                        style = Theme.typography.body.copy(
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 13.sp
//                        )
//                    )
//                    Text(
//                        text = paymentMethod,
//                        color = Theme.colors.greyishBrown,
//                        style = Theme.typography.caption.copy(fontSize = 10.sp)
//                    )
//                }
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
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    )
                    Text(
                        text = if (orderDate.isNotEmpty()) orderDate.split(" ")[0] else "",
                        color = Theme.colors.greyishBrown,
                        style = Theme.typography.caption.copy(fontSize = 10.sp)
                    )
                }
//                Row(
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.spacedBy(4.dp)
//                ) {
//                    Text(
//                        text = Theme.strings.paymentStatus,
//                        color = Theme.colors.black,
//                        style = Theme.typography.body.copy(
//                            fontWeight = FontWeight.SemiBold,
//                            fontSize = 13.sp
//                        )
//                    )
//                    Text(
//                        text = paymentStatus,
//                        color = Theme.colors.greyishBrown,
//                        style = Theme.typography.caption.copy(fontSize = 10.sp)
//                    )
//                }
            }
        }
    }
}

@Composable
private fun ProductItem(
    orderProduct: RefundProductUiState,
    price: Double,
    currencySymbol: String,
    exchangeRate: Double,
) {
    Card(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white)
    ) {
        Box(
            modifier = Modifier
                .padding(end = 12.dp)
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.weight(3f)) {
                    CoilImage(
                        modifier = Modifier
                            .weight(1f)
                            .height(130.dp),
                        url = orderProduct.image,
                        contentDescription = "Product Image",
                        shape = RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(2f)
                            .padding(top = 8.dp, bottom = 8.dp),
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = orderProduct.title,
                                style = Theme.typography.body.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
//                        Row(
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            LazyRow(
//                                modifier = Modifier.fillMaxWidth(),
//                                verticalAlignment = Alignment.CenterVertically
//                            ) {
//                                item { Spacer(modifier = Modifier.fillMaxWidth(0.30f)) }
//                                items(
//                                    items = orderProduct.serializedOptionsUiState.choices,
//                                    key = { it.id }) {
//                                    VariantItem(parentTitle = it.parentTitle, childTitle = it.title)
//                                }
//                            }
//                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = String.format(
                                    "%.3f",
                                    price * exchangeRate
                                ) +
                                        " $currencySymbol ",
                                style = Theme.typography.caption,
                                color = Theme.colors.mediumBrown
                            )
                        }
//                        Row(
//                            modifier = Modifier.fillMaxWidth(),
//                            horizontalArrangement = Arrangement.SpaceBetween
//                        ) {
//                            Row(
//                                verticalAlignment = Alignment.CenterVertically,
//                                horizontalArrangement = Arrangement.spacedBy(4.dp)
//                            ) {
//                                Text(
//                                    text = "Quantity" + ": ",
//                                    color = Theme.colors.black,
//                                    style = Theme.typography.caption.copy(
//                                        fontWeight = FontWeight.SemiBold
//                                    )
//                                )
//                                Text(
//                                    text = orderProduct.quantity.toString(),
//                                    color = Theme.colors.black,
//                                    style = Theme.typography.caption.copy(fontSize = 10.sp)
//                                )
//                            }
//                        }
                    }
                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun OrdersScreenContentPreview() {
    val dummyListener = object : TrackRefundScreenInteractionListener {
        override fun onClickUpButton() {}
        override fun onClickNotification() {}
    }
    DhaibanTheme {
        TrackRefundScreenContent(
            requestType = RequestType.PREVIOUS,
            state = TrackRefundUiState(),
            listener = dummyListener
        )
    }
}