package com.semicolon.dhaiban.presentation.paymentstatus

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.cart.CartAddressUiState
import com.semicolon.dhaiban.presentation.cart.CartItemUiState
import com.semicolon.dhaiban.presentation.wallet.WalletCurrencyUiState
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.orders.OrderResponseModel

data class PaymentStatusScreenUiState(
    val items: List<CartItemUiState> = emptyList(),
    val cartAddress: CartAddressUiState = CartAddressUiState(),

    val currency: WalletCurrencyUiState = WalletCurrencyUiState(),
    val paymentWay: String ="",
    val cashCheckBox:Boolean =false,
    var showWallatDialog: Boolean = false,
    var showMomoPayDialog: Boolean = false,
    val balance: Double = 0.0,
    val paidFromBalance: String = "",
    val numberOfAccountMomoPay: String = "",
    val transactionId: String ="",
    val amount: Int=0,
    val content: String="",
    val currencyOfPayment: String="",
    val externalId: String="",
    val financialTransactionId: String="",
    val payeeNote: String="",
    val payerMessage: String="",
    val status: String="",
    val statusCode: Int=0,
    val partyId: String="",
    val partyIdType: String="",
    val amountFromWallet :Double=0.0,
    val isLoading: Boolean = false,
    val discount: Double =0.0,

    val isLoadingStatusPayment: Boolean = false,
    val subTotal: Double = 0.0,
    val statusPaymentMomo:String="",
    val promoDiscount: Double = 0.0,
    var promoCode:String ="",
    val taxes: Double = 0.0,
    val total: Double = 0.0,
    val currencySymbol: String = "",
    val currencyId: Int = 0,
    val addressId: Int = 0,
    val cartItems: List<CartItem> = emptyList(),
    val orderUiState: OrderUiState = OrderUiState(),
    val paymentResponse: StatusPaymentType = StatusPaymentType.CONFIRM,
    val addCreditState: Boolean = false,
    val expandDebitCards: Boolean = false,
    val debitCardUiState: DebitCardUiState = DebitCardUiState()
)
enum class StatusPaymentType {
    PENDING, CONFIRM,REJECTED
}
@Immutable
data class OrderUiState(
    val message: String = "",
    val orderId: Int = 0,
    val success: Boolean = false
)

data class DebitCardUiState(
    val holderName: String = "",
    val cardNumber: String = "",
    val month: String = "",
    val year: String = "",
    val securityCode: String = "",
    val rememberMyCardInfo: Boolean = false
)

fun OrderResponseModel.toOrderUiState() =
    OrderUiState(
        message, orderId, success
    )
