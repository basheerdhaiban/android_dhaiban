package com.semicolon.dhaiban.presentation.product.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.presentation.product.ProductUiState
import java.text.NumberFormat

@Composable
fun RecommendedProductsComponent(
    products: List<ProductUiState>,
    exchangeRange : Double = 1.0,
    currencySymbol: String = "$",
    onProductClick: (Int) -> Unit,
) {
    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp)) {
        items(items = products.distinct(), key = { it.id }) { product ->
            RecommendedProductItem(
                productId = product.id,
                imageUrl = product.imageUrl,
                productTitle = product.title,
                price = product.price,
                currencySymbol = currencySymbol,
                exchangeRate = exchangeRange,
                onProductClick = onProductClick
            )
        }
    }
}

@Composable
private fun RecommendedProductItem(
    modifier: Modifier = Modifier,
    productId: Int,
    imageUrl: String,
    productTitle: String,
    price: Double,
    currencySymbol: String,
    exchangeRate: Double,
    onProductClick: (Int) -> Unit,
) {
    Box(
        modifier
            .padding(
                bottom = 8.dp,
                end = 8.dp,
                start = 8.dp
            )
            .clip(RoundedCornerShape(16.dp))
            .height(200.dp)
            .width(160.dp)
            .clickable { onProductClick(productId) }
    ) {
        Box(
            Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.TopEnd
        ) {
            CoilImage(
                shape = RoundedCornerShape(0.dp),
                url = imageUrl,
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxSize()
            )
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .background(Theme.colors.white)
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.fillMaxWidth(0.80f)) {
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = productTitle,
                            style = Theme.typography.title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp),
                            text = "$currencySymbol ${
                                NumberFormat.getNumberInstance().format(price * exchangeRate)
                            }",
                            color = Theme.colors.primary,
                            style = Theme.typography.title,
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun RecommendedProductItemPreview() {
    RecommendedProductItem(
        productId = 1,
        imageUrl = "",
        productTitle = "Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla Bla",
        price = 400.0,
        currencySymbol = "$",
        exchangeRate = 1.0,
        onProductClick = {}
    )
}