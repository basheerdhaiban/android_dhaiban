package com.semicolon.dhaiban.presentation.home.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.home.CategoryUiState


@Composable
fun CategoriesComponent(
    categories: List<CategoryUiState>,
    modifier: Modifier = Modifier,
    categoryBackColor: Color = Theme.colors.categoryBack,
    categoryTextColor: Color = Theme.colors.primary,
    onViewAllCLicked: () -> Unit,
    onCategoryClick: (Int, String) -> Unit

) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Theme.strings.categories,
                style = Theme.typography.header,
                color = Theme.colors.black87
            )
            Text(
                text = Theme.strings.viewAll,
                style = Theme.typography.body,
                color = Theme.colors.primary,
                modifier = Modifier.noRippleEffect { onViewAllCLicked() }


            )
        }
        LazyRow(
            modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(
                items = categories,
                key = { category ->
                    category.id
                }
            ) { category ->
                CategoryItem(
                    id = category.id,
                    title = category.title,
                    image = category.image,
                    backColor = categoryBackColor,
                    textColor = categoryTextColor,
                    onClick = onCategoryClick
                )
            }
        }
    }
}

@Composable
private fun CategoryItem(
    id: Int,
    title: String,
    image: String,
    backColor: Color,
    textColor: Color,
    onClick: (Int, String) -> Unit
) {
    Box(
        modifier = Modifier
            .background(
                color = Theme.colors.transparent
            )
            .padding(end = 8.dp)
            .noRippleEffect {
                onClick(id, title)
            },
        contentAlignment = Alignment.BottomEnd
    ) {
        Box(
            modifier = Modifier
                .width(180.dp)
                .height(80.dp)
                .background(
                    shape = RoundedCornerShape(16.dp),
                    color = backColor
                ),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                color = textColor,
                style = Theme.typography.body.copy(fontSize = 12.sp, fontWeight = FontWeight.W500),
                modifier = Modifier
                    .fillMaxWidth(0.58f),
                textAlign = TextAlign.Center
            )
        }
        CoilImage(
            url = image,
            contentDescription = "Category Image",
            modifier = Modifier
                .height(100.dp)
                .width(75.dp)
        )
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun CategoriesComponentPreview() {
    DhaibanTheme {
        CategoriesComponent(categories = listOf(
            CategoryUiState(
                1,
                "Fashion fcbdfndfn",
                "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
            ),
            CategoryUiState(
                2,
                "Beauty",
                "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
            ),
            CategoryUiState(
                3,
                "Bla",
                "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png"
            )
        ), onViewAllCLicked = {}) { _, _ ->

        }
    }

}