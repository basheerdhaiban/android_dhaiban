package com.semicolon.dhaiban.presentation.wallet

import androidx.compose.animation.AnimatedContent
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
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants
import kotlinx.coroutines.flow.collectLatest

class WalletScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<WalletScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state: WalletScreenUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            screenModel.updateCurrencyUiState(appScreenModel.getCurrency())

            screenModel.getCurrentBalance()

            screenModel.getWalletHistory()

            appScreenModel.setCurrentScreen(Constants.WALLET_SCREEN)

            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    WalletScreenUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    WalletScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }

        }

        WalletScreenContent(state, screenModel)
    }
}

@Composable
fun WalletScreenContent(state: WalletScreenUiState, listener: WalletScreenInteractionListener) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
    ) {
        AppBarWithIcon(
            countOFunReadNotifcation=state.countOfUnreadMessage,
            title = Theme.strings.wallet,
            onClickUpButton = listener::onClickUpButton,
            onClickNotification = listener::onClickNotification
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            AnimatedContent(targetState = state.isLoading, label = "") { loading ->
                if (loading) {
                    LoadingContent()
                } else {
                    Column {
                        BalanceComponent(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            balance = state.balance,
                            currency = state.currency
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(contentPadding = PaddingValues(vertical = 16.dp)) {
                            items(items = state.walletHistory, key = { it.id }) {
                                WalletHistoryItem(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    walletHistory = it,
                                    currency = state.currency
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BalanceComponent(
    modifier: Modifier = Modifier,
    balance: Double,
    currency: WalletCurrencyUiState
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.colors.white),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
        shape = RoundedCornerShape(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = Theme.strings.yourBalance,
                        style = Theme.typography.title,
                        color = Theme.colors.black
                    )
                    Image(
                        painter = painterResource(id = R.drawable.wallet_balance_icon),
                        contentDescription = "Wallet balance icon"
                    )
                }

                // Ensure balanceAccount is computed safely
                val balanceAccount = maxOf(0.0, balance * currency.exchangeRate)

                // Safely format the balance and append currency symbol
                Text(
                    text = "%.1f %s".format(balanceAccount, currency.symbol),
                    color = Theme.colors.black
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(0.9.dp)
                    .background(Theme.colors.silver.copy(alpha = 0.5f))
            )
        }
    }
}


@Composable
fun WalletHistoryItem(
    modifier: Modifier = Modifier,
    walletHistory: WalletHistoryUiState,
    currency: WalletCurrencyUiState
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .background(Theme.colors.transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(vertical = 16.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${Theme.strings.amount}: ",
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = String.format(
                        "%.2f",
                        walletHistory.amount * currency.exchangeRate
                    ) + " " + currency.symbol,
                    color = Theme.colors.black,
                    style = Theme.typography.caption
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                    text = walletHistory.paymentMethod,
                    color = Theme.colors.black,
                    style = Theme.typography.caption
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Absolute.SpaceBetween
            ) {
                Text(
                    text = "${Theme.strings.approval}: ",
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = when (walletHistory.approval) {
                        0 -> Theme.strings.declined
                        else -> Theme.strings.accepted
                    },
                    color = Theme.colors.black,
                    style = Theme.typography.caption
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${Theme.strings.date}: ",
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = walletHistory.createdAt,
                    color = Theme.colors.black,
                    style = Theme.typography.caption
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = Theme.strings.paymentDetails,
                    color = Theme.colors.black,
                    style = Theme.typography.body.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp
                    )
                )
                Text(
                    text = walletHistory.paymentDetails,
                    color = Theme.colors.black,
                    style = Theme.typography.caption,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect()
        )
    }
}

@Preview
@Composable
fun WalletContentPreview() {
    DhaibanTheme {
        LoadingContent()
    }
}

@Preview
@Composable
fun WalletHistoryItemPreview() {
    DhaibanTheme {
        WalletHistoryItem(
            walletHistory = WalletHistoryUiState(0.0, 0, "", 0, "", ""),
            currency = WalletCurrencyUiState()
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun WalletScreenContentScreenPreview() {
    val dummyListener = object : WalletScreenInteractionListener {
        override fun onClickUpButton() {}
        override fun onClickNotification() {}
    }
    DhaibanTheme {
        WalletScreenContent(state = WalletScreenUiState(), listener = dummyListener)
    }
}