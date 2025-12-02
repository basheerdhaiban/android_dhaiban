package com.semicolon.dhaiban.presentation.home.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.presentation.home.ProductUiState

@Composable
fun CustomDropdownMenu(
    options: List<ProductUiState>,
    shape: Shape = RoundedCornerShape(16.dp),
    onOptionSelected: (Int) -> Unit,
) {

    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            AnimatedVisibility(visible = options.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = shape
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(
                                if (options.size > 2) 200.dp else if (options.size == 1) 60.dp else 120.dp
                            )
                            .verticalScroll(rememberScrollState())
                    ) {
                        options.forEach { option ->
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp)
                                        .background(Theme.colors.splashBackground)
                                        .clickable {
                                            onOptionSelected(option.id)
                                        }, contentAlignment = Alignment.Center
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(start = 16.dp),
                                            text = option.title
                                        )
                                        CoilImage(
                                            modifier = Modifier
                                                .padding(end = 16.dp)
                                                .size(50.dp),
                                            url = option.imageUrl,
                                            contentDescription = "Product Image",
                                            shape = CircleShape,
                                            scaleType = ContentScale.FillBounds
                                        )
                                    }
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(Theme.colors.silver.copy(alpha = 0.5f))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

