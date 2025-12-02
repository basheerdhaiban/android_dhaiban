package com.semicolon.dhaiban.presentation.product

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.home.UserDataUiState
import com.semicolon.domain.entity.Product
import com.semicolon.domain.entity.productdetails.ChoiceOptionModel
import com.semicolon.domain.entity.productdetails.ColorModel
import com.semicolon.domain.entity.productdetails.OptionModel
import com.semicolon.domain.entity.productdetails.PhotoModel
import com.semicolon.domain.entity.productdetails.ProductDetailsModel
import com.semicolon.domain.entity.productdetails.ReviewModel

data class ProductScreenUiState(
    val items: List<com.semicolon.dhaiban.presentation.cart.CartItemUiState> = emptyList(),
    var momo: String = "",
    var transcationCode: String = "",
    var isLoading: Boolean = false,
    var isLoadingonGetDetailsSuccess: Boolean = true,

    val productDetails: ProductDetailsUiState = ProductDetailsUiState(),
    val productReviews: List<ReviewUiState> = emptyList(),
    val recommendedProducts: List<ProductUiState> = emptyList(),
    val currencySymbol: String = "",
    val currency: ProductCurrencyUiState = ProductCurrencyUiState(),
    val currencyId: Int = 0,
    val addToCartLoadingState: Boolean = false,
    val isExpandImagesSelected: Boolean = false,
    val showDialog: Boolean = false,
    val recreateState: Boolean = false,
    val isInCart: Boolean = false,
    val userData: UserDataUiState = UserDataUiState(),
    val showCartDialog: Boolean = false
)

data class ProductDetailsUiState(
    val brandName: String = "",
    val brandPhoto: String = "",
    val productId: Int = 0,
    val title: String = "",
    val shortDescription: String = "",
    val isFavourite: Boolean = false,
    val color: String = "",
    val lowerPrice: Double = 0.0,
    val higherPrice: Double = 0.0,
    val colors: List<ColorUiState> = emptyList(),
    val fullDescription: String = "",
    val productImage: String = "",
    val productImages: List<PhotoUiState> = emptyList(),
    val selectedImage: String = "",
    val selectedImageId: Int = 0,
    val selectedColorId: Int = 0,
    val choiceOptions: List<ChoiceOptionUiState> = emptyList(),
    val rating: Double = 0.0,
    val ratingCount: Int = 0,
    val stockCount: Int = 0,
    val properties: PropertiesUiState = PropertiesUiState(),
    val variantPrice: Double = -99999.999,
    val optionSelected: Boolean = false,
    val cart: List<CartItemUiState> = emptyList(),
    val identifier: Long = 0L,
    val cartQuantity: Int = 0,
    val variantQuantity: Int = 0,
    val seller: String = "", val discount: Double = 1.0,
    val discountType: String = "",
    val afterdiscount: Double = 0.0

)

@Immutable
data class CartItemUiState(
    val identifier: Long, val quantity: Int, val variant: String
)

@Immutable
data class PhotoUiState(
    val id: Int = 0, val imageUrl: String = ""
)

@Immutable
data class ProductCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)

data class ColorUiState(
    val colorCode: String, val id: Int
)

data class ReviewUiState(
    val id: Int = 0,
    val averageRating: Double = 0.0,
    val username: String = "",
    val createdDate: String = "",
    val updatedDate: String = "",
    val review: String = ""
)

data class ProductReviewsUiState(
    val totalRate: TotalRateUiState = TotalRateUiState(),
)

data class TotalRateUiState(
    val averageRating: Double = 0.0, val ratingCount: Int = 0
)

data class OptionUiState(
    val id: Int = 0,
    val title: String = "",
)

data class ChoiceItem(
    val id: Int, val title: String, val parentId: Int, val parentTitle: String
)

data class ChoiceOptionUiState(
    val id: Int = 0,
    val choiceTitle: String = "",
    val options: List<OptionUiState> = emptyList(),
    val selectedOptionId: Int = 0
)

data class ProductUiState(
    val id: Int = 0,
    val imageUrl: String = "",
    val title: String = "",
    val price: Double = 0.0,
)

@Immutable
data class UserDataUiState(
    val username: String = "", val isAuthenticated: Boolean = false
)


data class PropertiesUiState(
    val properties: Map<String, String> = emptyMap()
)

fun ColorModel.toColorUiState() = ColorUiState(
    colorCode = this.colorCode, id = this.id
)

fun PhotoModel.toPhotoUiState() = PhotoUiState(
    id = this.id, imageUrl = this.photoUrl
)

fun OptionModel.toOptionUiState() = OptionUiState(id, title)

fun ChoiceOptionModel.toChoiceOptionUiState() = ChoiceOptionUiState(id = this.id.toInt(),
    choiceTitle = this.title,
    options = this.options.map { it.toOptionUiState() })

fun ProductDetailsModel.toEntity() = ProductDetailsUiState(seller = this.seller,
    brandName = this.brand.name,
    brandPhoto = this.brand.logo,
    productId = this.id,
    title = this.title,
    shortDescription = this.shortDescription,
    isFavourite = this.userFavorite,
    color = labelColor,
    lowerPrice = priceLower,
    higherPrice = priceHigher,
    colors = this.colors.map { it.toColorUiState() },
    fullDescription = this.description,
    productImage = this.photo,
    productImages = this.photos.map { it.toPhotoUiState() },
    rating = this.rating,
    ratingCount = this.ratingCount,
    stockCount = this.currentStock,
    properties = PropertiesUiState(this.properties),
    selectedImage = this.photo,
    selectedImageId = if (photos.isNotEmpty()) this.photos.first().id else 0,
    selectedColorId = 0,
    discount = discount,
    afterdiscount = afterDiscount,
    discountType = discountType,
    choiceOptions = this.choiceOptions.map { it.toChoiceOptionUiState() },
    cart = this.cart.map { CartItemUiState(it.identifier, it.quantity, it.variant) })

fun Product.toProductUiState() = ProductUiState(
    id = this.id,
    imageUrl = this.photo,
    title = this.title,
    price = unitPrice,
)

fun ReviewModel.toReviewUiState() = ReviewUiState(
    id = reviewId,
    averageRating = rate,
    username = reviewUser.name,
    createdDate = createdAt,
    updatedDate = updatedAt,
    review = this.review
)

fun AppCurrencyUiState.toProductCurrencyUiState() =
    ProductCurrencyUiState(code, exchangeRate, id, name, symbol)