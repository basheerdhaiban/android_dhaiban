package com.semicolon.dhaiban.presentation.trackorder.composables

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.PrimaryButton
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.trackorder.RefundReasonUiState

@Composable
fun ReturnProductReasonsBottomSheet(
    refundReasons: List<RefundReasonUiState>,
    errorMessage: String,
    loadingState: Boolean,
    onSelectReason: (Int) -> Unit,
    onDismiss: () -> Unit,
    onClickSend: (Int, String, RefundError) -> Unit
) {
    var userComment by remember { mutableStateOf("") }
    var selectedReasonId by remember { mutableStateOf(0) }
    Column(
        modifier = Modifier
            .fillMaxHeight()
    ) {
        val startColor = Theme.colors.transparent
        val endColor = Theme.colors.black.copy(alpha = 0.2f)

        val backgroundColor = remember { Animatable(startColor) }

        LaunchedEffect(Unit) {
            backgroundColor.animateTo(endColor, animationSpec = tween(1000))
        }

        Box(
            Modifier
                .noRippleEffect { onDismiss() }
                .weight(2f)
                .fillMaxWidth()
                .background(backgroundColor.value)
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (errorMessage.isNotEmpty()) 2.5f else 2f)
                .background(backgroundColor.value),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(top = 24.dp, end = 16.dp, start = 16.dp)
                    .safeDrawingPadding(), horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = Theme.strings.returnProduct,
                        style = Theme.typography.titleLarge,
                        color = Theme.colors.black
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.80f)
                        .height(1.dp)
                        .background(Theme.colors.silver.copy(alpha = 0.5f))
                )

                Column(
                    modifier = Modifier
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = Theme.strings.RefundReasons, 
                            style = Theme.typography.title
                        )
                    }

                    AnimatedVisibility(visible = refundReasons.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier.height(100.dp),
                            contentPadding = PaddingValues(4.dp),
                            verticalArrangement = Arrangement.Center
                        ) {
                            items(items = refundReasons, key = { it.reasonId }) { refundReason ->
                                ReasonItem(reason = refundReason) {
                                    selectedReasonId = it
                                    onSelectReason(it)
                                }
                            }
                        }
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = Theme.strings.writeYourComment,
                            style = Theme.typography.title
                        )
                    }

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        value = userComment,
                        onValueChange = { userComment = it },
                        placeholder = {
                            Text(
                                text = "Write your comment here ..",
                                style = Theme.typography.caption,
                                color = Theme.colors.silverGray
                            )
                        },
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Theme.colors.whiteTwo,
                            unfocusedIndicatorColor = Theme.colors.whiteTwo,
                            unfocusedContainerColor = Theme.colors.background,
                            cursorColor = Theme.colors.mediumBrown,
                            selectionColors = TextSelectionColors(
                                handleColor = Theme.colors.mediumBrown,
                                backgroundColor = Theme.colors.darkBeige
                            )
                        )
                    )


                    AnimatedVisibility(visible = errorMessage.isNotEmpty()) {
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                            Text(
                                modifier = Modifier.padding(bottom = 8.dp),
                                text = errorMessage,
                                style = Theme.typography.body,
                                color = Theme.colors.error
                            )
                        }
                    }

                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = Theme.strings.send,
                        isLoading = loadingState,
                        onClick = {
                            when {
                                selectedReasonId != 0 && userComment.isNotEmpty() -> onClickSend(
                                    selectedReasonId,
                                    userComment,
                                    RefundError.NONE
                                )

                                selectedReasonId == 0 && userComment.isEmpty() -> onClickSend(
                                    selectedReasonId,
                                    userComment,
                                    RefundError.ALL_REQUIRED
                                )

                                selectedReasonId == 0 -> onClickSend(
                                    selectedReasonId,
                                    userComment,
                                    RefundError.UNSELECTED
                                )

                                userComment.isEmpty() -> onClickSend(
                                    selectedReasonId,
                                    userComment,
                                    RefundError.EMPTY_COMMENT
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

enum class RefundError(val errorMessage: String) {
    UNSELECTED("Select reason please"),
    EMPTY_COMMENT("Comment can't be empty"),
    ALL_REQUIRED("All fields are required"),
    NONE("")
} // todo add all to strings file

@Composable
fun ReasonItem(
    reason: RefundReasonUiState,
    onChoose: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = reason.reason, color = Theme.colors.greyishBrown, style = Theme.typography.body)
        RadioButton(
            modifier = Modifier.size(30.dp),
            selected = reason.selected,
            onClick = { onChoose(reason.reasonId) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Theme.colors.mediumBrown, unselectedColor = Theme.colors.mediumBrown
            )
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ReturnProductReasonsBottomSheetPreview() {
    DhaibanTheme {
        ReturnProductReasonsBottomSheet(
            listOf(
                RefundReasonUiState(1, "bla", false),
                RefundReasonUiState(2, "bla", false),
                RefundReasonUiState(3, "bla", false),
                RefundReasonUiState(4, "bla", false),
                RefundReasonUiState(5, "bla", true)
            ),
            "",
            false,
            {}, {}) { _, _, _ -> }
    }
}

@Preview(showBackground = true)
@Composable
fun ReasonItemPreview() {
    DhaibanTheme {
        ReasonItem(RefundReasonUiState()) {}
    }
}