package com.semicolon.dhaiban.presentation.cart

import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.Immutable
import com.semicolon.data.repository.remote.model.coupon.Product
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.app.StringResources
import com.semicolon.dhaiban.presentation.sharedUiState.AppConfigUiState
import com.semicolon.domain.entity.CartChoice
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.CartProduct
import com.semicolon.domain.entity.CouponProduct
import com.semicolon.domain.entity.ProductOFCoupon
import com.semicolon.domain.entity.SerializedOptionsModel
import com.semicolon.domain.entity.productdetails.DefaultAddress
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.math.round


data class CartScreenUiState(
    var shouldShowCouponToast: Boolean = false,
    val isLoading: Boolean = false,
    var isValidToDiscount: Boolean = false,
    var showToast: Boolean = true,
    var isDiscountLessThan: Boolean = false,
    var notValidAmountForCoupoan: Boolean = false,
    var CountOfProductHaveCoupon :Int=0,
    val isLoadingForCoupon: Boolean = false,
    val layoutDirection: String = "",
    val stringRes: StringResources = StringResources(),
    val appConfig: AppConfigUiState = AppConfigUiState(),
    val items: List<CartItemUiState> = emptyList(),
    val currencyId: Int = 0,
    val currencySymbol: String = "",
    val currency : CartCurrencyUiState = CartCurrencyUiState(),
    val userData: UserDataUiState = UserDataUiState(),
    var promoCode: String = "",
    var momo: String = "",
    var transcationCode: String = "",
    val cartAddress: CartAddressUiState = CartAddressUiState(),
    val errorMessage: String = "",
    var countOfUnreadMessage :Int=0,
    val discount: Double =0.0,
    val discountOfCoupoan: Double =0.0,
    val message :String ="",
    val products: List<ProductUi> =  emptyList(),
    var productsOFCoupon: List<ProductOFCoupon> = listOf()
) {



    val subTotal: Double = items.sumOf { it.afterDiscount * it.quantity }
    val taxes: Double = items.sumOf { it.tax }
    val total: Double = subTotal  +  taxes + cartAddress.shippingCost +cartAddress.shippingTax
}
data class ProductUi(
    val discount: Double,
    val id: Double,
    val price: Double,
    val variation: String
)
data class CartItemUiState(
    val currencyCode: String = "",
    val currencyId: Int = 0,
    val id: Int = 0,
    val identifier: Long = 0,
    val isUpdated: Int = 0,
    val price: Double = 0.0,
    val product: CartProductUiState = CartProductUiState(),
    val quantity: Int = 0,
    val serializedOptions: SerializedOptionsUiState = SerializedOptionsUiState(),
    val tax: Double = 0.0,
    val variant: String = "",
    val discount: Double = 0.0,
    var afterDiscount: Double = 0.0,

    val discountType: String = "",
    val currentStock: Int = 0,

    )

data class SerializedOptionsUiState(
    val choices: List<ChoiceItem> = emptyList(),
    val color: Int = 0,
    val colorId:String="",
    val colorTitle: String=""
)

data class ChoiceItem(
    val id: Int,
    val title: String,
    val parentId: Int,
    val parentTitle: String
)

data class CartProductUiState(
    val advancedProduct: String = "",
    val currentStock: Int = 0,
    val discount: Double = 0.0,
    val discountType: String = "",
    val id: Int = 0,
    val owner: String = "",
    val photo: String = "",
    val sellerId: Int = 0,
    val shortDescription: String = "",
    val title: String = ""
)

@Immutable
data class UserDataUiState(
    val username: String = "",
    val isAuthenticated: Boolean = false
)

@Parcelize
data class CartAddressUiState(
    val addressId: Int = 0,
    val addressName: String = "", val shippingCost: Double=0.0,
    val shippingTax: Double =0.0

) : Parcelable

