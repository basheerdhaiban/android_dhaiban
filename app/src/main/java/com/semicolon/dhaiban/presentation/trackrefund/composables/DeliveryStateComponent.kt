package com.semicolon.dhaiban.presentation.trackrefund.composables

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
import com.semicolon.dhaiban.presentation.refund.RefundStatus

@Composable
fun RefundStateComponent(modifier: Modifier = Modifier, state: RefundStatus) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        DeliveryStateItem(
            title = Theme.strings.pending,
            icon = R.drawable.pending_icon
        )
        if (state.ordinal > 0){
            DashedComponent()
        }

        if (state == RefundStatus.ACCEPTED) {
            DeliveryStateItem(
                title = Theme.strings.accepted,
                icon = R.drawable.under_review_icon
            )
//            DashedComponent()
        }
        if (state == RefundStatus.DECLINED) {
            DeliveryStateItem(
                title = Theme.strings.declined,
                icon = R.drawable.declined_icon
            )
        }
    }
}


@Composable
private fun DeliveryStateItem(
    color: Color = Theme.colors.mediumBrown,
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
        RefundStateComponent(state = RefundStatus.DECLINED)
    }
}

@Preview(showBackground = true)
@Composable
fun DeliveryStateItemPreview() {
    DhaibanTheme {
        DeliveryStateItem()
    }
}