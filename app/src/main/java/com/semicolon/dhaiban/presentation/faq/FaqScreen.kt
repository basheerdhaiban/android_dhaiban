package com.semicolon.dhaiban.presentation.faq

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.semicolon.dhaiban.presentation.faqdetails.FaqDetailsScreen
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants.FAQ_SCREEN
import kotlinx.coroutines.flow.collectLatest

class FaqScreen : Screen {
    @Composable
    override fun Content() {
        val screenModel = getScreenModel<FaqScreenModel>()
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(FAQ_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    FaqScreenUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    is FaqScreenUiEffect.OnNavigateToFaqDetails -> navigator.push(
                        FaqDetailsScreen(
                            effect.faqId
                        )
                    )

                    FaqScreenUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }
        }

        FaqScreenContent(state = state, listener = screenModel)
    }
}

@Composable
private fun FaqScreenContent(
    state: FaqScreenUiState,
    listener: FaqScreenListenerInteraction,
) {
    if (state.isLoading){
        LoadingContent()
    }
else{
    Log.d("FAQsss",state.faqTypes.get(0).type)
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = state.faqTypes.isEmpty() and state.isLoading.not()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = R.drawable.image_error_icon),
                    contentDescription = ""
                )
            }
        }
        Column(Modifier.fillMaxSize()) {
            AppBarWithIcon(
                title = Theme.strings.fAQ,
                onClickUpButton = listener::onClickUpButton,
                onClickNotification = listener::onClickNotification
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.faq_image),
                        contentDescription = "FAQ Image"
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                LazyColumn(modifier = Modifier.padding(top = 8.dp),contentPadding = PaddingValues(top = 8.dp)) {
                    items(state.faqTypes){
                        FaqItem(it.id,it.type){
                            listener.onFaqClick(it)

                        }
                    }
                }

            }
        }

    }}
}

@Composable
private fun FaqItem(id: Int, type: String, onClick: (Int) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(id) }
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = 8.dp)
            .background(Theme.colors.white)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.question_icon),
                contentDescription = "ContactUsIcon"
            )
            Text(text = type, color = Theme.colors.black, style = Theme.typography.title)
            Image(
                painter = painterResource(id = R.drawable.right_arrow),
                contentDescription = "ContactUsIcon",
                colorFilter = ColorFilter.tint(
                    Color.Black
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FaqItemPreview() {
    FaqItem(1, "Shipping") {}
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FaqScreenPreview() {
    val dummyListener = object : FaqScreenListenerInteraction {
        override fun onClickUpButton() {}
        override fun onFaqClick(id: Int) {}
        override fun onClickNotification() {}
    }
    DhaibanTheme {

        FaqScreenContent(state = FaqScreenUiState(), listener = dummyListener)
    }
}

@Composable
private fun LoadingContent() {
    Column {
        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .padding(16.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerEffect()
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun EaqContentPreview() {
    LoadingContent()
}