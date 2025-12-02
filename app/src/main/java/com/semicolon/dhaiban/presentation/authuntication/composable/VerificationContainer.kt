package com.semicolon.dhaiban.presentation.authuntication.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon

@Composable
fun VerificationContainer(
    title: String,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClickBackIcon: () -> Unit,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppBarWithIcon(
            title = title,
            withNotification = false,
            onClickUpButton = { onClickBackIcon() }
        )

        Image(
            modifier = Modifier.size(140.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo"
        )

        description?.let {
            Text(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = description,
                style = Theme.typography.title,
                color = Theme.colors.description,
                textAlign = TextAlign.Center

            )
        }

        content()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewVerificationContent() {
    DhaibanTheme {
        VerificationContainer(
            title = "Forget Password ?",
            description = "Please enter your phone number to\n" +
                    "receive a verification code",
            onClickBackIcon = {},
            content = {}
        )
    }
}