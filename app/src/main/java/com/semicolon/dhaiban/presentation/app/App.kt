package com.semicolon.dhaiban.presentation.app

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.core.screen.uniqueScreenKey
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.DhaibanSnackBar
import com.semicolon.dhaiban.presentation.configuration.ConfigurationScreen
import com.semicolon.dhaiban.presentation.home.container.MainContainer
import com.semicolon.dhaiban.presentation.welcome.WelcomeScreen
class MainApp(val typeofnotifcation: String = "", val inboxID: Int = Int.MIN_VALUE) : Screen {
    override val key: ScreenKey = uniqueScreenKey

    @Composable
    override fun Content() {
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state by appScreenModel.state.collectAsState()
        val isNavigationReady = !state.isLoading && state.isActive != null && state.isOnboardingCompleted != null

        Log.d("dassssss","${state.secondStringRes.thereIsNoProduct}")
        Log.d("dassssssaaa","${state.stringRes.home}")
        Log.d("dassssssaaaassaaa","${state.isActive}")
        DhaibanTheme(
            stringResources = state.stringRes,
            SecondStringResources = state.secondStringRes,
            layoutDirection = state.layoutDirection,
        ) {
            if (isNavigationReady) {
                val initialScreen = when (typeofnotifcation) {
                    "chat", "order", "refund" -> MainContainer(typeofnotifcation, inboxID)
                    else -> {
                        if (state.isOnboardingCompleted == true) {
                            if (state.isActive == true) MainContainer() else WelcomeScreen()
                        } else {
                            ConfigurationScreen()
                        }
                    }
                }

                Navigator(initialScreen) { SlideTransition(it) }
            }
            DhaibanSnackBar(
                icon = R.drawable.icon_language,
                iconBackgroundColor = Color.Transparent,
                iconTint = Theme.colors.white,
                isVisible = state.showSnackBar,
                modifier = Modifier
                    .fillMaxWidth()
                    .safeDrawingPadding()
                    .padding(bottom = 65.dp)
            ) {
                Text(
                    text = "Network error",
                    style = Theme.typography.body,
                    color = Theme.colors.white
                )
            }
        }
    }
}

//class MainApp (val typeofnotifcation :String ="",val inboxID:Int=Int.MIN_VALUE) : Screen {
//    override val key: ScreenKey = uniqueScreenKey
//
//    @Composable
//    override fun Content() {
//        val appScreenModel = getScreenModel<AppScreenModel>()
//
//        val state by appScreenModel.state.collectAsState()
//        DhaibanTheme(
//
//            stringResources = state.stringRes,
//            SecondStringResources =state.secondStringRes,
//            layoutDirection = state.layoutDirection,
//
//        ) {
//            Log.d("DhaibanTheme",state.stringRes.Be_the_first_to_start_a_conversation)
//            if (!state.isLoading) {
//                if (typeofnotifcation=="chat"){
//                    Navigator( ChatScreen(inboxID = inboxID, isComeFromNotification = true) ) {
//                        SlideTransition(it)
//                    }
////                    navigator.replaceAll(ChatScreen(inboxID = inboxID, isComeFromNotification = true))
//
//                }
//                else if (typeofnotifcation=="order") {
//                    Navigator( TrackOrderScreen(orderID = inboxID, isComeFromNotification = true , orderType = OrderType.CURRENT)) {
//                        SlideTransition(it)
//                    }
//
//
//                }
//                else if (typeofnotifcation=="refund") {
//                    Navigator( TrackRefundScreen(refundID = inboxID, isComeFromNotification = true , requestType = RequestType.CURRENT)) {
//                        SlideTransition(it)
//                    } }
//                else{
//                    Log.d("isactiveApp",state.isActive.toString())
//
//                    state.isOnboardingCompleted?.let { isCompleted ->
//                        Navigator(if (!isCompleted) ConfigurationScreen() else if (state.isActive==true) MainContainer else WelcomeScreen()) {
//                            SlideTransition(it)
//                        }
////                    if (isCompleted) {
////                        Navigator(MainContainer) { SlideTransition(it) }
////                    } else {
////                        BottomSheetNavigator(
////                            sheetShape = RoundedCornerShape(topEnd = 24.dp, topStart = 24.dp),
////                        ) {
////                            Navigator(ConfigurationScreen()) { SlideTransition(it) }
////                        }
////                    }
//                    }}
//            }
////
//            DhaibanSnackBar(
//                icon = R.drawable.icon_language,
//                iconBackgroundColor = Color.Transparent,
//                iconTint = Theme.colors.white,
//                isVisible = state.showSnackBar,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .safeDrawingPadding()
//                    .padding(bottom = 65.dp)
//            ) {
//                Text(
//                    text = "Network error",
//                    style = Theme.typography.body,
//                    color = Theme.colors.white
//                )
//            }
//        }
//    }
//}