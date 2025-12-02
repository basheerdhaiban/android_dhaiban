package com.semicolon.dhaiban.presentation.notification

import android.app.Notification
import android.util.Log
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.authuntication.signUp.SignUpScreenUiEffect
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.orders.OrderType
import com.semicolon.dhaiban.presentation.orders.OrderUiState
import com.semicolon.dhaiban.presentation.orders.OrdersScreenUiEffect
import com.semicolon.domain.usecase.ManageNotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationScreenModel(private val manageNotificationUseCase: ManageNotificationUseCase) :
    BaseScreenModel<NotificationScreenUiState, NotificationScreenUiEffect>(
        NotificationScreenUiState()
    ), NotificationScreenInteractionListener {
    override val viewModelScope: CoroutineScope = screenModelScope

    private val _userNotificationsState: MutableStateFlow<PagingData<NotificationUiState>> =
        MutableStateFlow(PagingData.empty())
    val userNotificationsState = _userNotificationsState.asStateFlow()
    private val _userUnReadNotificationsState: MutableStateFlow<PagingData<NotificationUiState>> =
        MutableStateFlow(PagingData.empty())
     val userUnReadNotificationsState= _userUnReadNotificationsState.asStateFlow()

    init {
        getUserNotifications()
        getUnReadUserNotifications()

    }

    private fun getUserNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                manageNotificationUseCase.getNotifications()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _userNotificationsState.value =
                            pagingData.map { it.toNotificationUiState() }
                        delay(1000)
                        _state.update { it.copy(isLoading = false) }
                    }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    private fun getUnReadUserNotifications() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                manageNotificationUseCase.getUnReadNotification()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _userUnReadNotificationsState.value =
                            pagingData.map { it.toNotificationUiState() }
                        delay(1000)
                        _state.update { it.copy(isLoading = false) }
                    }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    override fun updateRefundList(refundList: List<NotificationUiState>) {
        _state.update { it.copy(refundNotifications = refundList) }
    }

    override fun onClickTrackOrder(orderUiState: OrderUiState, orderType: OrderType ,id :Int,notificationID: String) {
        markNotificationAsRead(notificationID)
        sendNewEffect(NotificationScreenUiEffect.OnNavigateToTrackOrder(orderUiState, orderType, id) )
        Log.d(":NotificationScreenModel" ,"onClickTrackOrder")

    }
    override fun onClickMessage(orderUiState: OrderUiState, orderType: OrderType ,id :Int,notificationID: String) {
        markNotificationAsRead(notificationID)
        sendNewEffect(NotificationScreenUiEffect.OnNavigateToMessage( id) )
        Log.d(":NotificationScreenModel" ,"onClickTrackOrder")

    }
    override fun onClickPayment(orderUiState: OrderUiState, orderType: OrderType ,id :Int,notificationID: String) {
        markNotificationAsRead(notificationID)
        sendNewEffect(NotificationScreenUiEffect.OnNavigateToTrackOrder(orderUiState, orderType, id) )
        Log.d(":NotificationScreenModel" ,"onClickTrackOrder")

    }

    override fun onClickRefund(orderUiState: OrderUiState, orderType: OrderType ,notificationID: String ,refundID:Int) {
        markNotificationAsRead(notificationID)
        sendNewEffect(NotificationScreenUiEffect.OnNavigateTorRefund(orderUiState, orderType ,refundID))
    }

    override fun onClickUpButton() {
        sendNewEffect(NotificationScreenUiEffect.OnNavigateBack)
    }

    override fun onClickNotificationType(notificationType: NotificationType) {
        _state.update { it.copy(notificationType = notificationType) }
    }

    private fun markNotificationAsRead(notificationID:String ) {

        viewModelScope.launch(Dispatchers.IO) {


            tryToExecute(
                function = { manageNotificationUseCase.markNotificationsAsRead(notificationID) },
                onSuccess = { },
                onError = { }
            )
        }
        updateState { it.copy(isLoading = false) }

    }

}