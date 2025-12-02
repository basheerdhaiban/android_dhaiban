package com.semicolon.dhaiban.presentation.paymentstatus


import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.DhaibanAlertDialog
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.home.container.HomeTab
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.orders.OrdersScreen
import com.semicolon.dhaiban.presentation.payment.PaymentScreenInteractionListener
import com.semicolon.dhaiban.presentation.payment.PaymentScreenModel
import com.semicolon.dhaiban.presentation.payment.PaymentScreenUiEffect
import com.semicolon.dhaiban.presentation.payment.PaymentScreenUiState
import com.semicolon.dhaiban.presentation.payment.StatusPaymentType
import com.semicolon.dhaiban.presentation.utils.Constants.PAYMENT_SCREEN
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

data class PaymentStatusScreen(

    private val status: StatusPaymentType = StatusPaymentType.PENDING,
    private val amount: Double = 0.0,
    private val orderID: Int = 0,
    private val isComeFromCart: Boolean = false,
) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<PaymentScreenStatusModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: PaymentStatusScreenUiState by screenModel.state.collectAsState()

        screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
Log.d("orderIDPaymentStatusScreen",orderID.toString())
            appScreenModel.setCurrentScreen(PAYMENT_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    PaymentScreenStatusUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    PaymentScreenStatusUiEffect.OnNavigateToHome -> {
                        if (navigator.canPop) navigator.pop()
                        navigator.parent?.replace(HomeTab)
                    }

                    is PaymentScreenStatusUiEffect.OnNavigateToTrackOrder -> {
                        navigator.replace(OrdersScreen(orderId = effect.orderId))
                    }

                    PaymentScreenStatusUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )

                    else -> {}
                }
            }
        }
        Log.d("PaymentScreenContentway", state.paymentWay.toString())
        PaymentstatusScreenContent(
            state = state,
            listener = screenModel,
            model = screenModel,
            status = status,
            amount = amount.toString(), orderID = orderID
        )
    }
}

@Composable
private fun PaymentstatusScreenContent(
    status: StatusPaymentType,
    amount: String,
    orderID: Int,
    state: PaymentStatusScreenUiState,
    listener: PaymentScreenstatusInteractionListener,
    model: PaymentScreenStatusModel?
) {
    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Theme.colors.mediumBrown,

            )
        }
    } else {
        Column {
            AppBarWithIcon(
                title = Theme.strings.payment,
                onClickUpButton = listener::onClickUpButton,
                onClickNotification = listener::onClickNotification
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .safeDrawingPadding(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(15.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Image(
                        modifier = Modifier
                            .width(22.dp)
                            .height(22.dp),
                        painter = painterResource(id = R.drawable.momo),
                        contentDescription = ""
                    )
                    Text(
                        text = "momopay",
                        style = Theme.typography.headline.copy(fontSize = 16.sp),
                        modifier = Modifier.padding(top = 0.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Payment Details",
                    style = Theme.typography.headline.copy(fontSize = 14.sp),
                )
                Spacer(modifier = Modifier.height(12.dp))

                Divider(thickness = 1.5.dp)
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Log.d("amountpaymentstatusScreen", state.amount.toString())
                    Text(
                        text = "total",
                        style = Theme.typography.otherHeading.copy(fontSize = 15.sp)
                    )
                    Text(
                        state.amount.toString()
                                + " ${state.currencySymbol}",
                        style = Theme.typography.otherHeading.copy(fontSize = 15.sp)
                    )

                }
                Log.d("StatusPaymentTypeScreen", state.paymentResponse.toString())

                if (state.statusPaymentMomo == "PENDING") {
                    ContentOfPendingstatus(listener,model)
                } else if (state.statusPaymentMomo == "SUCCESSFUL") {
                    ContentOfsuccessStatus(listener)
                } else {
                    ContentOffailedstatus(listener)
                }


            }

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
                    onDismiss = { listener.onClickBackToHome() },
                    onPositive = {
                        listener.onClickTrackOrder(state.orderUiState.orderId)
                    })
            }

        }
    }

}

@Composable
fun ContentOfPendingstatus(
    listener: PaymentScreenstatusInteractionListener,
    model: PaymentScreenStatusModel?
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.pending_status_payment),
                    contentDescription = "No Data Icon",
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Pending",
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
                    text = "Please Retry",
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

            Row {
                val context = LocalContext.current
                Button(onClick = {
                                 model?.trackPaymentTransactionOfMomo()

                },
                    modifier = Modifier
                        .noRippleEffect {
                            model?.trackPaymentTransactionOfMomo()

                        }
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Theme.colors.mediumBrown
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = true) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Retry",
                        style = Theme.typography.title,
                        color = Theme.colors.white
                    )
                }
            }
        }
    }
}

@Composable
fun ContentOffailedstatus(
    listener: PaymentScreenstatusInteractionListener,
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.failed_payment_status),
                    contentDescription = "No Data Icon",
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Failed",
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
                    text = "SomeThing went Wrong",
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

            Row {
                val context = LocalContext.current
                Button(onClick = {
                    listener.onClickUpButton()                },
                    modifier = Modifier
                        .noRippleEffect {

                        }
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Theme.colors.mediumBrown
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = true) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Go Back",
                        style = Theme.typography.title,
                        color = Theme.colors.white
                    )
                }
            }
        }
    }
}

@Composable
fun ContentOfsuccessStatus(
    listener: PaymentScreenstatusInteractionListener,
) {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.confirmedpayment_status),
                    contentDescription = "No Data Icon",
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Confirmed",
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
                    text = "Booking done successfully",
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

            Row {
                val context = LocalContext.current
                Button(onClick = {
listener.onClickUpButton()
                },
                    modifier = Modifier
                        .noRippleEffect {

                        }
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Theme.colors.mediumBrown
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = true) {
                    Text(
                        modifier = Modifier.padding(vertical = 8.dp),
                        text = "Go Back",
                        style = Theme.typography.title,
                        color = Theme.colors.white
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentStatusScreenpreview() {
    val dummyListener = object : PaymentScreenstatusInteractionListener {
        override fun onClickUpButton() {

        }

        override fun onClickBackToHome() {

        }

        override fun onClickTrackOrder(orderId: Int) {

        }

        override fun onClickNotification() {

        }


    }
    DhaibanTheme {
        PaymentstatusScreenContent(
            state = PaymentStatusScreenUiState(),
            listener = dummyListener,
            model = null,
            status = StatusPaymentType.REJECTED,
            amount = "0", orderID = 0
        )
    }
}