package com.semicolon.dhaiban.presentation.subCategory.composables

import android.graphics.Color.parseColor
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.AppBarWithIcon
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.subCategory.AttributeUiState
import com.semicolon.dhaiban.presentation.subCategory.ColorUiState
import com.semicolon.dhaiban.presentation.subCategory.OptionUiState
import com.semicolon.dhaiban.presentation.subCategory.SubCategoryUiState
import com.semicolon.dhaiban.presentation.subCategory.toOptionUiState


@Composable
fun FilterComponent(
    categoryId: Int,
    attributes: List<AttributeUiState>,
    colors: List<ColorUiState>,
    categories: List<SubCategoryUiState>,
    maxPrice: Double,
    minPrice: Double,
    currencySymbol: String = "",
    onBackClick: () -> Unit,
    onChooseCategoryItem: (Int) -> Unit,
    onChooseSortByItem: (Int) -> Unit,
    onChooseColorItem: (Int) -> Unit,
    onChoosePrice: (Float, Float) -> Unit,
    onChooseDynamicOption: (Int, Int) -> Unit,
    onClearDynamicOption: (Int, Int) -> Unit,
    onAllCleared: () -> Unit,
    onChooseOptions: () -> Unit
) {
    var priceRangeValue by remember {
        mutableStateOf(minPrice.toFloat()..maxPrice.toFloat())
    }
    var clearSelection by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AppBarWithIcon(
            title = Theme.strings.filter, withNotification = false, onClickUpButton = onBackClick
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            LazyColumn(
                modifier = Modifier.weight(0.9f), contentPadding = PaddingValues(bottom = 40.dp)
            ) {
                item {
                    FilterItemComponent(filterId = SORT_BY,
                        title = Theme.strings.sortBy,
                        clearSelection = clearSelection,
                        options = listOf(
                            OptionUiState(1, "Newly Arrived"),
                            OptionUiState(2, "Pre-existing"),
                            OptionUiState(3, "Price: Low to High"),
                            OptionUiState(4, "Price: High to Low"),
                            OptionUiState(5, "Offers and Discounts"),
                            OptionUiState(6, "Rating: High to Low"),
                        ),
                        onChooseSortByItem = onChooseSortByItem,
                        onCleared = {
                            clearSelection = false
                        })
                }
                if (categories.isNotEmpty()) item {
                    FilterItemComponent(filterId = CATEGORY,
                        categoryId = categoryId,
                        title = Theme.strings.category,
                        clearSelection = clearSelection,
                        options = categories.map { it.toOptionUiState() },
                        onChooseCategoryItem = onChooseCategoryItem,
                        onCleared = {
                            clearSelection = false
                        })
                }
                if (colors.isNotEmpty()) item {
                    FilterItemComponent(filterId = COLOR,
                        title = Theme.strings.color,
                        clearSelection = clearSelection,
                        colors = colors,
                        onChooseColorItem = onChooseColorItem,
                        onCleared = {
                            clearSelection = false
                        })
                }
                items(items = attributes, key = { it.id }) { attribute ->
                    if (attribute.options.isNotEmpty()) FilterItemComponent(filterId = attribute.id,
                        title = attribute.title,
                        clearSelection = clearSelection,
                        options = attribute.options,
                        onChooseDynamicOption = onChooseDynamicOption,
                        onClearDynamicOption = onClearDynamicOption,
                        onCleared = {
                            clearSelection = false
                        })
                }
                item {
                    Column {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = Theme.strings.priceRange, style = Theme.typography.title)
                        Spacer(modifier = Modifier.height(8.dp))
                        Column {
                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text = "$currencySymbol ${
                                        if (clearSelection) String.format("%.1f", minPrice)
                                            .toFloat()
                                        else String.format("%.2f", priceRangeValue.start).toFloat()
                                    }",
                                    style = Theme.typography.caption,
                                    color = Theme.colors.dimGray
                                )
                                Text(
                                    text = "$currencySymbol ${
                                        if (clearSelection) String.format("%.1f", maxPrice)
                                            .toFloat()
                                        else String.format("%.2f", priceRangeValue.endInclusive)
                                            .toFloat()
                                    }",
                                    style = Theme.typography.caption,
                                    color = Theme.colors.dimGray
                                )
                            }
                            RangeSlider(
                                enabled = if (minPrice.toFloat() == maxPrice.toFloat()) false else true,
                                value = if (clearSelection) (minPrice.toFloat()..maxPrice.toFloat())
                                else priceRangeValue,
                                onValueChange = {
                                    clearSelection = false
                                    priceRangeValue = it
                                },
                                valueRange = if (minPrice.toFloat() != maxPrice.toFloat())
                                    minPrice.toFloat()..maxPrice.toFloat() else   minPrice.toFloat()..minPrice.toFloat(),
                                colors = SliderDefaults.colors(
                                    thumbColor = Theme.colors.white,
                                    activeTrackColor = Theme.colors.mediumBrown,
                                    inactiveTrackColor = Theme.colors.warmGray
                                ),
                                onValueChangeFinished = {
                                    onChoosePrice(
                                        priceRangeValue.start, priceRangeValue.endInclusive
                                    )
                                },
                            )
                        }
                    }
                }
            }
            Row(modifier = Modifier.padding(bottom = 10.dp)) {
                Button(
                    onClick = {
                        onAllCleared()
                        clearSelection = true
                    },
                    modifier = Modifier
                        .border(
                            1.dp, Theme.colors.mediumBrown, RoundedCornerShape(6.dp)
                        )
                        .height(40.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Theme.colors.white, containerColor = Theme.colors.background
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)

                ) { Text(text = Theme.strings.clear, color = Theme.colors.black) }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        onChooseOptions()
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Theme.colors.white, containerColor = Theme.colors.mediumBrown
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 0.dp)

                ) { Text(text = Theme.strings.done, color = Theme.colors.white) }
            }
        }

    }
}


