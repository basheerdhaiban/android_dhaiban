package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun DhaibanSearchBar(
    modifier: Modifier = Modifier,
    value: String,
    hint: String,
    shape: RoundedCornerShape = RoundedCornerShape(16.dp),
    enabled: Boolean = true,
    colors : TextFieldColors = TextFieldDefaults.colors(
        focusedIndicatorColor = Theme.colors.primary,
        unfocusedIndicatorColor = Theme.colors.primary100,
        focusedContainerColor = Theme.colors.background,
        unfocusedContainerColor = Theme.colors.background,
        cursorColor = Theme.colors.primary,
        focusedTextColor = Theme.colors.black87,
        unfocusedTextColor = Theme.colors.black87
    ),
    onQueryChanged: (String) -> Unit,
) {

    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp),
        value = value,
        singleLine =true,
        onValueChange = onQueryChanged,
        enabled = enabled,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.search_icon),
                contentDescription = "search icon",
                tint = LocalContentColor.current.copy(alpha = 0.5f)
            )
        },
        placeholder = {
            Text(hint, style = Theme.typography.body, color = Theme.colors.black38)
        },
        shape = shape,
        colors = colors,
        textStyle = Theme.typography.body
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewDhaibanSearchBar() {
    DhaibanTheme {
        var value by remember { mutableStateOf("") }
        DhaibanSearchBar(value = value, hint = "search", onQueryChanged = { value = it })
    }
}