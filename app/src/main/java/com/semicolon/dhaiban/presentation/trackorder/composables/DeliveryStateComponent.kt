package com.semicolon.dhaiban.presentation.trackorder.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.presentation.orders.DeliveryStatus

@Composable
fun DeliveryStateComponent(modifier: Modifier = Modifier, state: DeliveryStatus) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        DeliveryStateItem(
            title = Theme.strings.pending,
            color = Theme.colors.mediumBrown,
            icon = R.drawable.pending_icon
        )
        DashedComponent()
        DeliveryStateItem(
            title = Theme.strings.underReview,
            color = if (state.ordinal >= 1) Theme.colors.mediumBrown else Theme.colors.silverGray,
            icon = R.drawable.under_review_icon
        )
        if (state.ordinal >= 1) {
            DashedComponent()
        }
        DeliveryStateItem(
            title = Theme.strings.readyForShipping,
            color = if (state.ordinal >= 2) Theme.colors.mediumBrown else Theme.colors.silverGray,
            icon = R.drawable.ready_for_shipping_icon
        )
        if (state.ordinal >= 2) {
            DashedComponent()
        }
        DeliveryStateItem(
            title = "Under Delivery",
            color = if (state.ordinal >= 3) Theme.colors.mediumBrown else Theme.colors.silverGray,
            icon = R.drawable.under_delivery_icon
        ) // todo move to strings file
        if (state.ordinal >= 3) {
            DashedComponent()
        }

        DeliveryStateItem(
            title = Theme.strings.recieved,
            color = if (state.ordinal >= 4) Theme.colors.mediumBrown else Theme.colors.silverGray,
            icon = R.drawable.received_icon
        )
    }
}


@Composable
private fun DeliveryStateItem(
    color: Color = Theme.colors.silverGray,
    icon: Int = R.drawable.icon_language,
    title: String = "Pending",
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.height(48.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = "Delivery Icon"
                )
            }
        }

        Text(
            modifier = Modifier.width(60.dp),
            text = title,
            style = Theme.typography.caption.copy(fontSize = 10.5.sp),
            color = color,
            textAlign = TextAlign.Center,
            maxLines = 3
        )
    }
}

@Composable
private fun DashedComponent() {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(2) {
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Theme.colors.mediumBrown)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DeliveryStateComponentPreview() {
    DhaibanTheme {
        DeliveryStateComponent(state = DeliveryStatus.DELIVERED)
    }
}

@Preview(showBackground = true)
@Composable
fun DeliveryStateItemPreview() {
    DhaibanTheme {
        DeliveryStateItem()
    }
}