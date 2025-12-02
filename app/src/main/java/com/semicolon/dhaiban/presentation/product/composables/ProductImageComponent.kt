package com.semicolon.dhaiban.presentation.product.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.semicolon.dhaiban.R
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.composables.CoilImage
import com.semicolon.dhaiban.designSystem.composables.modifier.noRippleEffect

@Composable
fun ProductImageComponent(
    modifier: Modifier = Modifier,
    productId: Int,
    selectedImage: String,
    isFavourite: Boolean,
    onFavouriteClick: (Int, Boolean) -> Unit,
    onImageClick: () -> Unit
) {

    val iconPainter = painterResource(
        id = if (isFavourite) R.drawable.favourite_clicked
        else R.drawable.favourite_unclicked
    )
    Box(
        modifier = modifier
            .fillMaxWidth(0.80f)
            .height(220.dp)
            .clickable { onImageClick() },
        contentAlignment = Alignment.TopEnd
    ) {
        CoilImage(
            modifier = Modifier.fillMaxWidth(),
            url = selectedImage,
            contentDescription = "ProductImage"
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductImageComponentPreview() {
    DhaibanTheme {
        ProductImageComponent(
            productId = 1,
            selectedImage = "",
            isFavourite = false,
            onFavouriteClick = {_,_ ->}) {

        }
    }
}