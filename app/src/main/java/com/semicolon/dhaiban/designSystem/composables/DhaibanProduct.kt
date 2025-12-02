package com.semicolon.dhaiban.designSystem.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.Theme
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect
import java.text.NumberFormat

@Composable
fun DhaibanProduct(
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
    onProductClick: (Int) -> Unit,
) {
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
            .clip(RoundedCornerShape(16.dp))
            .height(240.dp)
            .clickable { onProductClick(productId) }
    ) {
        Column(modifier.fillMaxSize()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .weight(6f), contentAlignment = Alignment.TopEnd
            ) {
                CoilImage(
                    shape = RoundedCornerShape(0.dp),
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
                Modifier
                    .fillMaxWidth()

            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Theme.colors.white)
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
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

                            Column {
                                Text(
                                    modifier = Modifier.padding(
                                        start = 5.dp
                                    ),
                                    text = NumberFormat.getNumberInstance().format(price * exchangeRate),

                                    color = Theme.colors.primary,
                                    style = Theme.typography.title,
                                )      }
                        }
                        else{
                            Column {
                                Text(
                                    modifier = Modifier.padding(
                                        start = 2.dp
                                    ),
                                    text = NumberFormat.getNumberInstance().format(price * exchangeRate),
                                    textDecoration = TextDecoration.LineThrough,
                                    color = Theme.colors.silverGray,
                                    style = Theme.typography.caption.copy(fontSize = 11.sp)
                                )
                                Text(
                                    modifier = Modifier.padding(start = 2.dp),
                                    text = "$currencySymbol ${
                                        NumberFormat.getNumberInstance()
                                            .format(afterDiscount * exchangeRate)
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
}