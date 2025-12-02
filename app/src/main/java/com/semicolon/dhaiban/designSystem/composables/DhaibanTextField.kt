package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.arpitkatiyarprojects.countrypicker.CountryPickerOutlinedTextField
import com.arpitkatiyarprojects.countrypicker.models.CountryDetails
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme

@Composable
fun DhaibanTextField(
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    label: String,
    text: String,
    enabled: Boolean = true,
    leadingIcon: Int,
    trailingButton: Boolean = false,
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    shapeRadius: Shape = RoundedCornerShape(Theme.radius.large),
    singleLine: Boolean = true,
    errorMessage: String = "",
    isError: Boolean = errorMessage.isNotEmpty(),
    onValueChange: (String) -> Unit,
    onButtonClick: () -> Unit = {},
) {

    var showPassword by remember { mutableStateOf(false) }

    var isTextFieldFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue =
        if (isTextFieldFocused && !isError) {
            Theme.colors.primary
        } else if (isError) {
            Theme.colors.error
        } else {
            Color.Transparent
        }, label = ""
    )

    val focusedTextColor by animateColorAsState(
        targetValue = if (isError) Theme.colors.error
        else Theme.colors.black87, label = ""
    )

    val unFocusedTextColor by animateColorAsState(
        targetValue = if (isError) Theme.colors.error
        else Theme.colors.black60, label = ""
    )

    val labelColor by animateColorAsState(
        targetValue = if (isTextFieldFocused) {
            Theme.colors.primary
        } else {
            Theme.colors.black87
        }, label = ""
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 8.dp),
            style = Theme.typography.body,
            color = labelColor
        )
        OutlinedTextField(
            modifier = textFieldModifier
                .fillMaxWidth()
                .height(56.dp)
                .onFocusChanged {
                    isTextFieldFocused = it.isFocused
                }
                .border(1.dp, color = borderColor, shape = shapeRadius),
            value = text,
            placeholder = {
                Text(
                    hint,
                    style = Theme.typography.body,
                    color = Theme.colors.black38
                )
            },
            onValueChange = onValueChange,
            shape = shapeRadius,
            textStyle = Theme.typography.body,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            leadingIcon = {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(0.15f)
                        .background(
                            color = Theme.colors.container,
                            shape = RoundedCornerShape(
                                topEnd = Theme.radius.large,
                                topStart = Theme.radius.large,
                                bottomStart = Theme.radius.large,
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier,
                        imageVector = ImageVector.vectorResource(id = leadingIcon),
                        contentDescription = "",
                        tint = Theme.colors.primary
                    )
                }

            },
            trailingIcon = if (keyboardType == KeyboardType.Password) {
                {
                    if (trailingButton) {
                        Row {
                            Button(
                                onClick = { onButtonClick() },
                                colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.primary),
                                shape = RoundedCornerShape(Theme.radius.medium),
                                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp)
                            ) {
                                Text(
                                    text = Theme.strings.change,
                                    color = Theme.colors.white,
                                    style = Theme.typography.caption
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }


                    } else {
                        TrailingIcon(showPassword) { showPassword = !showPassword }
                    }
                }
            } else if (keyboardType == KeyboardType.Phone) {
                {
                    if (trailingButton) {
                        Row {
                            Button(
                                onClick = { onButtonClick() },
                                colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.primary),
                                shape = RoundedCornerShape(Theme.radius.medium),
                                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp)
                            ) {
                                Text(
                                    text = Theme.strings.change,
                                    color = Theme.colors.white,
                                    style = Theme.typography.caption
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }

            } else null,
            visualTransformation = dhaibanVisualTransformation(keyboardType, showPassword),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Theme.colors.white,
                focusedContainerColor = Theme.colors.white,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedTextColor = focusedTextColor,
                unfocusedTextColor = unFocusedTextColor,
                focusedLeadingIconColor = Theme.colors.primary,
                unfocusedLeadingIconColor = Theme.colors.primary100,
                focusedLabelColor = Theme.colors.black87,
                unfocusedLabelColor = Theme.colors.black60,
                errorContainerColor = Theme.colors.white,
                errorCursorColor = Theme.colors.error,
                errorIndicatorColor = Theme.colors.error,
                errorTextColor = Theme.colors.error,
                cursorColor = Theme.colors.primary,
                disabledContainerColor = Theme.colors.white,
                disabledIndicatorColor = Theme.colors.transparent
            ),
            enabled = enabled
        )
        AnimatedVisibility(isError) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 8.dp),
                style = Theme.typography.body,
                color = Theme.colors.error
            )
        }
    }

}
@Composable
fun DhaibanMoblieTextField(
    modifier: Modifier = Modifier,
    textFieldModifier: Modifier = Modifier,
    label: String,
    text: String,
    enabled: Boolean = true,
    leadingIcon: Int,
    trailingButton: Boolean = false,
    hint: String = "",
    keyboardType: KeyboardType = KeyboardType.Text,
    shapeRadius: Shape = RoundedCornerShape(Theme.radius.large),
    singleLine: Boolean = true,
    errorMessage: String = "",
    isError: Boolean = errorMessage.isNotEmpty(),
    onValueChange: (String) -> Unit,
    onButtonClick: () -> Unit = {},
    onCountryChange: (CountryDetails) -> Unit) {

    var showPassword by remember { mutableStateOf(false) }

    var isTextFieldFocused by remember { mutableStateOf(false) }

    val borderColor by animateColorAsState(
        targetValue =
        if (isTextFieldFocused && !isError) {
            Theme.colors.primary
        } else if (isError) {
            Theme.colors.error
        } else {
            Color.Transparent
        }, label = ""
    )

    val focusedTextColor by animateColorAsState(
        targetValue = if (isError) Theme.colors.error
        else Theme.colors.black87, label = ""
    )

    val unFocusedTextColor by animateColorAsState(
        targetValue = if (isError) Theme.colors.error
        else Theme.colors.black60, label = ""
    )

    val labelColor by animateColorAsState(
        targetValue = if (isTextFieldFocused) {
            Theme.colors.primary
        } else {
            Theme.colors.black87
        }, label = ""
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(bottom = 8.dp),
            style = Theme.typography.body,
            color = labelColor
        )
        CountryPickerOutlinedTextField(
            modifier = textFieldModifier
                .fillMaxWidth()
                .height(56.dp)
                .onFocusChanged {
                    isTextFieldFocused = it.isFocused
                }
                .border(1.dp, color = borderColor, shape = shapeRadius),
            mobileNumber = text,
            placeholder = {
                Text(
                    hint,
                    style = Theme.typography.body,
                    color = Theme.colors.black38
                )
            },
            onMobileNumberChange = onValueChange,
            shape = shapeRadius,
            textStyle = Theme.typography.body,
            singleLine = singleLine,
//            leadingIcon = {
//                Box(
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .fillMaxWidth(0.15f)
//                        .background(
//                            color = Theme.colors.container,
//                            shape = RoundedCornerShape(
//                                topEnd = Theme.radius.large,
//                                topStart = Theme.radius.large,
//                                bottomStart = Theme.radius.large,
//                            )
//                        ),
//                    contentAlignment = Alignment.Center
//                ) {
//                    Icon(
//                        modifier = Modifier,
//                        imageVector = ImageVector.vectorResource(id = leadingIcon),
//                        contentDescription = "",
//                        tint = Theme.colors.primary
//                    )
//                }
//
//            },
            trailingIcon = if (keyboardType == KeyboardType.Password) {
                {
                    if (trailingButton) {
                        Row {
                            Button(
                                onClick = {
                                    onButtonClick()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.primary),
                                shape = RoundedCornerShape(Theme.radius.medium),
                                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp)
                            ) {
                                Text(
                                    text = Theme.strings.change,
                                    color = Theme.colors.white,
                                    style = Theme.typography.caption
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }


                    } else {
                        TrailingIcon(showPassword) { showPassword = !showPassword }
                    }
                }
            } else if (keyboardType == KeyboardType.Phone) {
                {
                    if (trailingButton) {
                        Row {
                            Button(
                                onClick = { onButtonClick() },
                                colors = ButtonDefaults.buttonColors(containerColor = Theme.colors.primary),
                                shape = RoundedCornerShape(Theme.radius.medium),
                                contentPadding = PaddingValues(vertical = 0.dp, horizontal = 24.dp)
                            ) {
                                Text(
                                    text = Theme.strings.change,
                                    color = Theme.colors.white,
                                    style = Theme.typography.caption
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                        }
                    }
                }

            } else null,
            visualTransformation = dhaibanVisualTransformation(keyboardType, showPassword),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Theme.colors.white,
                focusedContainerColor = Theme.colors.white,
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                focusedTextColor = focusedTextColor,
                unfocusedTextColor = unFocusedTextColor,
                focusedLeadingIconColor = Theme.colors.primary,
                unfocusedLeadingIconColor = Theme.colors.primary100,
                focusedLabelColor = Theme.colors.black87,
                unfocusedLabelColor = Theme.colors.black60,
                errorContainerColor = Theme.colors.white,
                errorCursorColor = Theme.colors.error,
                errorIndicatorColor = Theme.colors.error,
                errorTextColor = Theme.colors.error,
                cursorColor = Theme.colors.primary,
                disabledContainerColor = Theme.colors.white,
                disabledIndicatorColor = Theme.colors.transparent
            ),
            enabled = enabled,
            onCountrySelected ={
                onCountryChange(it)
            },
        )
        AnimatedVisibility(isError) {
            Text(
                text = errorMessage,
                modifier = Modifier.padding(top = 8.dp),
                style = Theme.typography.body,
                color = Theme.colors.error
            )
        }
    }

}

@Composable
private fun TrailingIcon(
    showPassword: Boolean,
    togglePasswordVisibility: () -> Unit,
) {
    IconButton(onClick = { togglePasswordVisibility() }) {
        Icon(
            imageVector = if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff,
            contentDescription = if (showPassword) "Show Password" else "Hide Password",
            tint = Theme.colors.dimGray
        )
    }

}

@Composable
private fun dhaibanVisualTransformation(
    keyboardType: KeyboardType,
    showPassword: Boolean,
): VisualTransformation {
    return if (showPassword || keyboardType != KeyboardType.Password && keyboardType != KeyboardType.NumberPassword) {
        VisualTransformation.None
    } else {
        PasswordVisualTransformation(mask = '✱')
    }
}

@Preview
@Composable
fun PreviewDhaibanTextFiled() {
    var text by remember { mutableStateOf("") }

    DhaibanTextField(
        label = "email",
        text = text,
        onValueChange = { text = it },
        leadingIcon = R.drawable.ic_launcher_foreground
    )
}