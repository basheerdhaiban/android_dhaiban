package com.semicolon.dhaiban.presentation.onBoarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.presentation.welcome.WelcomeScreen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class OnBoardingScreen : Screen {

    @Composable
    override fun Content() {
        val screenModel = getScreenModel<OnBoardingScreenModel>()
        val state: OnBoardingUiState by screenModel.state.collectAsState()
        val navigator = LocalNavigator.currentOrThrow

        LaunchedEffect(Unit) {
            screenModel.effect.collectLatest { effect ->
                when (effect) {
                    OnBoardingUiEffect.OnNavigateToWelcomeScreen -> navigator.replace(WelcomeScreen())
                }
            }
        }

        OnBoardingScreenContent(state = state, listener = screenModel)
    }


    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun OnBoardingScreenContent(state: OnBoardingUiState, listener: OnBoardingInteractionListener) {

        val scrollState = rememberScrollState()
        val pagerState =
            rememberPagerState(initialPage = 0, pageCount = { state.onBoardingData.size })
        val coroutineScope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(Theme.colors.splashBackground)
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(64.dp, Alignment.CenterVertically)
        ) {

            HorizontalPager(
                modifier = Modifier.fillMaxHeight(0.5f),
                state = pagerState
            ) { position ->
                PagerContent(onBoardingData = state.onBoardingData[position])
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {

                PagerIndicator(
                    pageCounts = state.onBoardingData.size,
                    currentPage = pagerState.currentPage
                )

                PrimaryButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 32.dp),
                    text = Theme.strings.next.lowercase(),
                    onClick = {
                        if (pagerState.currentPage == 2) {
                            listener.onClickNextButton()
                        } else {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(page = pagerState.currentPage + 1)
                            }
                        }
                    }
                )
            }
        }
    }

    @Composable
    private fun PagerContent(onBoardingData: OnBoardingContentUiState) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(64.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                painter = painterResource(id = onBoardingData.image),
                contentDescription = "Pager Image"
            )
            Text(
                text = onBoardingData.description,
                style = Theme.typography.headline,
                textAlign = TextAlign.Center
            )
        }
    }

    @Composable
    private fun PagerIndicator(pageCounts: Int, currentPage: Int, modifier: Modifier = Modifier) {
        Row(
            modifier = modifier.wrapContentHeight(),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pageCounts) { iteration ->

                val isCurrentPage = currentPage == iteration

                val size by animateDpAsState(
                    targetValue = if (isCurrentPage) 36.dp else 8.dp,
                    label = "indicator size animation",
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
                )

                val shape = if (isCurrentPage) RoundedCornerShape(Theme.radius.large)
                else CircleShape

                val brush = Brush.horizontalGradient(
                    listOf(
                        Theme.colors.primary.copy(0.2f),
                        Theme.colors.primary,
                    )
                )

                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(shape)
                        .drawBehind {
                            if (currentPage == iteration)
                                drawRect(brush)
                        }
                        .background(Theme.colors.primary.copy(alpha = 0.5f))
                        .size(height = 8.dp, width = size)
                )
            }
        }
    }

    @Composable
    @Preview(showBackground = true)
    fun PreviewOnBoardingScreen() {
        val dummyListener = object : OnBoardingInteractionListener {
            override fun onClickNextButton() {}
        }
        DhaibanTheme {
            OnBoardingScreen().OnBoardingScreenContent(
                state = OnBoardingUiState(),
                listener = dummyListener
            )
        }
    }
}