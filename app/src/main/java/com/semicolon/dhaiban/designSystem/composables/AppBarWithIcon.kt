package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect

@Composable
fun AppBarWithIcon(
    countOFunReadNotifcation: Int = 0,
    title: String,
    withNotification: Boolean = true,

    modifier: Modifier=Modifier ,
    onClickUpButton: () -> Unit,
    onClickNotification: () -> Unit = {}
) {
    Card(modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
        shape = RoundedCornerShape(bottomEnd = 8.dp, bottomStart = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 16.dp, end = 16.dp)
//                .padding(horizontal = 30.dp, vertical = 16.dp),
           , horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .scale(
                            scaleX = if (LocalLayoutDirection.current == LayoutDirection.Ltr) 1f else -1f,
                            scaleY = 1f
                        )
                        .noRippleEffect {
                            onClickUpButton()
                        },
                    painter = painterResource(id = R.drawable.bar_back_icon),
                    contentDescription = "navigation back icon"
                )
                Text(text = title, style = Theme.typography.headline)
            }
            if (withNotification)
                BadgedBox(
                    badge = {
                        if (countOFunReadNotifcation != 0) {
                            Icon(painter = painterResource(id = R.drawable.dot),contentDescription = null , tint = Theme.colors.mediumBrownTwo , modifier = Modifier.align(
                                Alignment.TopStart))
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier.noRippleEffect { onClickNotification() },
                        painter =painterResource(id = R.drawable.notifcation),
                        tint = Theme.colors.mediumBrown,

                        contentDescription = null
                    )
                }


        }
    }
}