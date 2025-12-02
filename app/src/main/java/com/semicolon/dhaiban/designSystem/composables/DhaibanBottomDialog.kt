package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.window.DialogProperties
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhaibanBottomDialog(
    title: String,
    image: Int? = null,
    positiveText: String,
    negativeText: String,
    positiveButtonColor: Color = Theme.colors.mediumBrown,
    negativeButtonColor: Color = Theme.colors.black,
    onDismiss: () -> Unit,
    onPositive: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Box {}
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 16.dp))
                    .fillMaxWidth()
                    .fillMaxHeight(if (image == null) 0.30f else 0.60f)
                    .background(Theme.colors.white),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    image?.let {
                        Image(painter = painterResource(id = it), contentDescription = "Image")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(modifier = Modifier.padding(start = 6.dp, end = 6.dp),
                        text = title,
                        color = Theme.colors.black,
                        style = Theme.typography.title, textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = { onPositive() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = positiveButtonColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(
                            text = positiveText,
                            color = Theme.colors.white,
                            style = Theme.typography.title
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = negativeButtonColor
                        ),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(vertical = 12.dp)
                    ) {
                        Text(
                            text = negativeText,
                            color = Theme.colors.white,
                            style = Theme.typography.title
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                }
            }
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun BottomDialogPreview() {
    DhaibanTheme {
        DhaibanBottomDialog(
            title = "Are You Sure",
            positiveText = "Log out",
            negativeText = "Back",
            positiveButtonColor = Theme.colors.mediumBrown,
            negativeButtonColor = Theme.colors.black,
            onDismiss = {},
            onPositive = {}
        )
    }
}