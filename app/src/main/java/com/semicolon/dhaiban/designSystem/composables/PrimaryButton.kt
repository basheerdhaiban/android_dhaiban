package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun PrimaryButton(
    text: String?,
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    isLoading: Boolean = false,
    colors: ButtonColors = ButtonDefaults.buttonColors(containerColor = Theme.colors.primary),
    borderColor: Color = Color.Transparent,
    borderWidth: Dp = 0.dp,
    onClick: () -> Unit,
) {

    Button(
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(Theme.radius.medium),
        onClick = onClick,
        colors = colors,
        border = BorderStroke(width = borderWidth, color = borderColor),
        enabled = !isLoading
    ) {


        AnimatedContent(targetState = isLoading, label = "") {
            if (it) DhaibanThreeDotLoadingIndicator()
            else {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    iconRes?.let {
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .align(Alignment.CenterVertically),
                            imageVector = ImageVector.vectorResource(iconRes),
                            contentDescription = "button icon"
                        )
                    }

                    if (!text.isNullOrEmpty()) {
                        Text(
                            modifier = Modifier.padding(vertical = 4.dp),
                            text = text,
                            style = Theme.typography.titleLarge
                        )
                    }

                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewPrimaryButton() {
    DhaibanTheme {
        PrimaryButton(modifier = Modifier.fillMaxWidth(), text = "Login") {
        }
    }

}