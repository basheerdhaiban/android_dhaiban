package com.semicolon.dhaiban.presentation.home.composable

import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.home.BrandUiState

@Composable
fun BrandsComponent(
    brands: List<BrandUiState>,
    modifier: Modifier = Modifier,
    onViewAllCLicked: () -> Unit,
    onBrandClick: (Int, String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = Theme.strings.topBrands,
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
        if (brands.isNotEmpty()){
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(
                    items = brands,
                    key = { brand ->
                        brand.id
                    }
                ) { brand ->
                    BrandItem(
                        id = brand.id,
                        logo = brand.logo,
                        brandTitle = brand.title,
                        onClick = onBrandClick
                    )
                }
            }
        }else{
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.image_error_icon),
                    contentDescription = ""
                )
            }
        }

    }
}

@Composable
fun BrandItem(
    id: Int,
    logo: String,
    brandTitle: String,
    onClick: (Int, String) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(all = 8.dp)
            .clip(shape = RoundedCornerShape(8.dp))
            .height(60.dp)
            .width(80.dp)
            .background(Theme.colors.transparent)
            .noRippleEffect {
                onClick(id, brandTitle)
            }, contentAlignment = Alignment.Center

    ) {
        CoilImage(
            url = logo,
            contentDescription = "Brand Logo",
            shape = RoundedCornerShape(0.dp),
            scaleType = ContentScale.Crop,
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun BrandsComponentPrev() {
    DhaibanTheme {

        BrandsComponent(
            brands = listOf(
                BrandUiState(1, "Zara", ""),
                BrandUiState(2, "Zara", ""),
                BrandUiState(3, "Zara", ""),
                BrandUiState(4, "Zara", ""),
                BrandUiState(5, "Zara", ""),
                BrandUiState(6, "Zara", ""),
            ),
            onViewAllCLicked = {}
        ) {_,_ ->}
    }
}