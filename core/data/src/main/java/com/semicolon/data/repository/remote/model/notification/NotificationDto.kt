package com.semicolon.data.repository.remote.model.notification

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    val data: NotificationDataDto?,
    val id: String?,
    @SerializedName("read_at") val readAt: String?,
    val type: String?,
    @SerializedName("user_id") val userId: Int?
)