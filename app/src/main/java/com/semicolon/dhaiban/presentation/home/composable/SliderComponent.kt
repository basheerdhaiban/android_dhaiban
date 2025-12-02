package com.semicolon.dhaiban.presentation.home.composable

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.presentation.home.SliderUiState
import com.semicolon.dhaiban.presentation.sharedUiState.SliderItemType

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SliderComponent(
    modifier: Modifier = Modifier,
    sliderItems: List<SliderUiState>,
    onExploreClicked: (SliderItemType, Int, String ) -> Unit
) {

    Box(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Theme.colors.black60)
    ) {
        val pagerState = rememberPagerState(initialPage = 0) { sliderItems.size }

        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState
        ) { position ->

            PagerContent(sliderItems[position])
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            PagerIndicator(pageCounts = sliderItems.size, currentPage = pagerState.currentPage)
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 8.dp),
            contentAlignment = Alignment.BottomStart
        ) {
            val currentItem = sliderItems[pagerState.currentPage]
            Button(
                onClick = { onExploreClicked(currentItem.type, currentItem.typeId, currentItem.title ) },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Theme.colors.buttonTextGray,
                    containerColor = Theme.colors.white
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                Text(text = Theme.strings.exploreMe)
            }
        }
    }
}

@Composable
fun PagerContent(
    sliderData: SliderUiState,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        CoilImage(
            modifier = Modifier
                .fillMaxSize(),
            url = sliderData.photoUrl,
            contentDescription = "Slider Image"
        )
        Box(
            modifier = Modifier
                .background(Theme.colors.black38)
                .fillMaxSize()
        )
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = sliderData.title,
                    style = Theme.typography.header,
                    color = Theme.colors.white
                )
                Text(
                    text = sliderData.description,
                    style = Theme.typography.title,
                    color = Theme.colors.white
                )
            }
        }
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
                    Theme.colors.white,
                    Theme.colors.white,
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
                    .background(Theme.colors.transparent)
                    .border(BorderStroke(0.4.dp, Theme.colors.white), CircleShape)
                    .size(height = 8.dp, width = size)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SliderComponentPreview() {
    DhaibanTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            SliderComponent(
                modifier = Modifier.fillMaxHeight(0.2f),
                sliderItems = listOf(
                    SliderUiState(1, "The Best Product", "for your best time"),
                    SliderUiState(2, "The Best Product", "for your best time"),
                    SliderUiState(3, "The Best Product", "for your best time"),
                )
            ) { _, _, _ ->

            }
        }

    }
}



