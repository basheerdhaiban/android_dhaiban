package com.semicolon.dhaiban.presentation.trackorder

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.orders.DeliveryStatus
import com.semicolon.dhaiban.presentation.orders.OrderProductUiState
import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.domain.entity.RefundReasonModel
import kotlinx.parcelize.Parcelize

@Immutable
@Parcelize

data class TrackOrderScreenUiState(
    val review: String ="",
    val rate: Int =0,
    val rating: Int =0,
    val refundLoading: Boolean = false,
    val orderType: OrderType = OrderType.CURRENT,
    val currency: TrackOrdersCurrencyUiState = TrackOrdersCurrencyUiState(),
    val orderNumber: String = "",
    val orderId: Int = 0,
    val addressId: Int = 0,
    val receiver: String = "",
    val orderDate: String = "",
    val paymentMethod: String = "",
    val paymentStatus: String = "",
    val deliveryStatus: DeliveryStatus = DeliveryStatus.PENDING,
    val totalPrice: Double = 0.0,
    val orderProducts: List<OrderProductUiState> = emptyList(),
    val refundReasons: List<RefundReasonUiState> = emptyList(),
    val orderProductIdToRefund: Int = 0,
    val orderProductId: Int = 0,
    val showRefundItemsDialog: Boolean = false,
    val showRatingDialog: Boolean = false,
    val showReturnProductDialog: Boolean = false,
    val refundErrorMessage: String = ""
) :Parcelable {
    val subTotal: Double = orderProducts.sumOf { it.price * it.quantity }
    val taxes: Double = orderProducts.sumOf { it.tax }
}
@Parcelize
@Immutable
data class TrackOrdersCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
): Parcelable
@Parcelize
@Immutable
data class RefundReasonUiState(
    val reasonId: Int = 0,
    val reason: String = "",
    val selected: Boolean = false
): Parcelable
enum class OldOrNewChat {
    NEW, OLD
}
fun AppCurrencyUiState.toTrackOrderCurrencyUiState() =
    TrackOrdersCurrencyUiState(code, exchangeRate, id, name, symbol)

fun RefundReasonModel.toRefundReasonUiState() =
    RefundReasonUiState(reasonId, reason)

