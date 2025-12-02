package com.semicolon.dhaiban.presentation.authuntication.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect

@Composable
fun AuthContainer(
    modifier: Modifier = Modifier,
    logo: Int,
    headerText: String,
    actionText: String,
    actionDescription: String,
    onClickAction: () -> Unit,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Image(
            modifier = Modifier.size(160.dp),
            painter = painterResource(id = logo),
            contentDescription = "logo"
        )

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = headerText,
                color = Theme.colors.black87,
                style = Theme.typography.headline
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = actionDescription,
                    color = Theme.colors.black60,
                    style = Theme.typography.body,
                )

                Text(
                    modifier = Modifier.noRippleEffect { onClickAction() },
                    text = actionText,
                    color = Theme.colors.primary,
                    style = Theme.typography.title
                )
            }
        }

        content()

    }
}


@Preview(showBackground = true)
@Composable
fun AuthContainerPreview() {
    DhaibanTheme {
        AuthContainer(
            logo = R.drawable.logo,
            onClickAction = {},
            headerText = "Login",
            actionText = "SignUp",
            actionDescription = "Don't have an account ?"
        ) {

        }
    }

}