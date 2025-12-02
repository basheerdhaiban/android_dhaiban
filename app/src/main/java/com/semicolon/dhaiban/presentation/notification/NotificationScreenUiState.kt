package com.semicolon.dhaiban.presentation.notification

import com.semicolon.dhaiban.presentation.orders.DeliveryStatus
import com.semicolon.dhaiban.presentation.refund.RefundStatus
import com.semicolon.domain.entity.notification.NotificationDataModel
import com.semicolon.domain.entity.notification.NotificationModel

data class NotificationScreenUiState(
    val isLoading: Boolean = false,
    val notificationType: NotificationType = NotificationType.ORDER,
    val refundNotifications: List<NotificationUiState> = emptyList()
)

data class NotificationUiState(
    val readAt: String="",
    val id: String = "",
    val type: NotificationType = NotificationType.ORDER,
    val userId: Int = 0,
    val notificationData: NotificationDataUiState = NotificationDataUiState()
)

data class NotificationDataUiState(
    val title: String = "",
    val orderId: Int = 0,
    val deliveryStatus: DeliveryStatus = DeliveryStatus.ON_DELIVERY,
    val refundStatus: RefundStatus = RefundStatus.PENDING,
    val message: String = "",
    val readAt: String = "",
    val messageFrom: String="",
    val productID: Int=0,
    val refundId: Int=0,
    val inboxID: Int=0,
)

enum class NotificationType {
    ORDER, PAYMENT,MESSAGE,REFUND
}

fun NotificationDataModel.toNotificationDataUiState() = NotificationDataUiState(
    messageFrom=this.messageFrom,
    productID = this.productID,
    refundId = this.refundId,
    title = this.title,
    message = this.message,
    inboxID = this.inboxId,
    orderId = this.orderId,
    deliveryStatus = when (this.deliveryStatus) {
        "pending" -> DeliveryStatus.PENDING
        "on_review" -> DeliveryStatus.ON_REVIEW
        "ready_to_delever" -> DeliveryStatus.READY_TO_DELIVER
        "on_delivery" -> DeliveryStatus.ON_DELIVERY
        "" -> DeliveryStatus.NOT_FOUND
        else -> DeliveryStatus.DELIVERED
    },
    refundStatus = when (this.refundStatus.lowercase()) {
        "under_review" -> RefundStatus.PENDING
        "approved" -> RefundStatus.ACCEPTED
        "" -> RefundStatus.NOT_FOUND
        else -> RefundStatus.DECLINED
    }
)

fun NotificationModel.toNotificationUiState() = NotificationUiState(
    id = this.id,
    type = when (this.type) {
        "App\\Notifications\\ConversationNotification" -> NotificationType.MESSAGE
        "App\\Notifications\\OrderDeleveryStatusNoticication" -> NotificationType.ORDER

        "App\\Notifications\\RefundStatusNoticication" -> NotificationType.REFUND

        else -> NotificationType.PAYMENT
    },
    userId = this.userId,
    notificationData = this.notificationData.toNotificationDataUiState(),
    readAt = this.readAt
)