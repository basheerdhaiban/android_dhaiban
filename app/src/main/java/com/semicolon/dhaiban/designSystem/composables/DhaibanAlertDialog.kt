package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DhaibanAlertDialog(
    title: String,
    body: @Composable () -> Unit,
    positiveText: String,
    negativeText: String,
    onDismiss: () -> Unit,
    onPositive: () -> Unit
) {
    BasicAlertDialog(onDismissRequest = { onDismiss() }) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(Theme.colors.white),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        textAlign = TextAlign.Center,
                        color = Theme.colors.black,
                        style = Theme.typography.titleLarge
                    )
                }
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(text = "Thank You")
//                    Image(
//                        painter = painterResource(id = R.drawable.authorization_image),
//                        contentDescription = ""
//                    )
//                    Text(text = "Order No: 654564563")
//                }
                body()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPositive() },
                        colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.mediumBrown),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = positiveText,
                            color = Theme.colors.white,
                            style = Theme.typography.body
                        )
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = negativeText,
                            color = Theme.colors.white,
                            style = Theme.typography.body
                        )
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecondDhaibanAlertDialog(
    title: String,
    body: @Composable () -> Unit,
    positiveText: String,
    negativeText: String,
    onDismiss: () -> Unit,
    onPositive: () -> Unit
) {
    AlertDialog(onDismissRequest = { onDismiss() }) {
        Surface(
            color = Color.Gray.copy(alpha = 0.8f) // Semi-transparent background
        ) {
            // Your dialog content here

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .fillMaxWidth()
                .background(Theme.colors.white),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = title,
                        color = Theme.colors.black,
                        style = Theme.typography.titleLarge
                    )
                }
//                Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(text = "Thank You")
//                    Image(
//                        painter = painterResource(id = R.drawable.authorization_image),
//                        contentDescription = ""
//                    )
//                    Text(text = "Order No: 654564563")
//                }
                body()
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onPositive() },
                        colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.mediumBrown),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = positiveText,
                            color = Theme.colors.white,
                            style = Theme.typography.body
                        )
                    }
                    Button(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = { onDismiss() },
                        colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.black),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = negativeText,
                            color = Theme.colors.white,
                            style = Theme.typography.body
                        )
                    }
                }
            }
        }
    }}
}
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun DhaibanAlertDialogPreview() {
    DhaibanTheme {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            DhaibanAlertDialog(
                title = "Bla Bla",
                body = {},
                positiveText = "",
                negativeText = "",
                onDismiss = {}) {

            }
        }

    }
}