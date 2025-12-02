package com.semicolon.dhaiban.presentation.subCategory.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.subCategory.SubCategoryUiState

@Composable
fun SubCategoryComponent(
    subCategories: List<SubCategoryUiState>,
    selectedSubCategory: Int,
    modifier: Modifier = Modifier,
    onClick: (Int) -> Unit
) {
    LazyRow(
        modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        if (subCategories.isNotEmpty())
            item {
                SubgetAllCategoryItem(
                    id = -1,
                    image = R.drawable.getall,
                    title = Theme.strings.viewAll,
                    selected = selectedSubCategory == -1
                ) {
                    onClick(-1)
                }
            }
        items(
            items = subCategories,
            key = { subCategory ->
                subCategory.id
            }
        ) { subCategory ->
            SubCategoryItem(
                id = subCategory.id,
                image = subCategory.imageUrl,
                title = subCategory.title,
                selected = selectedSubCategory == subCategory.id
            ) {
                onClick(it)
            }
        }
    }
}


@Composable
private fun SubCategoryItem(
    id: Int,
    image: String,
    title: String,
    selected: Boolean = false,
    onClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(all = 8.dp)
            .noRippleEffect {
                onClick(id)
            }, contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            CoilImage(
                shape = CircleShape,
                url = image,
                contentDescription = "Sub-Category Image",
                modifier = Modifier.size(70.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = if (selected)
                    Theme.typography.body.copy(fontWeight = FontWeight.Bold)
                else Theme.typography.body.copy(
                    fontSize = 12.sp
                ),
                color = if (selected) Theme.colors.mediumBrown else Theme.colors.black
            )
        }

    }
}

@Composable
private fun SubgetAllCategoryItem(
    id: Int,
    image: Int,
    title: String,
    selected: Boolean = false,
    onClick: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(all = 8.dp)
            .noRippleEffect {
                onClick(id)
            }, contentAlignment = Alignment.Center

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.all2),
                contentDescription = null,
                modifier = Modifier.clip(CircleShape).size(70.dp),
contentScale = ContentScale.FillHeight,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = if (selected)
                    Theme.typography.body.copy(fontWeight = FontWeight.Bold)
                else Theme.typography.body.copy(
                    fontSize = 12.sp
                ),
                color = if (selected) Theme.colors.mediumBrown else Theme.colors.black
            )
        }

    }
}
