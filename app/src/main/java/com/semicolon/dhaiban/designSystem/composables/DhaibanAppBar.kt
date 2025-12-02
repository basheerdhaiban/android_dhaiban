package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhaibanAppBar(
    title: String,
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = Theme.colors.white
    ),
    backIconAction: () -> Unit,
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
        shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
    ) {
        TopAppBar(
            modifier = modifier.fillMaxWidth(),
            title = {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = title,
                    style = Theme.typography.header,
                    textAlign = TextAlign.Center
                )
            },
            navigationIcon = {
                Icon(
                    modifier = Modifier
                        .scale(
                            scaleX = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f,
                            scaleY = 1f
                        )
                        .noRippleEffect { backIconAction() },
                    painter = painterResource(id = R.drawable.bar_back_icon),
                    contentDescription = "navigation back icon"
                )
            },
            colors = colors
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun PreviewAppBar() {
    DhaibanTheme {
        DhaibanAppBar(title = "Forget Password ?") {}
    }
}