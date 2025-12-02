package com.semicolon.dhaiban.presentation.trackorder

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.orders.OrderScreenUiState
import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.orders.OrderUiState
import com.semicolon.dhaiban.presentation.orders.toCurrentOrderUiState
import com.semicolon.dhaiban.presentation.trackorder.composables.RefundError
import com.semicolon.domain.entity.RefundReasonModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.usecase.ManageOrderUseCase
import com.semicolon.domain.usecase.ManageRefundUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TrackOrderScreenModel(

    private val orderType: OrderType,
    private val manageOrderUseCase: ManageOrderUseCase,
    val orderId: Int,
    private val manageRefundUseCase: ManageRefundUseCase
) :
    BaseScreenModel<TrackOrderScreenUiState, TrackOrderScreenUiEffect>(
        TrackOrderScreenUiState()
    ), TrackOrderScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope
    private val _order: MutableStateFlow<OrderScreenUiState> =
        MutableStateFlow(OrderScreenUiState())
    val orderWithId = _order.asStateFlow()

    init {
        if (orderId != Int.MIN_VALUE) {
            getOrderWithID(orderId)
        }
//      else {
//            _state.update {
//                it.copy(
//                    orderType = orderType,
//                    orderNumber = order.orderNumber,
//                    orderId = order.id,
//                    receiver = order.receiver,
//                    orderDate = order.orderDate,
//                    paymentMethod = order.paymentMethod,
//                    paymentStatus = order.paymentStatus,
//                    deliveryStatus = order.deliveryStatus,
//                    orderProducts = order.orderProducts,
//                    totalPrice = order.totalPrice,
//                    addressId = order.addressId
//                )
//            }
//      }
        Log.d("TrackOrderScreenModelId", orderId.toString())

    }

    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toTrackOrderCurrencyUiState()) }
    }

    private fun getRefundReasons() {
        _state.update { it.copy(refundLoading = true) }
        tryToExecute(
            function = { manageRefundUseCase.getRefundReasons() },
            onSuccess = ::onGetRefundReasonsSuccess,
            onError = { _state.update { it.copy(refundLoading = false) } }
        )
    }

     fun SetReview(
        orderId: Int,
        productId: Int,
        rate: Int,
        review: String
    ) {

        tryToExecute(
            function = {
                manageOrderUseCase.setRateOrder(orderId, productId, rate, review)
            },
            onSuccess = {},
            onError = { exception ->

                Log.e("OrderError", exception.message.toString())
            }
        )
    }

    private fun onGetRefundReasonsSuccess(reasons: List<RefundReasonModel>) {
        _state.update { uiState ->
            uiState.copy(
                refundReasons = reasons.map { it.toRefundReasonUiState() },
                refundLoading = false
            )
        }
    }

    private fun fillStateInCaseOrderWithId() {
        _state.update {
            it.copy(
                orderType = _order.value.orderType,
                orderNumber = _order.value.currentOrder.orderNumber,
                orderId = _order.value.currentOrder.id,
                receiver = _order.value.currentOrder.receiver,
                orderDate = _order.value.currentOrder.orderDate,
                paymentMethod = _order.value.currentOrder.paymentMethod,
                paymentStatus = _order.value.currentOrder.paymentStatus,
                deliveryStatus = _order.value.currentOrder.deliveryStatus,
                orderProducts = _order.value.currentOrder.orderProducts,
                totalPrice = _order.value.currentOrder.totalPrice,
                addressId = _order.value.currentOrder.addressId
            )
        }
    }

    private fun makeRefundRequest(
        addressId: Int,
        orderId: Int,
        orderProductId: Int,
        reasonId: Int,
        customerReason: String
    ) {
        Log.d("makeRefundRequest", orderProductId.toString())
        _state.update { it.copy(refundLoading = true) }
        tryToExecute(
            function = {
                manageRefundUseCase.makeRefundRequest(
                    addressId,
                    orderId,
                    orderProductId,
                    reasonId,
                    customerReason
                )
            },
            onSuccess = ::onMakeRefundRequestSuccess,
            onError = { _state.update { it.copy(refundLoading = false) } }
        )
    }

    private fun onMakeRefundRequestSuccess(success: Boolean) {
        if (success) {
            _state.update { uiState ->
                uiState.copy(
                    refundLoading = false,
                    showReturnProductDialog = false,
                    orderProducts = uiState.orderProducts.map {
                        if (it.productId == state.value.orderProductIdToRefund)
                            it.copy(productRefundable = false)
                        else it
                    })
            }
            sendNewEffect(TrackOrderScreenUiEffect.OnNavigateBack)
        }
    }

    override fun onReviewTextChange(text: String) {
        updateState { it.copy(review = text) }
    }

    override fun onRateChange(rate: Float) {
        updateState { it.copy(rate = rate.toInt()) }    }

    override fun onClickUpButton() {
        sendNewEffect(TrackOrderScreenUiEffect.OnNavigateBack)

    }

    override fun onClickReturnProduct(orderProductId: Int) {
        _state.update {
            it.copy(
                showRefundItemsDialog = true,
                orderProductIdToRefund = orderProductId
            )
        }
    }

    override fun onClickReview(orderProductId: Int) {


        _state.update {
            it.copy(
                showRatingDialog = true,
                orderProductIdToRefund = orderProductId
            )
        }
    }

    private fun getOrderWithID(id: Int) {
        _order.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageOrderUseCase.getOrderWithID(id) },
            onSuccess = ::onGetCurrentOrdersSuccess,
            onError = { _order.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetCurrentOrdersSuccess(currentOrders: List<OrderModel>) {
        _order.update { homeScreenUiState ->
            OrderScreenUiState(
                isLoading = false,
                orderType = OrderType.CURRENT,
                currentOrder = currentOrders[0].toCurrentOrderUiState()
            )
        }
        Log.d("onGetCurrentOrdersSuccess", "${_order.value.currentOrder.totalPrice}")
        fillStateInCaseOrderWithId()

    }

    override fun onDismissRefundItemsBottomSheet() {
        _state.update { it.copy(showRefundItemsDialog = false) }
    }

    override fun onDismissReviewDailog() {
        _state.update { it.copy(showRatingDialog = false) }
    }

    override fun onSelectRefundReason(optionId: Int) {
        _state.update { uiState ->
            uiState.copy(refundReasons = _state.value.refundReasons.map {
                if (it.reasonId == optionId) it.copy(selected = true)
                else it.copy(selected = false)
            })
        }
    }

    override fun onClickNext() {
        if (_state.value.refundReasons.isEmpty())
            getRefundReasons()
        _state.update { it.copy(showRefundItemsDialog = false, showReturnProductDialog = true) }
    }

    override fun onDismissReturnProductBottomSheet() {
        _state.update { it.copy(showReturnProductDialog = false) }
    }

    override fun onRefundError(refundError: RefundError) {
        _state.update { it.copy(refundErrorMessage = refundError.errorMessage) }
    }

    override fun onClickSend(selectedReasonId: Int, userComment: String) {
        makeRefundRequest(
            addressId = _state.value.addressId,
            orderId = _state.value.orderId,
            orderProductId = _state.value.orderProductIdToRefund,
            reasonId = selectedReasonId,
            customerReason = userComment
        )
    }

    override fun onDismissBottomSheet() {
        _state.update {
            it.copy(
                showReturnProductDialog = false,
                showRefundItemsDialog = false,
                showRatingDialog = false
            )
        }
    }

    override fun onClickNotification() {
        sendNewEffect(TrackOrderScreenUiEffect.OnNavigateToNotificationScreen)
    }

    override fun onClickChat(item:Int) {
        sendNewEffect(TrackOrderScreenUiEffect.OnNavigateToChatScreen(item))

    }
}