package com.semicolon.dhaiban.presentation.faqdetails

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
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
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.designSystem.composables.modifier.shimmerEffect
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.notification.NotificationScreen
import com.semicolon.dhaiban.presentation.utils.Constants.FAQ_DETAILS_SCREEN
import kotlinx.coroutines.flow.collectLatest
import org.koin.core.parameter.parametersOf

class FaqDetailsScreen(private val faqId: Int) : Screen {
    @Composable
    override fun Content() {
        val screenModel =
            getScreenModel<FaqDetailsScreenModel>(parameters = { parametersOf(faqId) })
        val appScreenModel = getScreenModel<AppScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow
        LaunchedEffect(Unit) {
            appScreenModel.setCurrentScreen(FAQ_DETAILS_SCREEN)
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    FaqDetailsUiEffect.OnNavigateBack -> if (navigator.canPop) navigator.pop()
                    FaqDetailsUiEffect.OnNavigateToNotificationScreen -> navigator.push(
                        NotificationScreen()
                    )
                }
            }
        }

        FaqDetailsScreenContent(state = state, listener = screenModel)
    }
}

@Composable
private fun FaqDetailsScreenContent(
    state: FaqDetailsUiState,
    listener: FaqDetailsInteractionListener,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = state.faqDetails.isEmpty() and state.isLoading.not()) {
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
            AnimatedContent(targetState = state.isLoading, label = "") {
                if (it) {
                    LoadingContent()
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .safeDrawingPadding()
                            .verticalScroll(rememberScrollState())
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        state.faqDetails.forEach { faqDetails ->
                            AnimatedVisibility(visible = state.faqDetails.isNotEmpty()) {
                                FaqItem(question = faqDetails.question, answer = faqDetails.answer)
                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
private fun FaqItem(question: String, answer: String) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f,
        label = ""
    )
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .shadow(elevation = 8.dp)
            .background(Theme.colors.white)
            .padding(16.dp)
            .animateContentSize(
                animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
            )
            .noRippleEffect { expandedState = !expandedState }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.question_icon),
                contentDescription = "Question Icon"
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = question,
                modifier = Modifier.weight(0.9f),
                style = Theme.typography.body,
            )
            Image(
                modifier = Modifier
                    .weight(0.1f)
                    .rotate(rotationState)
                    .noRippleEffect { expandedState = !expandedState },
                painter = painterResource(id = R.drawable.down_arrow),
                contentDescription = "Drop Down Arrow"
            )
        }
        if (expandedState) {
            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Theme.colors.silver.copy(alpha = 0.5f))
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = answer, color = Theme.colors.greyishTwo, style = Theme.typography.caption)
        }
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
private fun FaqDetailsContentPreview() {
    LoadingContent()
}

@Composable
@Preview(showBackground = true)
fun FaqItemPreview() {
    DhaibanTheme {
        FaqItem(question = "Shipping", answer = "bla bla bla bla")
    }
}