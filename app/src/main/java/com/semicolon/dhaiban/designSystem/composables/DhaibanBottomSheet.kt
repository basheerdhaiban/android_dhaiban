package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.domain.utils.UserConfig

@Composable
fun DhaibanBottomSheet(
    userConfig: UserConfig,
    items: List<Pair<String, Int>>,
    selectedOption: String,
    searchValue: String,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onSelectItem: (String, Int) -> Unit,
    onClickCancel: () -> Unit,
    onClickConfirm: () -> Unit
) {
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

        val headerTitle = when (userConfig) {
            UserConfig.COUNTRY -> Theme.strings.country
            UserConfig.LANGUAGE -> Theme.strings.language
            UserConfig.CURRENCY -> Theme.strings.currency
        }

        val headerIconRes = when (userConfig) {
            UserConfig.COUNTRY -> R.drawable.icon_language
            UserConfig.LANGUAGE -> R.drawable.icon_language
            UserConfig.CURRENCY -> R.drawable.icon_currency
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
                .weight(3f)
                .background(backgroundColor.value),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Theme.colors.white),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .padding(top = 16.dp, end = 16.dp, start = 16.dp)
                    .safeDrawingPadding(), horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Bottom),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    HeaderContent(
                        headerIconRes = headerIconRes,
                        headerTitle = headerTitle,
                        onClickCancel = onClickCancel,
                        onClickConfirm = onClickConfirm
                    )

                    DhaibanSearchBar(
                        modifier = Modifier.padding(bottom = 16.dp),
                        value = searchValue,
                        hint = Theme.strings.search,
                        onQueryChanged = {
                            onQueryChanged(it)
                        }
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        item {
                            AnimatedVisibility(
                                visible = isLoading,
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(300.dp)
                                        .fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = Theme.colors.primary)
                                }
                            }
                        }

                        items(items = items) { item ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    RadioButtonItem(
                                        option = item.first,
                                        isSelected = (item.first == selectedOption),
                                        onClick = {
                                            onSelectItem(item.first, item.second)
                                            onClickConfirm()
                                        }
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(1.dp)
                                            .background(Theme.colors.silver.copy(alpha = 0.5f))
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun RadioButtonItem(
    option: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = option, style = Theme.typography.body, color = Theme.colors.greyishBrown)
        RadioButton(
            selected = isSelected,
            onClick = { onClick(option) },
            colors = RadioButtonDefaults.colors(
                selectedColor = Theme.colors.primary,
                unselectedColor = Theme.colors.black38
            )
        )
    }
}

@Composable
private fun HeaderContent(
    modifier: Modifier = Modifier,
    headerIconRes: Int,
    headerTitle: String,
    onClickCancel: () -> Unit,
    onClickConfirm: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        Icon(
            imageVector = ImageVector.vectorResource(headerIconRes),
            contentDescription = "header icon",
            tint = Theme.colors.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = headerTitle,
            style = Theme.typography.headline.copy(fontWeight = FontWeight.Medium),
            color = Theme.colors.primary
        )
    }
    /*Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier.noRippleEffect { onClickConfirm() },
            painter = painterResource(id = R.drawable.confirm_icon),
            contentDescription = "Confirm Icon"
        )
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(
                imageVector = ImageVector.vectorResource(headerIconRes),
                contentDescription = "header icon",
                tint = Theme.colors.primary
            )
            Text(
                text = headerTitle,
                style = Theme.typography.headline.copy(fontWeight = FontWeight.Medium),
                color = Theme.colors.primary
            )
        }
        Image(
            modifier = Modifier.noRippleEffect { onClickCancel() },
            painter = painterResource(id = R.drawable.cancel_icon),
            contentDescription = "Cancel Icon"
        )

    }*/
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun BottomDialogPreview() {
    DhaibanTheme {
        DhaibanBottomSheet(
            userConfig = UserConfig.LANGUAGE,
            items = listOf(
                "Egypt" to 0,
                "Palestine" to 0,
                "Saudi-Arabia" to 0, "Egypt" to 0,
                "Palestine" to 0,
                "Saudi-Arabia" to 0, "Egypt" to 0,
                "Palestine" to 0,
                "Saudi-Arabia" to 0, "Egypt" to 0,
                "Palestine" to 0,
                "Saudi-Arabia" to 0,
            ),
            selectedOption = "",
            searchValue = "",
            isLoading = false,
            {},
            {},
            { _, _ -> },
            {},
            {},
        )
    }
}