@Composable
fun FilterItemComponent(
    filterId: Int,
    categoryId: Int = 0,
    title: String,
    clearSelection: Boolean = false,
    options: List<OptionUiState> = emptyList(),
    colors: List<ColorUiState> = emptyList(),
    onCleared: () -> Unit = {},
    onChooseCategoryItem: (Int) -> Unit = {},
    onChooseSortByItem: (Int) -> Unit = {},
    onChooseColorItem: (Int) -> Unit = {},
    onChooseDynamicOption: (Int, Int) -> Unit = { _, _ -> },
    onClearDynamicOption: (Int, Int) -> Unit = { _, _ -> }
) {
    var expandedState by remember { mutableStateOf(false) }
    val rotationState by animateFloatAsState(
        targetValue = if (expandedState) 180f else 0f, label = ""
    )
    var selectedId by remember { mutableIntStateOf(-1) }
    var optionIds: List<Int> by remember { mutableStateOf(listOf()) }
    if (clearSelection) {
        selectedId = -1
        optionIds = emptyList()
    }
    Column(modifier = Modifier
        .fillMaxWidth()
        .animateContentSize(
            animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
        )
        .noRippleEffect { expandedState = !expandedState }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title,
                modifier = Modifier.weight(0.9f),
                style = Theme.typography.title,
            )
            Image(
                modifier = Modifier
                    .weight(0.1f)
                    .rotate(rotationState)
                    .noRippleEffect { expandedState = !expandedState },
                painter = painterResource(id = R.drawable.down_arrow),
                contentDescription = "Drop Down Arrow"
            )
        }
        if (expandedState) {
            when (filterId) {
                SORT_BY -> {
                    Column {
                        options.forEach { option ->
                            FilterOptionComponent(
                                title = option.title, selected = option.id == selectedId
                            ) {
                                selectedId = if (it) {
                                    option.id
                                } else {
                                    -1
                                }
                                onCleared()
                                onChooseSortByItem(selectedId)
                            }
                        }
                    }
                }

                CATEGORY -> {
                    Column {
                        options.forEach { option ->
                            FilterOptionComponent(
                                title = option.title, selected = option.id == selectedId
                            ) {
                                selectedId = if (it) {
                                    option.id
                                } else {
                                    categoryId
                                }
                                onCleared()
                                onChooseCategoryItem(selectedId)
                            }
                        }
                    }
                }

                COLOR -> {
                    Column {
                        colors.distinct().forEach { color ->
                            FilterOptionComponent(
                                colorCode = color.colorCode, selected = color.id == selectedId
                            ) {
                                selectedId = if (it) {
                                    color.id
                                } else {
                                    -1
                                }
                                onCleared()
                                onChooseColorItem(selectedId)
                            }
                        }
                    }
                }

                else -> {
                    Column {
                        options.forEach { option ->
                            FilterOptionComponent(
                                title = option.title, selected = option.id in optionIds
                            ) {
                                optionIds = if (it) {
                                    onChooseDynamicOption(filterId, option.id)
                                    optionIds + option.id
                                } else {
                                    onClearDynamicOption(filterId, option.id)
                                    optionIds - option.id
                                }
                                onCleared()
                            }
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Theme.colors.silver.copy(alpha = 0.5f))
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun FilterOptionComponent(
    title: String = "",
    colorCode: String = "",
    selected: Boolean = false,
    onCheckChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (title.isNotEmpty()) Text(
            text = title,
            modifier = Modifier.weight(0.9f),
            style = Theme.typography.title,
            color = Theme.colors.warmGray
        )
        if (colorCode.isNotEmpty()) Box(
            modifier = Modifier
                .padding(end = 4.dp)
                .size(25.dp)
                .background(
                    color = Color(parseColor(colorCode)), shape = CircleShape
                )
                .clip(CircleShape)
        )
        Checkbox(
            checked = selected,
            onCheckedChange = onCheckChange,
            colors = CheckboxDefaults.colors(checkedColor = Theme.colors.mediumBrown)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun FilterComponentPreview() {
    DhaibanTheme {
        FilterComponent(
            categoryId = 0,
            attributes = listOf(),
            colors = listOf(),
            categories = listOf(),
            maxPrice = 1000.0,
            minPrice = 0.0,
            onBackClick = {},
            onChooseCategoryItem = {},
            onChoosePrice = { _, _ -> },
            onChooseSortByItem = {},
            onChooseColorItem = {},
            onClearDynamicOption = { _, _ -> },
            onAllCleared = {},
            onChooseDynamicOption = { _, _ -> },
        ) {}
    }
}

//@Preview(showBackground = true)
//@Composable
//fun FilterItemComponentPreview() {
//    DhaibanTheme {
//        FilterItemComponent(title = "Title")
//    }
//}

@Preview(showBackground = true)
@Composable
fun FilterOptionComponentPreview() {
    DhaibanTheme {
        FilterOptionComponent(title = "Title") {

        }
    }
}

const val SORT_BY: Int = -1
const val CATEGORY: Int = -2
const val COLOR: Int = -3