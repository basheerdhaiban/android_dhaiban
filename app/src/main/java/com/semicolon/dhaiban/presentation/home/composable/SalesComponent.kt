package com.semicolon.dhaiban.presentation.home.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import com.semicolon.dhaiban.presentation.home.SalesUiState
import java.text.NumberFormat

@Composable
fun SalesComponent(
    modifier: Modifier = Modifier,
    salesProducts: List<SalesUiState>,
    currencySymbol: String,
    exchangeRate: Double,
    onViewAllCLicked: () -> Unit,
    onFavouriteClick: (Int, Boolean) -> Unit,
    onAddToCartClick: (Int) -> Unit,
    onProductClick: (Int) -> Unit,
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
                text = Theme.strings.flashSale,
                style = Theme.typography.header,
                color = Theme.colors.black87,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = Theme.strings.viewAll,
                style = Theme.typography.body,
                color = Theme.colors.primary,
                modifier = Modifier.noRippleEffect { onViewAllCLicked() }


            )
        }
        if (salesProducts.isNotEmpty()) {
            LazyRow(
                modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(
                    items = salesProducts,
                    key = { salesProduct ->
                        salesProduct.id
                    }
                ) { salesProduct ->
                    SalesItem(
                        id = salesProduct.id,
                        imageUrl = salesProduct.imageUrl,
                        isFavourite = salesProduct.isFavourite,
                        title = salesProduct.title,
                        description = salesProduct.description,
                        price = salesProduct.price,
                        afterDiscount = salesProduct.afterDiscount,
                        currencySymbol = currencySymbol,
                        exchangeRate = exchangeRate,
                        onFavouriteClick = onFavouriteClick,
                        onAddToCartClick = onAddToCartClick,
                        onProductClick = onProductClick
                    )
                }
            }
        } else {
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
fun SalesItem(
    id: Int,
    imageUrl: String,
    isFavourite: Boolean,
    title: String,
    description: String,
    price: Double,
    afterDiscount: Double,
    currencySymbol: String,
    exchangeRate: Double,
    onFavouriteClick: (Int, Boolean) -> Unit,
    onAddToCartClick: (Int) -> Unit,
    onProductClick: (Int) -> Unit
) {
    Box(
        Modifier
            .width(190.dp)
            .height(260.dp)
            .padding(end = 16.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Theme.colors.white)
            .clickable {
                onProductClick(id)
            },
        contentAlignment = Alignment.BottomStart
    ) {
        val iconPainter =
            painterResource(
                id = if (isFavourite) R.drawable.favourite_clicked
                else R.drawable.favourite_unclicked
            )
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            CoilImage(
                modifier = Modifier
                    .fillMaxHeight(0.50f)
                    .fillMaxWidth(),
                url = imageUrl,
                contentDescription = "Product Image",
                scaleType = ContentScale.FillHeight,
                shape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp)
            )
        }
        Image(
            modifier = Modifier
                .padding(8.dp)
                .size(30.dp)
                .noRippleEffect {
                    onFavouriteClick(id, isFavourite)
                }
                .align(Alignment.TopEnd),
            painter = iconPainter,
            contentDescription = "FavoriteIcon"
        )
        Column(
            modifier = Modifier
                .fillMaxHeight(0.50f)
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Theme.colors.black87,
                style = Theme.typography.caption
            )
            Text(
                modifier = Modifier.padding(start = 2.dp),
                text = description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = Theme.colors.black87,
                style = Theme.typography.caption
            )
            val difference = afterDiscount- price
            if (difference==0.0){
                Row(
                    Modifier
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = "$currencySymbol ${
                            NumberFormat.getNumberInstance().format(price* exchangeRate)
                        }",
                        color = Theme.colors.primary,
                        style = Theme.typography.title,
                    )
                }

            }

            else{
            Text(
                modifier = Modifier.padding(
                    top = 12.dp,
                    start = 2.dp
                ),
                text = NumberFormat.getNumberInstance().format(price * exchangeRate),
                textDecoration = TextDecoration.LineThrough,
                color = Theme.colors.silverGray,
                style = Theme.typography.caption.copy(fontSize = 11.sp)
            )
            Row(
                Modifier
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier.padding(start = 2.dp),
                    text = "$currencySymbol ${
                        NumberFormat.getNumberInstance().format(afterDiscount * exchangeRate)
                    }",
                    color = Theme.colors.primary,
                    style = Theme.typography.title,
                )
            } }
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun SalesComponentPreview() {
    DhaibanTheme {
        SalesComponent(salesProducts = listOf(
            SalesUiState(
                1,
                "",
                false,
                "Smart Watch",
                "Smart watch smart smart smart",
                90.0,
                80.04
            ),
            SalesUiState(
                2,
                "",
                true,
                "Smart Watch",
                "Smart watch smart smart smart",
                90.0,
                85000.0
            ),
            SalesUiState(
                3,
                "",
                false,
                "Smart Watch",
                "Smart watch smart smart smart",
                90.0,
                85000.0
            )
        ),
            currencySymbol = "$",
            exchangeRate = 1.5,
            onViewAllCLicked = {},
            onFavouriteClick = { _, _ -> },
            onAddToCartClick = {},
            onProductClick = {})
    }

}