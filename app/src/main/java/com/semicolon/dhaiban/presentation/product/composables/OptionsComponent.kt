package com.semicolon.dhaiban.presentation.product.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun OptionComponent(
    modifier: Modifier = Modifier,
    onShareClick: () -> Unit,
    onOptionClick: (OptionType) -> Unit,
    ) {
    var selectedButton by remember { mutableStateOf(OptionType.DESCRIPTION) }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { index ->
            Button(
                onClick = {
                    selectedButton = when (index) {
                        0 -> OptionType.DESCRIPTION
                        1 -> OptionType.REVIEWS
                        else -> OptionType.PROPERTIES
                    }
                    onOptionClick(selectedButton)
                }, modifier = Modifier
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Theme.colors.buttonTextGray,
                    containerColor = if (selectedButton.index == index) Theme.colors.primary
                    else Theme.colors.optionWhite
                ), shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text(
                    text = when (index) {
                        0 -> Theme.strings.description
                        1 -> Theme.strings.reviews
                        else -> Theme.strings.properties
                    },
                    color = if (selectedButton.index == index) Theme.colors.white
                    else Theme.colors.black
                )
            }
        }
//        Image(
//            modifier = Modifier
//                .padding(8.dp)
//                .noRippleEffect {
//                    onShareClick()
//                },
//            painter = painterResource(id = R.drawable.share_icon),
//            contentDescription = "FavoriteIcon"
//        )
    }

}

enum class OptionType(val index: Int) {
    DESCRIPTION(0), REVIEWS(1), PROPERTIES(2)
}