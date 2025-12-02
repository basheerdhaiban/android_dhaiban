package com.semicolon.dhaiban.presentation.cart.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun ButtonForEmptyState(Text:String ,modifier: Modifier=Modifier ,onClick: () -> Unit) {
    Button(
        onClick = {
onClick()
        },
        modifier = modifier
            .height(40.dp)
            .width(150.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Theme.colors.white,
            containerColor = Theme.colors.mediumBrown
        ),
        shape = RoundedCornerShape(22.dp),
        contentPadding = PaddingValues(horizontal = 0.dp),
    ) {
        Text(text =Text )

    }
}

@Preview(showBackground = true)
@Composable
fun OptionItemPreview() {
    DhaibanTheme {
        Column {
            ButtonForEmptyState(Text="start") {}

        }
    }
}