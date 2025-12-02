package com.semicolon.dhaiban.presentation.payment

import CartScreen
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.transitions.SlideTransition
import com.semicolon.data.repository.remote.repository.OrderRepositoryImp.Companion.gson
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
import com.semicolon.dhaiban.presentation.payment.composables.MomoPayDialog
import com.semicolon.dhaiban.presentation.payment.composables.WalletDialog
import com.semicolon.dhaiban.presentation.paymentstatus.PaymentStatusScreen
import com.semicolon.dhaiban.presentation.paymentstatus.WebViewScreen
import com.semicolon.dhaiban.presentation.utils.Constants.PAYMENT_SCREEN
import com.semicolon.dhaiban.utils.PaymentMethodsType
import com.semicolon.domain.entity.PayTabsModel
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

data class PaymentScreen(
    private val subTotal: Double,
    private val discountcode: String,
    private val discount: Double,
    private val taxes: Double,
    private val total: Double,
    private val currencySymbol: String,
    private val currencyId: Int,
    private val addressId: Int,
) : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<PaymentScreenModel>(parameters = {
            parametersOf(
                subTotal, discount, taxes, total, currencySymbol, currencyId, addressId
            )
        })
        Log.d("discountcode", discountcode)
        val appScreenModel = getScreenModel<AppScreenModel>()

        val state: PaymentScreenUiState by screenModel.state.collectAsState()

        screenModel.updateCurrencyUiState(appScreenModel.getCurrency())
        state.promoCode = discountcode
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            screenModel.getCartProducts()
            screenModel.getCurrentBalance()
            Log.d("PaymentScreen", "OrderId: "+state.orderUiState.orderId.toString())

            appScreenModel.setCurrentScreen(PAYMENT_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    PaymentScreenUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    PaymentScreenUiEffect.OnNavigateToHome -> {
                        if (navigator.canPop) navigator.pop()
                        navigator.parent?.replace(HomeTab)
                    }

                    is PaymentScreenUiEffect.OnNavigateToTrackOrder -> {
                        state.orderUiState.orderId = effect.orderId
                        navigator.replace(OrdersScreen(orderId = effect.orderId))
                    }

                    PaymentScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )

                    is PaymentScreenUiEffect.OnNavigateToStatusPaymentScreen -> {
                        navigator.replace(
                            PaymentStatusScreen(
                                status = effect.status,
                                amount = effect.amount, orderID = effect.orderID
                            )
                        )
                    }
                }
            }
        }
        Log.d("PaymentScreenContentWay", state.paymentWay.toString())
        PaymentScreenContent(state = state, listener = screenModel, model = screenModel)
    }
}

