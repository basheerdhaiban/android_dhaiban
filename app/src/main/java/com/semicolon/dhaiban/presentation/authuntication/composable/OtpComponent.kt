package com.semicolon.dhaiban.presentation.authuntication.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun OtpComponent(
    modifier: Modifier = Modifier,
    errorMessage: String = "",
) {
    var firstOtpValue by remember { mutableStateOf("") }
    var secondOtpValue by remember { mutableStateOf("") }
    var thirdOtpValue by remember { mutableStateOf("") }
    var fourthOtpValue by remember { mutableStateOf("") }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(
                8.dp,
                alignment = Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OtpTextField(
                value = firstOtpValue,
                onValueChanged = {
                    firstOtpValue = it
                    if (it.length == 1) {
                        focusRequester2.requestFocus()
                    }
                },
                modifier = Modifier.focusRequester(focusRequester1),
                errorMessage = errorMessage
            )
            OtpTextField(
                value = secondOtpValue,
                onValueChanged = {
                    secondOtpValue = it
                    if (it.length == 1) {
                        focusRequester3.requestFocus()
                    }
                },
                modifier = Modifier.focusRequester(focusRequester2),
                errorMessage = errorMessage
            )
            OtpTextField(
                value = thirdOtpValue,
                onValueChanged = {
                    thirdOtpValue = it
                    if (it.length == 1) {
                        focusRequester4.requestFocus()
                    }
                },
                modifier = Modifier.focusRequester(focusRequester3),
                errorMessage = errorMessage
            )
            OtpTextField(
                value = fourthOtpValue,
                onValueChanged = {
                    fourthOtpValue = it
                },
                modifier = Modifier.focusRequester(focusRequester4),
                errorMessage = errorMessage
            )
        }
        AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
            Text(text = errorMessage, style = Theme.typography.caption, color = Theme.colors.error)
        }
    }

}

@Composable
fun OtpTextField(
    value: String,
    modifier: Modifier = Modifier,
    errorMessage: String = "",
    onValueChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier.size(56.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            modifier = modifier
                .wrapContentSize()
                .align(Alignment.CenterHorizontally),
            value = value.take(1),
            onValueChange = onValueChanged,
            shape = RoundedCornerShape(16.dp),
            textStyle = Theme.typography.titleLarge.copy(
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            ),
            isError = errorMessage.isNotEmpty(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Theme.colors.container,
                unfocusedContainerColor = Theme.colors.container,
                focusedIndicatorColor = Theme.colors.primary,
                cursorColor = Theme.colors.primary,
                unfocusedIndicatorColor = Color.Transparent,
                focusedTextColor = Theme.colors.black87,
                unfocusedTextColor = Theme.colors.black60,
                errorIndicatorColor = Theme.colors.error,
                errorContainerColor = Theme.colors.container,
                errorTextColor = Theme.colors.error
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewOtpTextField() {
    DhaibanTheme {
        OtpComponent()
    }
}