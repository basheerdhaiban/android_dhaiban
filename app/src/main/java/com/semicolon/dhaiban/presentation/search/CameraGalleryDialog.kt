package com.semicolon.dhaiban.presentation.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun CameraGalleryDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Box(modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center) {

                    Text(stringResource(R.string.choose_option),
                        style = MaterialTheme.typography.titleMedium)
                }
            },
//            text = {
//                Column {
//                    Text(text = "Select source to pick image",
//                        style = Theme.typography.headline)
//                }
//            },

            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(
                        onClick = {
                            onGalleryClick()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.gallery))
                    }
                    TextButton(
                        onClick = {
                            onCameraClick()
                            onDismiss()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.camera))
                    }
                }
            },
//            dismissButton = {
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(horizontal = 16.dp),
//                ) {
//                    TextButton(
//                        onClick = {
//                            onGalleryClick()
//                            onDismiss()
//                        },
//                        modifier = Modifier.weight(1f)
//                    ) {
//                        Text(stringResource(R.string.gallery))
//                    }
//                }
//            }
        )
    }
}