@Composable
private fun PaymentScreenContent(
    state: PaymentScreenUiState,
    listener: PaymentScreenInteractionListener,
    model: PaymentScreenModel?
) {

    val rotationState by animateFloatAsState(
        targetValue = if (state.expandDebitCards) 180f else 0f, label = ""
    )
    Column {
        AppBarWithIcon(
            title = Theme.strings.payment,
            onClickUpButton = listener::onClickUpButton,
            onClickNotification = listener::onClickNotification
        )
        if (state.isLoadingStatusPayment) {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Theme.colors.mediumBrown,

                    )
            }
        } else {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .safeDrawingPadding(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                LazyColumn(
                    modifier = Modifier.weight(0.9f),
                    contentPadding = PaddingValues(vertical = 16.dp)
                ) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Image(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(22.dp)
                                        .padding(top = 6.dp),
                                    painter = painterResource(id = R.drawable.wallet),
                                    contentDescription = ""
                                )
                                Column() {
                                    Text(
                                        text = Theme.strings.useYourWallet,
                                        style = Theme.typography.title,
                                        color = Theme.colors.black
                                    )
                                    Text(
                                        text = String.format(
                                            "%.1f", state.balance * state.currency.exchangeRate
                                        ) + " " + state.currency.symbol,
                                        style = Theme.typography.body,
                                        color = Theme.colors.greyishBrown
                                    )
                                }
                            }
                            Checkbox(modifier = Modifier
                                .size(20.dp)
                                .padding(end = 5.dp),
                                checked = state.isWalletSelected,
                                onCheckedChange = {
                                    listener.onClickWallet(isWalletSelected = it)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Theme.colors.mediumBrown,
                                    uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )
//                            Button(
//                                shape = RoundedCornerShape(8.dp), onClick = {
//                                    listener.onClickWallet()
//                                }, colors = ButtonDefaults.buttonColors(
//                                    containerColor = Theme.colors.mediumBrown
//                                )
//                            ) {
//
//                                Text(
//                                    text = Theme.strings.use,
//                                    style = Theme.typography.otherHeading,
//                                    color = Theme.colors.white
//                                )
//                            }
                        }
                        Spacer(modifier = Modifier.height(9.dp))
                        Divider(thickness = 1.5.dp)
                        Spacer(modifier = Modifier.height(9.dp))

                    /*Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Theme.strings.creditDebitCards,
                            style = Theme.typography.title,
                            color = Theme.colors.black
                        )
                        Icon(
                            modifier = Modifier
                                .padding(end = 5.dp)
                                .rotate(rotationState)
                                .noRippleEffect {
                                    listener.onClickExpandDebitCards()
                                },
                            imageVector = ImageVector.vectorResource(R.drawable.up_arrow),
                            contentDescription = "Down Arrow"
                        )
                    }

                    AnimatedVisibility(visible = state.expandDebitCards) {
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Spacer(modifier = Modifier.height(16.dp))
                            CreditCardItem(image = R.drawable.visa_icon, cardHolder = "Visa")
                            CreditCardItem(
                                image = R.drawable.master_card_icon, cardHolder = "Master Card"
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))



                    AnimatedContent(targetState = state.addCreditState, label = "") { addCredit ->
                        Spacer(modifier = Modifier.height(16.dp))
                        if (addCredit) {
                            AddCreditComponent(
                                holderName = state.debitCardUiState.holderName,
                                cardNumber = state.debitCardUiState.cardNumber,
                                month = state.debitCardUiState.month,
                                year = state.debitCardUiState.year,
                                securityCode = state.debitCardUiState.securityCode,
                                rememberMyCard = state.debitCardUiState.rememberMyCardInfo
                            ) {
                                listener.onClickDismissAddCredit()
                            }
                        } else {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .noRippleEffect { listener.onClickAddNewCredit() },
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Start
                            ) {
                                Text(
                                    text = Theme.strings.addNewCreditDebitCard,
                                    style = Theme.typography.body,
                                    color = Theme.colors.black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedVisibility(visible = state.addCreditState.not()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(Theme.colors.silver.copy(alpha = 0.5f))
                        )
                    }*/
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Image(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(22.dp),
                                    painter = painterResource(id = R.drawable.paytabs),
                                    contentDescription = ""
                                )
                                Text(
                                    text = stringResource(R.string.payTabs),
                                    style = Theme.typography.title,
                                    color = Theme.colors.black
                                )
                                Image(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp),
                                    painter = painterResource(id = R.drawable.master_card),
                                    contentDescription = ""
                                )
                                Image(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(30.dp),
                                    painter = painterResource(id = R.drawable.visa),
                                    contentDescription = ""
                                )
                                Image(
                                    modifier = Modifier
//                                        .width(20.dp)
                                        .height(35.dp),
                                    painter = painterResource(id = R.drawable.pci_dss),
                                    contentDescription = ""
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Checkbox(modifier = Modifier
                                .size(20.dp)
                                .padding(end = 5.dp),
                                checked = state.isPayTabSelected,
                                onCheckedChange = {
                                    listener.onClickPayTabs(isPayTabSelected = it)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Theme.colors.mediumBrown,
                                    uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )
                            /*Button(
                                shape = RoundedCornerShape(8.dp), onClick = {
                                    listener.onClickPayTabs()
                                }, colors = ButtonDefaults.buttonColors(
                                    containerColor = Theme.colors.mediumBrown
                                )
                            ) {
                                Text(
                                    text = Theme.strings.use,
                                    style = Theme.typography.otherHeading,
                                    color = Theme.colors.white
                                )
                            }*/
                        }
                        Spacer(modifier = Modifier.height(9.dp))
                        Divider(thickness = 1.5.dp)
                        Spacer(modifier = Modifier.height(9.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Image(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(22.dp),
                                    painter = painterResource(id = R.drawable.momo),
                                    contentDescription = ""
                                )
                                Text(
                                    text = Theme.strings.momoPay,
                                    style = Theme.typography.title,
                                    color = Theme.colors.black
                                )
                            }
                            Checkbox(modifier = Modifier
                                .size(20.dp)
                                .padding(end = 5.dp),
                                checked = state.isMomoPaySelected,
                                onCheckedChange = {
                                    listener.onClickMomoPay(isMomoPaySelected = it)
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Theme.colors.mediumBrown,
                                    uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )
                            /*Button(
                                shape = RoundedCornerShape(8.dp), onClick = {
                                    listener.onClickMomoPay()
                                }, colors = ButtonDefaults.buttonColors(
                                    containerColor = Theme.colors.mediumBrown
                                )
                            ) {
                                Text(
                                    text = Theme.strings.use,
                                    style = Theme.typography.otherHeading,
                                    color = Theme.colors.white
                                )
                            }*/
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        /*Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Image(
                                    modifier = Modifier
                                        .width(30.dp)
                                        .height(22.dp),
                                    painter = painterResource(id = R.drawable.cash),
                                    contentDescription = ""
                                )
                                Text(
                                    modifier = Modifier.padding(top = 6.dp),
                                    text = Theme.strings.cash,
                                    style = Theme.typography.title,
                                    color = Theme.colors.black
                                )
                            }

                            Checkbox(
                                modifier = Modifier
                                    .size(20.dp)
                                    .padding(end = 5.dp, top = 6.dp),
                                checked = state.cashCheckBox,
                                onCheckedChange = {
                                    listener.onCheckCash(it)
                                    Log.d("checked", it.toString())
                                },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = Theme.colors.mediumBrown,
                                    uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(
                                        alpha = 0.5f
                                    )
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))*/

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Text(
                                text = Theme.strings.paymentDetails, style = Theme.typography.title
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth()
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
                                        "%.2f", state.subTotal
                                    ) + " ${state.currency.symbol}",
                                    style = Theme.typography.body,
                                    color = Theme.colors.greyishBrown
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            AnimatedVisibility(
                                visible = state.promoDiscount != 0.00,
                                enter = fadeIn(animationSpec = tween(500)),
                                exit = fadeOut(animationSpec = tween(500))
                            ) {
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
                                            "%.2f", -1 * state.promoDiscount
                                        ) + " ${state.currency.symbol}",
                                        style = Theme.typography.body,
                                        color = Color.Red
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
                                Text(
                                    text = String.format(
                                        "%.2f", state.taxes
                                    ) + " ${state.currency.symbol}",
                                    style = Theme.typography.body,
                                    color = Theme.colors.greyishBrown
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            AnimatedVisibility(
                                visible = state.amountFromWallet != 0.00,
                                enter = fadeIn(animationSpec = tween(500)),
                                exit = fadeOut(animationSpec = tween(500))
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {

                                    Text(
                                        text = Theme.strings.wallet,
                                        style = Theme.typography.body,
                                        color = Theme.colors.greyishBrown
                                    )
                                    Text(
                                        text = String.format(
                                            "%.2f", -1 * state.amountFromWallet
                                        ) + " ${state.currency.symbol}",
                                        style = Theme.typography.body,
                                        color = Color.Red
                                    )
                                }
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
                                    text = Theme.strings.total, style = Theme.typography.body.copy(
                                        fontWeight = FontWeight(
                                            650
                                        )
                                    ), color = Theme.colors.black
                                )

                                Log.d("paymentScreen", "${state.total}")
                                Text(
                                    text = String.format(
                                        "%.2f",
                                        state.total - state.promoDiscount
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

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                }
                var outOfStock: Boolean = false
                val scope = rememberCoroutineScope()
                for (item in state.items) {
                    Log.d("for in qunantity", "for in qunantity")

                    if (
                        item.product.currentStock < 1
                    ) {
                        outOfStock = true
                        Log.d("quantity out of stock", "quantity out of stock")
                    }
                }
                AnimatedVisibility(visible = outOfStock) {
                    Column(modifier = Modifier.padding(top = 6.dp , bottom = 10.dp)) {
                        for (item in state.items) {
                            if (
                                item.product.currentStock < 1
                            ) {
                                Text(text = "This Product ${item.product.title} Out Of stock ", color = Color.Red)
                            }
                        }
                    }
                }
                Row {
                    val context = LocalContext.current
                    Button(onClick = {
                        Toast.makeText(context, "PlaceOrder Button Clicked", Toast.LENGTH_LONG).show()
                        if (state.paymentWay == PaymentMethodsType.NONE) {
                            Toast.makeText(context, context.getString(
                                R.string.you_should_select_payment_way),
                                Toast.LENGTH_LONG).show()
                        } else if (outOfStock) {
                            Toast.makeText(context, context.getString(
                                R.string.there_is_product_out_of_stock),
                                Toast.LENGTH_LONG).show()
                        } else {
                            listener.onClickPlaceOrder()
                        }
                    },
                        modifier = Modifier
                            .noRippleEffect {

                            }
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Theme.colors.mediumBrown
                        ),
                        shape = RoundedCornerShape(8.dp),
                        enabled = true) {
                        Text(
                            modifier = Modifier.padding(vertical = 8.dp),
                            text = Theme.strings.placeOrder,
                            style = Theme.typography.title,
                            color = Theme.colors.white
                        )
                    }
                }

            }
        }

    }
    AnimatedVisibility(
        visible = state.orderUiState.orderId != 0 && state.orderUiState.message != "",
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
    ) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter
        ) {
            DhaibanAlertDialog(title = Theme.strings.The_order_has_been_requested_successfully,
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
//                        Text(text = "${Theme.strings.orderNumber} ${state.orderUiState.orderId}")
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
    val context = LocalContext.current
    if (state.showWallatDialog) {
        Log.d("WalletDialog", "WalletDialog")
        if (model != null) {
            WalletDialog(
                state.balance, state, model,
                onPositive = {
                    listener.onChosseWalletAsPayment()
                    listener.onDismissWalletDailog()

                },
                onDismissRequest = {
                    listener.onDismissWalletDailog()
                },

                )
        }

    }
    if (state.showMomoPayDialog) {
        Log.d("WalletDialog", "WalletDialog")
        if (model != null) {
            MomoPayDialog(state, model, onPositive = {
                Log.d("", "")
                if (state.momoWay.isEmpty() || state.numberOfAccountMomoPay.isEmpty()) {
                    Toast.makeText(
                        context,
                        "You should select the way and enter number",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    model.makePayment()
                    listener.onDismissMomoPayDailog()
                }
            }, onDismissRequest = {
                listener.onDismissMomoPayDailog()
            }
            )
        }
    }

    if (state.showPayTabsOptionSelected) {
        Log.d("showPayTabsView", "PayTabs: showPayTabsOptionSelected")
        state.paymentWay = PaymentMethodsType.PAYTABS
        state.showPayTabsOptionSelected = false;
    }
    if (state.showPayTabsOptionPaymentWebview) {
        Log.d("showPayTabsView", "PayTabs: showPayTabsOptionPaymentWebview")
//        val url = "https://dhaibantrading.com/policy.html"
        val url: String? = state.payTabsModel?.redirect_url
        if (url?.isNotEmpty() == true) {
            state.showPayTabsOptionPaymentWebview = false
            WebInspectorScreen(url = url, tran_ref = state.payTabsModel?.tran_ref, listener = listener)
        } else {
            Log.d("showPayTabsView", "PayTabs: Invalid Redirect URL")
            Toast.makeText(context, "Invalid Redirect URL", Toast.LENGTH_SHORT).show()
        }
    }

    if (state.showPayTabsStatus) {
        state.showPayTabsStatus = false
        Content()
    }

}

fun extractCleanJson(html: String): PayTabsModel? {
    val regex = "\\{.*\\}".toRegex()
    val raw = regex.find(html)?.value ?: return null
    var jsonString =  raw.replace("\\\"", "\"")
    println("CleanJson: $jsonString")
    if (jsonString.isNotEmpty()) {
        val model = gson.fromJson(jsonString, PayTabsModel::class.java)
        return model
    } else {
        return null
    }

}
@Composable
fun WebInspectorScreen(url: String, tran_ref: String?,
                       listener: PaymentScreenInteractionListener) {
    var htmlContent by remember { mutableStateOf("") }
    WebViewScreen(
        url = url,
        onHtmlRetrieved = { html ->
            htmlContent = html
            println("WebViewScreen - HTML Content: $html")
            if (htmlContent.contains("status") && (htmlContent.contains("failed") || htmlContent.contains("paid"))) {
//                var model = extractCleanJson(htmlContent)
                if (tran_ref?.isNotEmpty() == true) {
                    listener.paytabsCheckStatus(tran_ref = tran_ref)
                }
            }
        },
//        onReceivedClientCertRequest = { request ->
//            println("WebViewScreen - Client Cert Request: $request")
//        },
//        onRequestIntercepted = { request ->
//            println("WebViewScreen - Intercepted: ${request?.url}")
//        }
    )

    // You can also show parsed HTML summary
    if (htmlContent.isNotEmpty()) {
        println("WebViewScreen - Page length: ${htmlContent.length}")
    }
}

@Composable
fun Content() {
    Navigator(screen = CartScreen()) {
        SlideTransition(it)
    }
}
@Composable
private fun AddCreditComponent(
    holderName: String,
    cardNumber: String,
    month: String,
    year: String,
    securityCode: String,
    rememberMyCard: Boolean,
    onClickDismiss: () -> Unit
) {
    Card(
        modifier = Modifier
            .background(Theme.colors.transparent)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = Theme.strings.creditDebitCards,
                    style = Theme.typography.title,
                    color = Theme.colors.black
                )
                Icon(
                    modifier = Modifier.noRippleEffect {
                        onClickDismiss()
                    },
                    imageVector = ImageVector.vectorResource(R.drawable.up_arrow),
                    contentDescription = "Down Arrow"
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                value = holderName,
                onValueChange = { },
                shape = RoundedCornerShape(10.dp),
                placeholder = {
                    Text(
                        text = "Card holder's Name",
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

            OutlinedTextField(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth(),
                value = cardNumber,
                onValueChange = { },
                shape = RoundedCornerShape(10.dp),
                placeholder = {
                    Text(
                        text = "Card Number",
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

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Expire Date", style = Theme.typography.title)
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f),
                    value = month,
                    onValueChange = { },
                    shape = RoundedCornerShape(10.dp),
                    placeholder = {
                        Text(
                            text = "Month",
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
                Spacer(modifier = Modifier.weight(0.05f))
                OutlinedTextField(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f),
                    value = year,
                    onValueChange = { },
                    shape = RoundedCornerShape(10.dp),
                    placeholder = {
                        Text(
                            text = "Year",
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
            }
            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .height(50.dp)
                        .weight(1f),
                    value = securityCode,
                    onValueChange = { },
                    shape = RoundedCornerShape(10.dp),
                    placeholder = {
                        Text(
                            text = "Security Code",
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
                Spacer(modifier = Modifier.weight(0.05f))
                Spacer(modifier = Modifier.weight(1f))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    modifier = Modifier.size(20.dp),
                    checked = false,
                    onCheckedChange = {},
                    colors = CheckboxDefaults.colors(
                        checkedColor = Theme.colors.mediumBrown,
                        uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(alpha = 0.5f)
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Remember my card info",
                    color = Theme.colors.dimGray,
                    style = Theme.typography.caption
                )
            }
        }
    }


}
@Composable
private fun CreditCardItem(
    image: Int = R.drawable.image_error_icon,
    cardHolder: String = "Sarah Hamdi",
    cardNumber: String = "*** *** 2587"
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Image(
                modifier = Modifier
                    .width(80.dp)
                    .height(50.dp),
                painter = painterResource(id = image),
                contentDescription = ""
            )
            Column(modifier = Modifier.fillMaxHeight(), verticalArrangement = Arrangement.Center) {
                Text(
                    text = cardHolder,
                    style = Theme.typography.caption.copy(fontWeight = FontWeight.SemiBold),
                    color = Theme.colors.black
                )
                Text(
                    text = cardNumber,
                    style = Theme.typography.caption,
                    color = Theme.colors.dimGray
                )
            }
        }

        Checkbox(
            modifier = Modifier.size(20.dp),
            checked = false,
            onCheckedChange = {},
            colors = CheckboxDefaults.colors(
                checkedColor = Theme.colors.mediumBrown,
                uncheckedColor = CheckboxDefaults.colors().uncheckedBorderColor.copy(alpha = 0.5f)
            )
        )

    }

}

@Preview(showBackground = true)
@Composable
fun CreditCardItemPreview() {
    DhaibanTheme {
        CreditCardItem()
    }
}

@Preview(showBackground = true)
@Composable
fun AddCreditComponentPreview() {
    DhaibanTheme {
        AddCreditComponent("", "", "", "", "", false) {

        }
    }
}


@Preview(showBackground = true)
@Composable
fun MyProfileScreenPreview() {
    val dummyListener = object : PaymentScreenInteractionListener {
        override fun onChosseCashAsPayment() {}
        override fun deletePayment() {}
        override fun onClickPaymentStatus(status: StatusPaymentType, amount: Double) {}
        override fun onClickUpButton() {}
        override fun onClickPlaceOrder() {}
        override fun onClickTrackOrder(orderId: Int) {}
        override fun onClickWallet(isWalletSelected: Boolean) {}
        override fun onClickMomoPay(isMomoPaySelected: Boolean) {}
        override fun onClickPayTabs(bool: Boolean) {}
        override fun onCheckCash(isCheck: Boolean) {}
        override fun onClickBackToHome() {}
        override fun onClickAddNewCredit() {}
        override fun onClickExpandDebitCards() {}
        override fun onClickDismissAddCredit() {}
        override fun onClickNotification() {}
        override fun onDismissWalletDailog() {}
        override fun onDismissMomoPayDailog() {}
        override fun onDismissPayTabsView() {}
        override fun onChosseWalletAsPayment() {}
        override fun onWalletBalanceTextChange(amount: String) {}
        override fun onMomoPayAccountTextChange(text: String) {}
        override fun paytabsInitiate(order_id: Int) {}
        override fun paytabsCheckStatus(tran_ref: String?) {}
//        override fun paytabsReturn(tran_ref: String?) {}
//        override fun paytabsCallBack(tran_ref: String, cart_id: String) {}
    }
    DhaibanTheme {
        PaymentScreenContent(state = PaymentScreenUiState(), listener = dummyListener, model = null)
    }
}