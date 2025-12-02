package com.semicolon.dhaiban.presentation.chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow





class NotificationViewModel : ViewModel() {

    private val _isNewNotification = MutableStateFlow(false)
    val isNewNotification = _isNewNotification.asStateFlow()

    // Function to set the notification flag
    fun setNewNotification(flag: Boolean) {
        _isNewNotification.value = flag
    }
}
