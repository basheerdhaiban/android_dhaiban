package com.semicolon.dhaiban.presentation.trackrefund

import android.util.Log
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.orders.OrderScreenUiState
import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.orders.toCurrentOrderUiState
import com.semicolon.dhaiban.presentation.refund.RefundItemUiState
import com.semicolon.dhaiban.presentation.refund.RefundStatus
import com.semicolon.dhaiban.presentation.refund.toRefundItemUiState
import com.semicolon.dhaiban.presentation.refund.toRefundProductUiState
import com.semicolon.domain.entity.RefundModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.usecase.ManageRefundUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.update

class TrackRefundScreenModel(private val refundItem: RefundItemUiState, private val manageRefundUseCase: ManageRefundUseCase ,private val id:Int) :
    BaseScreenModel<TrackRefundUiState, TrackRefundUiEffect>(
        TrackRefundUiState()
    ), TrackRefundScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getOrderWithID(id)

//        _state.update {
//            it.copy(
//                refundStatus = refundItem.refundStatus,
//                orderNumber = refundItem.orderNumber,
//                receiver = refundItem.receiver,
//                orderDate = refundItem.orderDate,
//                totalPrice = refundItem.totalPrice,
//                refundProduct = refundItem.product
//            )
//        }


    }

    override fun onClickUpButton() {
        sendNewEffect(TrackRefundUiEffect.OnNavigateBack)
    }
    private fun getOrderWithID(id: Int) {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageRefundUseCase.getRefundById(id) },
            onSuccess = ::onGetCurrentOrdersSuccess,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun onGetCurrentOrdersSuccess(refund:RefundModel) {
        _state.update {
            it.copy(
                refundStatus =  when (refund.status.lowercase()) {
                    "under_review" -> RefundStatus.PENDING
                    "approved" -> RefundStatus.ACCEPTED
                    else -> RefundStatus.DECLINED
                },
                orderNumber = refund.code,
                receiver = refund.user.name,
                orderDate = refund.createdAt,
                totalPrice = refund.refundAmount,
                refundProduct = refund.product.toRefundProductUiState()
            )
        }


    }
    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toTrackRefundCurrencyUiState()) }
    }

    override fun onClickNotification() {
        sendNewEffect(TrackRefundUiEffect.OnNavigateToNotificationScreen)
    }
}