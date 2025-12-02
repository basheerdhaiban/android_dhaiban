package com.semicolon.data.repository.remote.model.cart

import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    val id: Int?,
    val parentId: Int?,
    val title: String?,
    val parentTitle:String?
)