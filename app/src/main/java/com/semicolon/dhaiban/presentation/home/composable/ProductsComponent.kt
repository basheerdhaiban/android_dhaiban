package com.semicolon.dhaiban.presentation.home.composable

import android.util.Log
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.semicolon.dhaiban.presentation.home.ProductTypeUiState
import java.text.NumberFormat


@Composable
fun TopProducts(
    selectedId: Int,
    productTypes: List<ProductTypeUiState>,
    onNewProductsClick: () -> Unit,
    onProductTypeClicked: (Int) -> Unit
) {
    var selectedOption by remember { mutableIntStateOf(selectedId) }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .background(Theme.colors.background)
            .padding(vertical = 8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),

        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        item { // This ui of New Products
            Button(
                onClick = {
                    onNewProductsClick()
                    selectedOption = 0
                }, modifier = Modifier
                    .padding(end = 8.dp)
                    .height(35.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = Theme.colors.buttonTextGray,
                    containerColor = if (selectedOption == 0) Theme.colors.primary else Theme.colors.white
                ), shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text(
                    text = Theme.strings.newProduct,
                    color = if (selectedOption == 0) Theme.colors.white else Theme.colors.primary
                )
            }
        }

        items(items = productTypes, key = { it.id }) { productType ->
            ProductTypeItem(
                productType = productType,
                selectedId = selectedOption,
                onClick = { productTypeId ->
                    onProductTypeClicked(productTypeId)
                    selectedOption = productTypeId
                })
        }
    }
}

@Composable
fun ProductTypeItem(
    productType: ProductTypeUiState,
    selectedId: Int,
    onClick: (Int) -> Unit
) {
    Button(
        onClick = {
            onClick(productType.id)
        }, modifier = Modifier
            .padding(end = 8.dp)
            .height(35.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Theme.colors.buttonTextGray,
            containerColor = if (selectedId == productType.id) Theme.colors.primary else Theme.colors.white
        ), shape = RoundedCornerShape(8.dp),
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        Text(
            text = productType.title,
            color = if (selectedId == productType.id) Theme.colors.white else Theme.colors.primary
        )
    }
}

@Composable
fun ProductGridItem(
    modifier: Modifier = Modifier,
    productId: Int,
    imageUrl: String,
    productTitle: String,
    price: Double,
    afterDiscount: Double,
    currencySymbol: String,
    exchangeRate: Double,
    isFavourite: Boolean,
    onFavouriteClick: (Int, Boolean) -> Unit,
    onAddToCartClick: (Int) -> Unit,
    onProductClick: (Int) -> Unit,
) {
    Log.d("ExhangeRate",exchangeRate.toString())
    val iconPainter =
        painterResource(
            id = if (isFavourite) R.drawable.favourite_clicked
            else R.drawable.favourite_unclicked
        )

    Box(
        modifier
            .padding(
                bottom = 8.dp,
                end = 8.dp,
                start = 8.dp
            )
            .height(240.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable { onProductClick(productId) }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.70f)
                .clip(RoundedCornerShape(16.dp)), contentAlignment = Alignment.TopEnd
        ) {
            CoilImage(
                url = imageUrl,
                contentDescription = "Product Image",
                modifier = Modifier.fillMaxSize()
            )
            Image(
                modifier = Modifier
                    .padding(8.dp)
                    .size(30.dp)
                    .noRippleEffect {
                        onFavouriteClick(productId, isFavourite)
                    },
                painter = iconPainter,
                contentDescription = "FavoriteIcon"
            )
        }
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart
        ) {
            Column(Modifier.fillMaxWidth()) {
                Text(
                    text = productTitle,
                    style = Theme.typography.title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val difference = afterDiscount- price
                    if (difference==0.0){
Log.d("importanthome","${price.toString()}+ ${exchangeRate.toString()}+ ${productTitle.toString()}")
                        Column {
                            Text(
                                modifier = Modifier.padding(
                                    start = 5.dp
                                ),
                                text = "$currencySymbol ${
                                    NumberFormat.getNumberInstance().format(price* exchangeRate)
                                }",

                                color = Theme.colors.primary,
                                style = Theme.typography.title,
                            )      }
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
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun ProductsComponentPreview() {
    DhaibanTheme {
        TopProducts(selectedId = 0, productTypes = listOf(
            ProductTypeUiState(1, "Best Sellers"),
            ProductTypeUiState(2, "Today's offers"),
        ), {}) {}
    }
}