@Immutable
data class CartCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)
fun DefaultAddress.toCartAddressUiState() = CartAddressUiState(
    addressId = this.id,
    addressName = this.address.ifEmpty { "Make default address" },
    shippingCost=this.shippingCost,
    shippingTax=this.shippingTax
)
fun CouponProduct.toProductUiState() = ProductUi(
discount = discount?:0.0,id?:0.0, price?:0.0, variation?:""
)

fun CartProduct.toCartProductUiState() = CartProductUiState(
    advancedProduct,
    currentStock,
    discount,
    discountType,
    id,
    owner,
    photo,
    sellerId,
    shortDescription,
    title,

)

fun CartItem.toCartItemUiState() = CartItemUiState(
    currencyCode = this.currencyCode,
    currencyId = this.currencyId,
    id = this.id,
    identifier = this.identifier,
    isUpdated = this.isUpdated,
    price = this.price,
    product = this.product.toCartProductUiState(),
    quantity = this.quantity,
    serializedOptions = this.serializedOptions.toSerializedOptionsUiState(),
    tax = this.tax,
    variant = this.variant,
    discountType = this.product.discountType
    , discount = this.product.discount
    , currentStock = this.product.currentStock,
    afterDiscount = this.afterDiscount
)
fun SerializedOptionsModel.toSerializedOptionsUiState() = SerializedOptionsUiState(
    choices.map { ChoiceItem(it.id, it.title, it.parentId, it.parentTitle) }, color,colorCode,colorTitle
)
fun CartItemUiState.toCouponModel() = ProductOFCoupon(
   id= product.id , price = (afterDiscount)*quantity , variation = variant
)


fun CartProductUiState.toCartProduct() =
    CartProduct(
        advancedProduct,
        currentStock,
        discount,
        discountType,
        id,
        owner,
        photo,
        sellerId,
        shortDescription,
        title
    )

fun ChoiceItem.toCartChoice() = CartChoice(
    id, parentId, title, parentTitle
)

fun SerializedOptionsUiState.toEntity() = SerializedOptionsModel(
    choices.map { it.toCartChoice() }, color,colorId,colorTitle
)

fun CartItemUiState.toCartItem() = CartItem(
    currencyCode,
    currencyId,
    id,
    identifier,
    isUpdated,
    price,
    product.toCartProduct(),
    quantity,
    serializedOptions.toEntity(),
    tax, variant,
)

fun AppCurrencyUiState.toCartCurrencyUiState() =
    CartCurrencyUiState(code, exchangeRate, id, name, symbol)
fun CartItemUiState.toCartItemstate() = CartItemstate(
    currencyCode,
    currencyId,
    id,
    identifier,
    isUpdated,
    price,
    product.toCartProductstate(),
    quantity,
    serializedOptions.toEntityState(),
    tax, variant
)
fun SerializedOptionsUiState.toEntityState() = SerializedOptionsModelState(
    choices.map { it.toCartChoicestate() }, color
)
fun ChoiceItem.toCartChoicestate() = CartChoiceState(
    id, parentId, title, parentTitle
)
fun CartProductUiState.toCartProductstate() =
    CartProductState(
        advancedProduct,
        currentStock,
        discount,
        discountType,
        id,
        owner,
        photo,
        sellerId,
        shortDescription,
        title
    )


 class CartItemstate(
    val currencyCode: String,
    val currencyId: Int,
    val id: Int,
    val identifier: Long,
    val isUpdated: Int,
    val price: Double,
    val product: CartProductState,
    val quantity: Int,
    val serializedOptions: SerializedOptionsModelState,
    val tax: Double,
    val variant: String
)

data class CartProductState(
    val advancedProduct: String,
    val currentStock: Int,
    val discount: Double,
    val discountType: String,
    val id: Int,
    val owner: String,
    val photo: String,
    val sellerId: Int,
    val shortDescription: String,
    val title: String
)

data class SerializedOptionsModelState(
    val choices: List<CartChoiceState>,
    val color: Int
)

data class CartChoiceState(
    val id: Int,
    val parentId: Int,
    val title: String,
    val parentTitle:String
)
