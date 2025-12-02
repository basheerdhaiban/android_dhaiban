package com.semicolon.dhaiban.presentation.subCategory

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.sharedUiState.SliderItemType
import com.semicolon.domain.entity.AttributeModel
import com.semicolon.domain.entity.Product
import com.semicolon.domain.entity.SliderItem
import com.semicolon.domain.entity.SubCategory
import com.semicolon.domain.entity.productdetails.ColorModel
import com.semicolon.domain.entity.productdetails.OptionModel

data class SubCategoryScreenUiState(
    val isLoading: Boolean = false,
    val isProductsLoading: Boolean = false,
    val subCategories: List<SubCategoryUiState> = emptyList(),
    val currency: SubCategoryCurrencyUiState = SubCategoryCurrencyUiState(),
    val selectedSubCategory: Int = 0,
    val sliderItems: List<SliderUiState> = emptyList(),
    val colors: List<ColorUiState> = emptyList(),
    val attributes: List<AttributeUiState> = emptyList(),
    val maxPrice: Double = 1500.0,
    val minPrice: Double = 0.0,
    val isFilterSelected: Boolean = false,
    val isAuthorized: Boolean = false,
    val errorMessage: String = "",
    val country: String = "",

    val firstItemIsVisible: Boolean = true,
)

@Immutable
data class SubCategoryUiState(
    val id: Int,
    val title: String,
    val imageUrl: String,
)

data class SubCategoryProductUiState(
    val id: Int = 0,
    val imageUrl: String = "",
    val isFavourite: Boolean = false,
    val title: String = "",
    val price: Double = 0.0,
    val afterDiscount: Double = 0.0,
)

@Immutable
data class SubCategoryCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)

@Immutable
data class SliderUiState(
    val id: Int = 0,
    val title: String = "",
    val description: String = "",
    val photoUrl: String = "",
    val type: SliderItemType = SliderItemType.PRODUCT,
    val typeId: Int = 0
)

@Immutable
data class ColorUiState(
    val colorCode: String,
    val id: Int
)

@Immutable
data class OptionUiState(
    val id: Int = 0,
    val title: String = "",
)

@Immutable
data class AttributeUiState(
    val id: Int = 0,
    val options: List<OptionUiState> = emptyList(),
    val title: String = ""
)

fun AttributeModel.toEntity() = AttributeUiState(
    id = this.id,
    options = this.options.map { it.toEntity() },
    title = this.title
)

fun ColorModel.toColorUiState() = ColorUiState(
    colorCode = this.colorCode,
    id = this.id
)

fun OptionModel.toEntity() = OptionUiState(id, title)

fun SubCategoryUiState.toOptionUiState() = OptionUiState(
    id = this.id,
    title = this.title
)

fun SubCategory.toEntity() = SubCategoryUiState(
    id = this.id,
    title = this.title,
    imageUrl = this.imageUrl
)

fun Product.toSubCategoryProductModel() = SubCategoryProductUiState(
    id = this.id,
    imageUrl = this.photo,
    isFavourite = userFavorite,
    title = this.title,
    price = unitPrice,
    afterDiscount = this.afterDiscount
)

fun SliderItem.toSliderUiState() =
    SliderUiState(
        id = this.id?:0,
        title = this.title?:"",
        description = this.description?:"",
        photoUrl = this.photoUrl?:"",
        typeId = this.typeId?:0,
        type = when (this.type) {
            "Product" -> SliderItemType.PRODUCT
            else -> SliderItemType.CATEGORY
        }
    )

fun AppCurrencyUiState.toSubCategoryCurrencyUiState() =
    SubCategoryCurrencyUiState(code, exchangeRate, id, name, symbol)