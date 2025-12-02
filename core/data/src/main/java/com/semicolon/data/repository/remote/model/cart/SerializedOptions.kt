package com.semicolon.data.repository.remote.model.cart

import kotlinx.serialization.Serializable

@Serializable
data class SerializedOptions(
    val choices: List<Choice>,
    val color: Int?,
    val color_code: String?,
    val color_title:String?,
)