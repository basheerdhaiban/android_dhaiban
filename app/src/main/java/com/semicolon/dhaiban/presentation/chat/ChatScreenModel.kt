package com.semicolon.dhaiban.presentation.chat

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.app.AppCurrencyUiState
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.cart.toCartCurrencyUiState
import com.semicolon.dhaiban.presentation.trackorder.OldOrNewChat
import com.semicolon.domain.entity.ProductXModel
import com.semicolon.domain.usecase.ChatUseCases
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class  ChatScreenModel(
    private val oldOrNewChat: OldOrNewChat,
    private val productId: Int,
    private val orderId: Int,
    private val productOwnerId: String,
    private val inboxID: Int,
    private val chatUseCases: ChatUseCases,

    ) :
    BaseScreenModel<ChatScreenState, ChatUiEffect>(
        ChatScreenState()
    ), ChatScreenInteractioListener {


    override val viewModelScope: CoroutineScope = screenModelScope

    private val _messagestate: MutableStateFlow<PagingData<ChatsState>> =
        MutableStateFlow(PagingData.empty())
    val messageState = _messagestate.asStateFlow()
    private val _notificationMessage = MutableStateFlow<String?>(null)
    val notificationMessage = _notificationMessage.asStateFlow()
    init {
        Log.d("ChatScreenModelownerId", productOwnerId.toString())
        if (oldOrNewChat == OldOrNewChat.NEW) {
            sendNewMessage(productId = productId, orderId = orderId, user = productOwnerId.toInt())
        } else {
            getInboxById(id = inboxID)
            getMetaDataMessage(inboxID)
        }

    }

    fun getInboxById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoadingForIntial = true) }
            try {
                chatUseCases.getInboxById(id)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->


                        _messagestate.value = pagingData.map { it.toChatsScreenState() }
                        pagingData.map {
                            _state.update { uiState ->
                                uiState.copy(userType = it.userType.toString())


                            }
                        }

                        delay(1000)
                        _state.update { it.copy(isLoadingForIntial = false) }
                        Log.d("_inboxStatestate", state.value.isLoading.toString())
                    }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    fun sendNewMessage(productId: Int, orderId: Int, user: Int, message: String = "") {


        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                chatUseCases.sendNewMessage(
                    productId = productId,
                    orderId = orderId,
                    user = user,
                    message = message
                )
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _messagestate.value = pagingData.map { it.toChatsScreenState() }
                        delay(1000)
                        _state.update { it.copy(isLoading = false) }
                    }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    fun sendMessage(inboxID: Int, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                chatUseCases.sendMessage(inboxID, message = message)
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->
                        _messagestate.value = pagingData.map { it.toChatsScreenState() }
                        delay(1000)
                        _state.update { it.copy(isLoading = false) }
                    }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    fun getMetaDataMessage(inboxID: Int) {
        Log.d("sendNewMessage", "sendNewMessage")
        _state.update { it.copy(isLoading = true) }
        tryToExecute(
            function = { chatUseCases.getMetaDataInboxById(inboxID) },
            onSuccess = ::onGetAllMessageSuccess,
            onError = { Log.d("onErrorChatScreenModel", it.message.toString()) }
        )
    }


    private fun onGetAllMessageSuccess(chatModel: ProductXModel) {

        Log.d("onGetAllMessageSuccess", chatModel.title)
        Log.d("onGetAllMessageSuccess", chatModel.unit_price.toString())

        _state.update { uiState ->
            uiState.copy(
                isLoading = false,
                imageSeller = chatModel.imageSeller,
                nameOfSeller = chatModel.nameSeller,
                titleOfProduct = chatModel.title,
                productImg = chatModel.photo,
                descriptionOfProduct = chatModel.short_desc,
                price = chatModel.unit_price.toDouble()
            )
        }
        _state.value.chats.get(1).userType?.let { Log.d("onGetAllMessageSuccess", it) }


    }
    fun updateCurrencyUiState(appCurrencyUiState: AppCurrencyUiState) {
        _state.update { it.copy(currency = appCurrencyUiState.toCartCurrencyUiState()) }
    }
    override fun onChatTextChange(text: String) {
        updateState { it.copy(message = text) }
    }

    override fun onClickUpButton() {
        sendNewEffect(ChatUiEffect.OnNavigateBack)
    }

    override fun onClickNotification() {
        sendNewEffect(ChatUiEffect.OnNavigateToNotificationScreen)
    }


}