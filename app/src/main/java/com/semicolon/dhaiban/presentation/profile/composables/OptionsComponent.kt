package com.semicolon.dhaiban.presentation.profile.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect

@Composable
fun OptionsComponent(
    modifier: Modifier = Modifier,
    onOrdersClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onWalletClick: () -> Unit,
    onRefundClick: () -> Unit
) {
    Box(
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        Theme.colors.splashBackground,
                        Theme.colors.toupe,
                        Theme.colors.toupeTwo
                    ),
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
//            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OptionItem(
                optionName = Theme.strings.orders,
                icon = R.drawable.orders_icon
            ) { onOrdersClick() }
            OptionItem(
                optionName = Theme.strings.favourites,
                icon = R.drawable.favorite_icon
            ) { onFavoritesClick() }
            OptionItem(
                optionName = Theme.strings.wallet,
                icon = R.drawable.wallat_in_profile
            ) { onWalletClick() }
            OptionItem(
                optionName = Theme.strings.refund,
                icon = R.drawable.refund_icon
            ) { onRefundClick() }
        }
    }

}

@Composable
fun OptionItem(optionName: String, icon: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier.noRippleEffect { onClick() },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = "Option Button"
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = optionName, color = Theme.colors.black, fontWeight = FontWeight(500))
    }
}

@Preview
@Composable
fun OptionsComponentPreview() {
    DhaibanTheme {
        OptionsComponent(Modifier, {}, {}, {}, {})
    }
}

