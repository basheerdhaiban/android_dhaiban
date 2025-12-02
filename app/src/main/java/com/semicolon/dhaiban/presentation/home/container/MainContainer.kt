package com.semicolon.dhaiban.presentation.home.container

import ChatScreen
import TrackOrderScreen
import TrackRefundScreen
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.cart.CartAddressUiState
import com.semicolon.dhaiban.presentation.cart.CartScreenModel
import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.refund.RequestType
import com.semicolon.dhaiban.presentation.utils.Constants.ADDRESS_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.ADD_ADDRESS_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.BRAND_PRODUCTS_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.BRAND_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.CONTACT_US_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.FAQ_DETAILS_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.FAQ_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.FAVORITES_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.FLASH_SALE_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.MY_PROFILE_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.NOTIFICATION_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.ORDERS_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.PAYMENT_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.PRODUCT_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.REFUND_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.SUBCATEGORY_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.TRACK_ORDER_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.TRACK_REFUND_SCREEN
import com.semicolon.dhaiban.presentation.utils.Constants.WALLET_SCREEN
import com.semicolon.dhaiban.presentation.utils.SafeNavigator
import androidx.lifecycle.compose.LocalLifecycleOwner
import org.koin.core.parameter.parametersOf


fun shouldShowBottomBar(currentScreen: String): Boolean {
    val screensWithoutBottomBar = setOf(
        PRODUCT_SCREEN, SUBCATEGORY_SCREEN, FAVORITES_SCREEN,
        BRAND_SCREEN, FLASH_SALE_SCREEN, BRAND_PRODUCTS_SCREEN,
        ADDRESS_SCREEN, ADD_ADDRESS_SCREEN, MY_PROFILE_SCREEN,
        PAYMENT_SCREEN, ORDERS_SCREEN, TRACK_ORDER_SCREEN,
        CONTACT_US_SCREEN, FAQ_SCREEN, FAQ_DETAILS_SCREEN,
        REFUND_SCREEN, TRACK_REFUND_SCREEN, WALLET_SCREEN,
        NOTIFICATION_SCREEN
    )
    return currentScreen !in screensWithoutBottomBar
}

class MainContainer(val notification: String = "", val inboxId: Int=0) : Screen {

    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val lifecycleOwner = LocalLifecycleOwner.current

        Log.d("dasdasdas","$notification + $inboxId")
        
        // Handle navigation from notifications in LaunchedEffect with lifecycle safety
        LaunchedEffect(notification, inboxId) {
            when (notification) {
                "chat" -> SafeNavigator.safeReplaceAll(
                    navigator,
                    ChatScreen(
                        inboxID = inboxId,
                        isComeFromNotification = true
                    ),
                    lifecycleOwner
                )

                "order" -> SafeNavigator.safeReplaceAll(
                    navigator,
                    TrackOrderScreen(
                        orderID = inboxId,
                        isComeFromNotification = true,
                        orderType = OrderType.CURRENT
                    ),
                    lifecycleOwner
                )

                "refund" -> SafeNavigator.safeReplaceAll(
                    navigator,
                    TrackRefundScreen(
                        refundID = inboxId,
                        isComeFromNotification = true,
                        requestType = RequestType.CURRENT
                    ),
                    lifecycleOwner
                )
            }
        }
        val appScreenModel = getScreenModel<AppScreenModel>()
        val appState by appScreenModel.state.collectAsState()
        val screenModel =
            getScreenModel<CartScreenModel>(parameters = {
                parametersOf(
                    CartAddressUiState(
                        0,
                        ""
                    )
                )
            })


        TabNavigator(HomeTab) {
            screenModel.getCartProducts()
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                containerColor = Theme.colors.background,
                bottomBar = {
                    val tabNavigator = LocalTabNavigator.current
                    val tabs = rememberTabsContainer()
                    AnimatedContent(
                        targetState = shouldShowBottomBar(appState.currentScreen),
                        label = ""
                    ) { showBottomBar ->
                        if (showBottomBar) {
                            BottomBar(
                                cartCount = appState.cartItemsNumber,
                                tabs = tabs,
                                tabNavigator = tabNavigator,
                                screenModel = screenModel
                            )
                        } else {
                            BottomBar(
                                modifier = Modifier.height(0.dp),
                                navBarHeight = 0.dp,
                                tabs = emptyList(),
                                tabNavigator = tabNavigator,
                                screenModel = screenModel
                            )
                        }
                    }
                },
                content = {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(bottom = it.calculateBottomPadding())
                    ) {
                        CurrentTab()

                    }
                }
            )
        }
    }
}

@Composable
private fun BottomBar(
    modifier: Modifier = Modifier,
    cartCount: Int = 0,
    navBarHeight: Dp = 64.dp,
    tabs: List<TabContainer>,
    tabNavigator: TabNavigator,
    screenModel: CartScreenModel
) {
    val cartCounts = screenModel.countOfCart.collectAsState()
    DhaibanNavigationBar(
        modifier = modifier.background(Theme.colors.background),
        navigationBarHeight = navBarHeight
    ) {
        tabs.forEach { tabContainer ->
            val selected = tabNavigator.current == tabContainer.tab
            val drawable =
                if (selected) tabContainer.selectedIcon else tabContainer.unSelectedIcon

            DhaibanNavigationBarItem(
                icon = { color ->
                    if (tabContainer.tab == CartTab) {
                        if (cartCounts.value > 0) {
                            Log.d("BottomBar", cartCounts.value.toString())
                            Box {
                                Box(
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .align(Alignment.Center)
                                ) {
                                    Image(
                                        modifier = Modifier.align(Alignment.Center),
                                        imageVector = ImageVector.vectorResource(drawable),
                                        contentDescription = "Cart Icon",
                                        colorFilter = ColorFilter.tint(color)
                                    )
                                }
                                Badge(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    containerColor = Theme.colors.veryLightBrown,
                                    contentColor = Theme.colors.white
                                ) {
                                    Text(
                                        text = cartCounts.value.toString(),
                                        color = Theme.colors.white,
                                        style = Theme.typography.caption
                                    )
                                }
                            }
                        } else {
                            Icon(
                                imageVector = ImageVector.vectorResource(drawable),
                                contentDescription = null,
                                tint = color,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        Icon(
                            imageVector = ImageVector.vectorResource(drawable),
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                },
                label = { Text(text = tabContainer.tab.options.title, style = it) },
                selected = selected,
                onClick = { tabNavigator.current = tabContainer.tab },
            )
        }
    }
}
