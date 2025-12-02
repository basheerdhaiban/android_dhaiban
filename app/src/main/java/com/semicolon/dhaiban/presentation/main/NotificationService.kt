package com.semicolon.dhaiban.presentation.main

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.semicolon.dhaiban.presentation.chat.NotificationViewModel
import com.semicolon.dhaiban.utils.sendOrdersNotification
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject



class NotificationService : FirebaseMessagingService() {

    private val localConfigurationUseCase by inject<LocalConfigurationUseCase>()
    private val notificationViewModel by inject<NotificationViewModel>() // Inject the ViewModel

    companion object {
        const val MESSAGE = "New message"
        const val PAYMET = "Order payment status"
        const val ORDERDELIVERY = "Order delivery status"
    }

    override fun onNewToken(token: String) {
        println("my token: $token")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                localConfigurationUseCase.saveFcmToken(token)
            } catch (e: Exception) {
                Log.e("Notification Error", e.message.toString())
            }
        }
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        println("my message ${message.data}")
        println("my message ${message.data["body"]}")
        message.notification?.let {
            val messageBody = it.body
            val messageTitle = it.title

            // Notify the ViewModel of a new message for the chat screen
            if (messageTitle == MESSAGE) {
                val jsonString = Gson().toJson(message.data)
                val result = Gson().fromJson(jsonString, MessageStatus::class.java)
                Log.d("message", "message")

                notificationViewModel.setNewNotification(true) // Set flag in ViewModel for new notification

                sendOrdersNotification(
                    result.message, messageTitle, this,
                    result.inbox_id, "chat"
                )
            } else if (messageTitle == ORDERDELIVERY || messageTitle == PAYMET) {
                val jsonString = Gson().toJson(message.data)
                val result = Gson().fromJson(jsonString, OrderStatus::class.java)
                Log.d("ORDERDELIVERY", result.order_id.toString())
                sendOrdersNotification(
                    messageBody.toString(), message.data["title"] ?: "", this, result.order_id,
                    typeOfNotification = "order"
                )
            } else {
                val jsonString = Gson().toJson(message.data)
                val result = Gson().fromJson(jsonString, OrderStatus::class.java)
                sendOrdersNotification(
                    messageBody.toString(), message.data["title"] ?: "", this, result.order_id,
                    typeOfNotification = "refund"
                )
            }
        }
        super.onMessageReceived(message)
    }
}
