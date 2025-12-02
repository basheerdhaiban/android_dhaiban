package com.semicolon.dhaiban.presentation.list_of_chat

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import cafe.adriel.voyager.core.model.screenModelScope
import com.semicolon.dhaiban.presentation.base.BaseScreenModel
import com.semicolon.dhaiban.presentation.chat.ChatsState
import com.semicolon.dhaiban.presentation.chat.toChatsScreenState
import com.semicolon.dhaiban.presentation.home.HomeScreenUiEffect
import com.semicolon.dhaiban.presentation.trackorder.OldOrNewChat
import com.semicolon.domain.entity.ChatModel
import com.semicolon.domain.usecase.ChatUseCases
import com.semicolon.domain.usecase.ManageNotificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ListOfChatScreenModel(
    private val oldOrNewChat: OldOrNewChat,
    private val chatUseCases: ChatUseCases,
    private val manageNotificationUseCase: ManageNotificationUseCase
) :
    BaseScreenModel<ListOfChatScreenState, ListOfChatUiEffect>(
        ListOfChatScreenState()
    ), ListOfChatScreenInteractioListener {

    private val _inboxStatestate: MutableStateFlow<PagingData<InboxState>> =
        MutableStateFlow(PagingData.empty())
    val inboxStatestate = _inboxStatestate.asStateFlow()
    val oldOrNewChats = mutableStateOf(oldOrNewChat)
    override val viewModelScope: CoroutineScope = screenModelScope

    init {
        getInboxes()
        getCountOfUnreadNotification()
    }
    private fun getCountOfUnreadNotification() {

        tryToExecute(
            function = { manageNotificationUseCase.getUnReadNotificationCount() },
            onSuccess = {
                if (it != null) {
                    _state.update { screenUiState -> screenUiState.copy(

                        countOfUnreadMessage= it.notifications
                    )  }
                }

            },
            onError = {}
        )
    }
    fun getInboxes() {
        viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true) }
            try {
                chatUseCases.getAllInbox()
                    .cachedIn(viewModelScope)
                    .collectLatest { pagingData ->


                        _inboxStatestate.value = pagingData.map { it.toInboxState() }
                        delay(1000)
                        _state.update { it.copy(isLoading = false) }
                        Log.d("_inboxStatestate", state.value.isLoading.toString())
                    }
            } catch (e: Exception) {
                Log.e("error", e.message.toString())
            }
        }
    }

    override fun onChatTextChange(text: String) {
        updateState { it.copy(message = text) }
    }

    override fun onClickUpButton() {
        sendNewEffect(ListOfChatUiEffect.OnNavigateBack)
    }

    override fun onClickInboxItem(
        inboxId: Int,
        image: String,
        nameOfProduct: String,
        price: Int,
        photoOfVendor: String
    ) {
        sendNewEffect(
            ListOfChatUiEffect.OnNavigateToChatScreen(
                inboxId,
                image,
                nameOfProduct,
                price.toDouble(),
                photoOfVendor
            )
        )
    }

    override fun onClickNotification() {
        sendNewEffect(ListOfChatUiEffect.OnNavigateToNotificationScreen)
    }
}