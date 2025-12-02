package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.count_of_unreadnotification.Data
import com.semicolon.data.repository.remote.model.ChangePasswordUserDto
import com.semicolon.domain.entity.ChangePasswordUser
import com.semicolon.domain.entity.UnreadOfNotifcationModel

fun Data.toUnreadOfNotifcationModel(): UnreadOfNotifcationModel {
    return UnreadOfNotifcationModel(notifications =notifications)
    }
