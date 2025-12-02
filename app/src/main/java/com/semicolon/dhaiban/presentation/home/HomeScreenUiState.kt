package com.semicolon.dhaiban.presentation.home

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.cart.CartAddressUiState
import com.semicolon.dhaiban.presentation.cart.CartItemUiState
import com.semicolon.dhaiban.presentation.paymentstatus.OrderUiState
import com.semicolon.dhaiban.presentation.sharedUiState.SliderItemType
import com.semicolon.domain.entity.Brand
import com.semicolon.domain.entity.Category
import com.semicolon.domain.entity.Product
import com.semicolon.domain.entity.ProductType
import com.semicolon.domain.entity.SliderItem

@Immutable
data class HomeScreenUiState(
    val _isRefreshing:Boolean =false,
    val isLoading: Boolean = false,
    val isNewProductsLoading: Boolean = false,
    val isGroupProductsLoading: Boolean = false,
    val needsUpdate: Boolean = false,
    val orderUiState: OrderUiState = OrderUiState(),
    val categories: List<CategoryUiState> = emptyList(),
    val sliderItems: List<SliderUiState> = emptyList(),
    val userData: UserDataUiState = UserDataUiState(),
    val brands: List<BrandUiState> = emptyList(),
    val saleProducts: List<SalesUiState> = emptyList(),
    val productTypes: List<ProductTypeUiState> = emptyList(),
    val newProducts: List<ProductUiState> = emptyList(),
    val selectedList: List<ProductUiState> = emptyList(),
    val searchList: List<ProductUiState> = emptyList(),
    val currencySymbol: String = "",
    val homeCurrencyUiState: HomeCurrencyUiState = HomeCurrencyUiState(),
    val exchangeRate: Double = 0.0,
    val errorMessage: String = "",
    val queryValue: String = "",
    val refreshState: Boolean = false,
    val showExitDialog : Boolean = false,
    val statusPaymentMomo:String="",
    val items: List<CartItemUiState> = emptyList(),
    val transactionId: String ="",
    val amount: Int=0,
    val content: String="",
    val currencyOfPayment: String="",
    val externalId: String="",
    val financialTransactionId: String="",
    val payeeNote: String="",
    val payerMessage: String="",
    val status: String="not coming yet",
    val statusCode: Int=0,
    val partyId: String="",
    val partyIdType: String="",
    val country: String="",
    val discount: Double =0.0,
    val cartAddress: CartAddressUiState = CartAddressUiState(),
)

@Immutable
data class CategoryUiState(
    val id: Int = 0,
    val title: String = "",
    val image: String = ""
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
data class BrandUiState(
    val id: Int = 0,
    val title: String = "",
    val logo: String = ""
)

@Immutable
data class HomeCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)

data class SalesUiState(
    val id: Int = 0,
    val imageUrl: String = "",
    val isFavourite: Boolean = false,
    val title: String = "",
    val description: String,
    val price: Double = 0.0,
    val afterDiscount: Double = 0.0,
)

@Immutable
data class ProductTypeUiState(
    val id: Int = 0,
    val title: String = ""
)
@Immutable
data class OrderUiState(
    val message: String = "",
    val orderId: Int = 0,
    val success: Boolean = false
)
@Immutable
data class UserDataUiState(
    val username: String = "",
    val imageUrl: String = "",
    val isAuthenticated: Boolean = false,
    val country: Int = 0,

    )

data class ProductUiState(
    val id: Int = 0,
    val imageUrl: String = "",
    val isFavourite: Boolean = false,
    val title: String = "",
    val price: Double = 0.0,
    val afterDiscount: Double = 0.0,
) {
    fun doesMatchSearchQuery(query: String): Boolean {
        val matchingCombinations = listOf(
            title,
        )
        return matchingCombinations.any {
            it.startsWith(query, ignoreCase = true) || it.contains(query, ignoreCase = true)
        }
    }
}

fun Category.toEntity() =
    CategoryUiState(
        id = this.id,
        title = this.title,
        image = imageUrl
    )

fun Brand.toEntity() =
    BrandUiState(
        id = this.id,
        title = this.title,
        logo = logoUrl
    )

fun SliderItem.toEntity() =
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

fun Product.toEntity() =
    SalesUiState(
        id = this.id,
        imageUrl = this.photo,
        isFavourite = userFavorite,
        title = this.title,
        description = shortDescription,
        price = unitPrice,
        afterDiscount = this.afterDiscount
    )

fun Product.toProductUiState() =
    ProductUiState(
        id = this.id,
        imageUrl = this.photo,
        isFavourite = userFavorite,
        title = this.title,
        price = unitPrice,
        afterDiscount = this.afterDiscount
    )

fun ProductType.toProductTypeUiState() =
    ProductTypeUiState(
        id = this.id,
        title = this.title
    )

fun AppCurrencyUiState.toEntity() =
    HomeCurrencyUiState(code, exchangeRate, id, name, symbol)