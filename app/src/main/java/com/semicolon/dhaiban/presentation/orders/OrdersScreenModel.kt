package com.semicolon.dhaiban.presentation.orders

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.ManageOrderUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrdersScreenModel(
    private val orderId: Int,
    private val manageOrderUseCase: ManageOrderUseCase,
    private val manageNotificationUseCase: ManageNotificationUseCase
) : BaseScreenModel<OrdersScreenUiState, OrdersScreenUiEffect>(
    OrdersScreenUiState()
), OrdersScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _previousOrdersState: MutableStateFlow<PagingData<OrderUiState>> =
        MutableStateFlow(value = PagingData.empty())
    val previousOrdersState = _previousOrdersState.asStateFlow()


    init {
        getCurrentOrders()
        getCountOfUnreadNotification()

    }

    private fun getCountOfUnreadNotification() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageNotificationUseCase.getUnReadNotificationCount() },
            onSuccess = {
                if (it != null) {
                    _state.update { screenUiState -> screenUiState.copy(
                        isLoading = false,
                        countOfUnreadMessage= it.notifications
                    )  }
                }

            },
            onError = {}
        )
    }
    private fun getCurrentOrders() {
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { manageOrderUseCase.getCurrentOrders() },
            onSuccess = ::onGetCurrentOrdersSuccess,
            onError = { _state.update { it.copy(isLoading = false) } }
        )
    }

    private fun getPreviousOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                manageOrderUseCase.getPreviousOrders()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _previousOrdersState.value = pagingData.map { it.toCurrentOrderUiState() }
                        delay(1000)
                        _state.update { it.copy(isLoading = false) }
                    }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    private fun onGetCurrentOrdersSuccess(currentOrders: List<OrderModel>) {
        if (orderId != Int.MIN_VALUE)
            onClickTrackOrder(
                currentOrders.find { it.id == orderId }?.toCurrentOrderUiState() ?: OrderUiState(),
                OrderType.CURRENT
            )
        _state.update { uiState ->
            uiState.copy(
                currentOrders = currentOrders.reversed().map { it.toCurrentOrderUiState() },
                isLoading = false,
                gotResponse =true
            )
        }
    }

    override fun onClickUpButton() {
        sendNewEffect(OrdersScreenUiEffect.OnNavigateBack)
    }

    override fun onClickOrderType(orderType: OrderType) {
        _state.update { it.copy(orderType = orderType) }
        if (orderType == OrderType.PREVIOUS)
            getPreviousOrders()
    }

    override fun onClickTrackOrder(orderUiState: OrderUiState, orderType: OrderType) {
        sendNewEffect(OrdersScreenUiEffect.OnNavigateToTrackOrder(orderUiState, orderType))
    }

    override fun onClickNotification() {
        sendNewEffect(OrdersScreenUiEffect.OnNavigateToNotificationScreen)
    }
}