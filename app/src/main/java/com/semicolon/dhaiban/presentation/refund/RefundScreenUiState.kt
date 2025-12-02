package com.semicolon.dhaiban.presentation.refund

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.domain.entity.RefundModel
import com.semicolon.domain.entity.RefundProductModel
import kotlinx.parcelize.Parcelize

data class RefundScreenUiState(
    val isLoading: Boolean = false,
    val requestType: RequestType = RequestType.CURRENT,
    val currentRefundItems: List<RefundItemUiState> = emptyList(),
    val previousRefundItems: List<RefundItemUiState> = emptyList(),
    val currency: RefundCurrencyUiState = RefundCurrencyUiState(),
    val countOfUnreadMessage:Int =0
)

@Immutable
data class RefundCurrencyUiState(
    val code: String = "",
    val exchangeRate: Double = 1.0,
    val id: Int = 0,
    val name: String = "",
    val symbol: String = ""
)

@Parcelize
data class RefundItemUiState(
    val id: Int = 0,
    val orderNumber: String = "",
    val receiver: String = "",
    val orderDate: String = "",
    val paymentMethod: String = "",
//    val paymentStatus: String = "",
    val product: RefundProductUiState = RefundProductUiState(),
    val refundStatus: RefundStatus = RefundStatus.PENDING,
    val totalPrice: Double = 0.0,
    val refundProduct: RefundProduct = RefundProduct(),
    val addressId: Int = 0
) : Parcelable

@Parcelize
data class RefundProduct(
    val productId: Int = 0,
    val title: String = "",
    val image: String = ""
) : Parcelable

@Parcelize
data class RefundProductUiState(
    val id: Int = 0,
    val image: String = "",
    val title: String = ""
) : Parcelable

enum class RequestType {
    CURRENT, PREVIOUS
}

enum class RefundStatus {
    PENDING, ACCEPTED, DECLINED, NOT_FOUND
}

fun RefundProductModel.toRefundProductUiState() =
    RefundProductUiState(id = this.id, image = this.photo, title = this.title)

fun RefundModel.toRefundItemUiState() = RefundItemUiState(
    id = this.id,
    orderNumber = this.code,
    receiver = this.user.name,
    orderDate = this.createdAt,
    paymentMethod = this.paymentType,
    refundStatus = when (this.status.lowercase()) {
        "under_review" -> RefundStatus.PENDING
        "approved" -> RefundStatus.ACCEPTED
        else -> RefundStatus.DECLINED
    },
    totalPrice = this.refundAmount,
    product = this.product.toRefundProductUiState()
)

fun AppCurrencyUiState.toRefundCurrencyUiState() =
    RefundCurrencyUiState(code, exchangeRate, id, name, symbol)