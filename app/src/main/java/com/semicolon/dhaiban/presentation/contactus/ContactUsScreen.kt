package com.semicolon.dhaiban.presentation.contactus

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.customer_service.CustomerServiceScreen
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants.CONTACT_US_SCREEN
import kotlinx.coroutines.flow.collectLatest

class ContactUsScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<ContactUsScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val navigator = LocalNavigator.currentOrThrow
        val context = LocalContext.current
        val state: ContactUsUiState by screenModel.state.collectAsState()
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(CONTACT_US_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    ContactUsUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    ContactUsUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                    ContactUsUiEffect.OnNavigateToCustomerScreen -> navigator.push(
                        CustomerServiceScreen()
                    )
                }
            }
        }

        ContactUsScreenContent(listener = screenModel, context = context, state = state)
    }
}

@Composable
private fun ContactUsScreenContent(
    listener: ContactUsScreenInteractionListener,
    context: Context,
    state: ContactUsUiState
) {
    Column {
        AppBarWithIcon(
            countOFunReadNotifcation=state.countOfUnreadMessage,
            title = Theme.strings.contactUs,
            onClickUpButton = listener::onClickUpButton,
            onClickNotification = listener::onClickNotification
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ContactUsItem(
                icon = R.drawable.customer_service_icon,
                title = Theme.strings.customerService
            ) {
                listener.onClickcustomerService()
            }
            ContactUsItem(icon = R.drawable.website_icon, title = Theme.strings.website) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://dhaibantrading.com")
                )
                context.startActivity(intent)
            }
            ContactUsItem(icon = R.drawable.facebook_icon, title = Theme.strings.facebook) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(state.social.facebook)
                )
                context.startActivity(intent)
            }
            ContactUsItem(icon = R.drawable.whatsapp_icon, title = Theme.strings.whatsapp) {
                openWhatsApp(context,state.social.whats_app)

            }
            ContactUsItem(icon = R.drawable.twitter_icon, title = Theme.strings.twitter) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(state.social.twitter)
                )
                context.startActivity(intent)
            }
            ContactUsItem(icon = R.drawable.instagram_icon, title = Theme.strings.instagram) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(state.social.instgram)
                )
                context.startActivity(intent)
            }
            ContactUsItem(
                icon = R.drawable.snapchat, title = Theme.strings.snapChat

//            Theme.strings.snapChat
            ) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(state.social.snap_chat)
                )
                context.startActivity(intent)
            }
            ContactUsItem(
                icon = R.drawable.tiktok,
                title = Theme.strings.tiktok

            ) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(state.social.tik_tok)
                )
                context.startActivity(intent)
            }
            ContactUsItem(icon = R.drawable.youtube, title = Theme.strings.youTube) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(state.social.you_tube)
                )
                context.startActivity(intent)
            }
            ContactUsItem(icon = R.drawable.wechat, title = Theme.strings.weChat) {
                openWeChatAccount(context, state.social.we_chat)

            }
        }
    }
}
fun isWeChatInstalled(context: Context): Boolean {
    return try {
        context.packageManager.getPackageInfo("com.tencent.mm", 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
fun isWhatsAppInstalled(context: Context): Boolean {
    return try {
        context.packageManager.getPackageInfo("com.whatsapp", 0)
        true
    } catch (e: PackageManager.NameNotFoundException) {
        false
    }
}
fun openWhatsApp(context: Context, phoneNumber: String) {
    val formattedNumber = phoneNumber.replace("+", "") // Remove "+" if present, as WhatsApp expects country code without "+"

        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/$formattedNumber") // WhatsApp link format
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "WhatsApp not found.", Toast.LENGTH_SHORT).show()
        }

}
fun openWeChatAccount(context: Context, weChatLink: String) {
Log.d("wechassst",weChatLink)
        // WeChat is installed, open in WeChat app
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(weChatLink)
                `package` = "com.tencent.mm"
            }
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Failed to open WeChat.", Toast.LENGTH_SHORT).show()
        }

}
@Composable
private fun ContactUsItem(icon: Int, title: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = 8.dp)
            .background(Theme.colors.white)
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(painter = painterResource(id = icon), contentDescription = "ContactUsIcon")
            Text(text = title, color = Theme.colors.black, style = Theme.typography.body)
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun ContactUsScreenPreview() {
//    val dummyListener = object : ContactUsScreenInteractionListener {
//        override fun onClickUpButton() {}
//        override fun onClickNotification() {}
//    }
//    DhaibanTheme {
//
//        ContactUsScreenContent(listener = dummyListener, LocalContext.current)
//    }
//}