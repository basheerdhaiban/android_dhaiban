package com.semicolon.dhaiban.presentation.subCategory.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.semicolon.dhaiban.designSystem.DhaibanTheme
import com.semicolon.dhaiban.designSystem.composables.DhaibanProduct


@Preview(showBackground = false)
@Composable
fun SubCategoryProductGridItemPreview() {
    DhaibanTheme {
        DhaibanProduct(
            productId = 1,
            imageUrl = "",
            productTitle = "Bla Bla",
            price = 500.0,
            afterDiscount = 400.0,
            currencySymbol = "$",
            exchangeRate = 1.0,
            isFavourite = false,
            onFavouriteClick = { _, _ -> },
            onProductClick = {}
        )

    }
}