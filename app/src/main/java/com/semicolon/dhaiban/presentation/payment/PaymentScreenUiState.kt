package com.semicolon.dhaiban.presentation.payment

import androidx.compose.runtime.Immutable
import com.semicolon.dhaiban.presentation.cart.CartAddressUiState
import com.semicolon.dhaiban.presentation.cart.CartItemUiState
import com.semicolon.dhaiban.presentation.wallet.WalletCurrencyUiState
import com.semicolon.dhaiban.utils.PaymentMethodsType
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.PayTabsModel
import com.semicolon.domain.entity.orders.OrderResponseModel

data class PaymentScreenUiState(
    val items: List<CartItemUiState> = emptyList(),
    val cartAddress: CartAddressUiState = CartAddressUiState(),

    val currency: WalletCurrencyUiState = WalletCurrencyUiState(),
    var paymentWay: PaymentMethodsType = PaymentMethodsType.CASH,
    val cashCheckBox:Boolean =false,
    val mobileNumberCheckBox:Boolean =false,
    var isPayTabSelected:Boolean =false,
    val partyCodeCheckBox:Boolean =false,
    val emailCheckBox:Boolean =false,
    var showWallatDialog: Boolean = false,
    var isWalletSelected: Boolean = false,
    var showMomoPayDialog: Boolean = false,
    var isMomoPaySelected: Boolean = false,
    var showPayTabsOptionSelected: Boolean = false,
    var showPayTabsOptionInit: Boolean = false,
    var showPayTabsOptionPaymentWebview: Boolean = false,
    var showPayTabsStatus: Boolean = false,
    var payTabsModel: PayTabsModel? = null,
    val momoWay: String = "",
    val balance: Double = 0.0,
    val orderId: Int = 0,
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
    val status: String="not coming yet",
    val statusCode: Int=0,
    val partyId: String="",
    val partyIdType: String="",
    val key: String="25",
    val amountFromWallet :Double=0.0,
    var isLoading: Boolean = false,
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
    var orderUiState: OrderUiState = OrderUiState(),
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
    var orderId: Int = 0,
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
