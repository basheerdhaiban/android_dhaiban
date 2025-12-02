package com.semicolon.data.repository.remote.model.order

import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val id: Int?,
    val parentId: Int?,
    val parentTitle: String?,
    val title: String?
)