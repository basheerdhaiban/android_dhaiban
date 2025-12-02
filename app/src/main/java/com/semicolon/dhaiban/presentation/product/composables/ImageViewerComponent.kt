package com.semicolon.dhaiban.presentation.product.composables

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.product.PhotoUiState

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ImageViewerComponent(imageId: Int, images: List<PhotoUiState>, onClickBack: ()-> Unit) {
    val pagerState = rememberPagerState { images.size }

    LaunchedEffect(pagerState) {
        var imageIndex = 0
        images.forEachIndexed { index, photoUiState ->
            if (photoUiState.id == imageId)
                imageIndex = index
        }
        pagerState.animateScrollToPage(imageIndex)
    }

    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .background(Theme.colors.black)
    ) { page ->
        val currentImage = images[page]
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Icon(
                modifier = Modifier
                    .padding(32.dp)
                    .align(Alignment.TopStart)
                    .scale(
                        scaleX = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f,
                        scaleY = 1f
                    )
                    .noRippleEffect { onClickBack() },
                painter = painterResource(id = R.drawable.bar_back_icon),
                contentDescription = "navigation back icon",
                tint = Theme.colors.white
            )
            CoilImage(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.65f)
                    .align(Alignment.Center),
                url = currentImage.imageUrl,
                contentDescription = "Product Image",
                scaleType = ContentScale.FillWidth,
                zoomable = true,
            )
        }
    }
}