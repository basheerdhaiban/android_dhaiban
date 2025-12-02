package com.semicolon.dhaiban.presentation.orders

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.semicolon.domain.entity.SerializedOptionsModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.entity.orders.OrderProductModel
import kotlinx.parcelize.Parcelize

data class OrdersScreenUiState(
    val isLoading: Boolean = false,
    val gotResponse: Boolean = false,
    val orderType: OrderType = OrderType.CURRENT,
    val currentOrders: List<OrderUiState> = listOf() ,val countOfUnreadMessage:Int =0
)
data class OrderScreenUiState(
    val isLoading: Boolean = false,
    val orderType: OrderType = OrderType.CURRENT,
    val currentOrder: OrderUiState =OrderUiState()
)

enum class OrderType {
    CURRENT, PREVIOUS
}
@Immutable
@Parcelize
enum class DeliveryStatus : Parcelable {
    PENDING, ON_REVIEW, READY_TO_DELIVER, ON_DELIVERY, DELIVERED, NOT_FOUND
}

@Immutable
@Parcelize
data class OrderUiState(
    val id: Int = 0,
    val orderNumber: String = "",
    val receiver: String = "",
    val orderDate: String = "",
    val paymentMethod: String = "",
    val paymentStatus: String = "",
    val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,
    val totalPrice: Double = 0.0,
    val orderProducts: List<OrderProductUiState> = emptyList(),
    val addressId: Int = 0
) : Parcelable

@Immutable
@Parcelize
data class OrderProductUiState(
    val advancedProduct: String = "",
    val couponDiscount: Int = 0,
    val id: Int = 0,
    val photo: String = "",
    val price: Double = 0.0,
    val productId: Int = 0,
    val productRefundable: Boolean = false,
    val productShortDesc: String = "",
    val productTitle: String = "",
    val quantity: Int = 0,
    val tax: Double = 0.0,
    val rated: Int =0,
    val productOwner :String="",
    val productOwnerId :String="",
    val productImg :String="",
    val serializedOptionsUiState: SerializedOptionsUiState = SerializedOptionsUiState()
) : Parcelable


@Immutable
@Parcelize
data class SerializedOptionsUiState(
    val choices: List<ChoiceItem> = emptyList(),
    val color: Int = 0,
    val colorCode :String="",
    val colorTitle:String=""
) : Parcelable

@Immutable
@Parcelize
data class ChoiceItem(
    val id: Int,
    val title: String,
    val parentId: Int,
    val parentTitle: String
) : Parcelable

fun SerializedOptionsModel.toSerializedOptionsUiState() =
    SerializedOptionsUiState(
        choices.map {
            ChoiceItem(
                it.id,
                it.title,
                it.parentId,
                it.parentTitle
            )
        }, color,colorCode,colorTitle
    )

fun OrderProductModel.toOrderProductUiState() =
    OrderProductUiState(
        advancedProduct,
        couponDiscount,
        id,
        photo,
        price,
        productId,
        productRefundable,
        productShortDesc,
        productTitle,
        quantity,
        tax,
        rated = rated,
        productOwner,
        productOwnerId.toString(),
        productOwnerImg?:"",
        serializedOptions.toSerializedOptionsUiState(),
    )

fun OrderModel.toCurrentOrderUiState() =
    OrderUiState(
        id = this.id,
        orderNumber = this.code,
        receiver = this.user,
        orderDate = this.date,
        paymentMethod = this.paymentType,
        paymentStatus = if (this.paymentStatus == 0) "Unpaid" else "Paid",
        deliveryStatus = when (this.deliveryStatus) {
            "pending" -> DeliveryStatus.PENDING
            "on_review" -> DeliveryStatus.ON_REVIEW
            "ready_to_delever" -> DeliveryStatus.READY_TO_DELIVER
            "on_delivery" -> DeliveryStatus.ON_DELIVERY
            else -> DeliveryStatus.DELIVERED
        },
        totalPrice = this.totalPrice,
        orderProducts = this.orderProducts.map { it.toOrderProductUiState() },
        addressId = address.id
    )