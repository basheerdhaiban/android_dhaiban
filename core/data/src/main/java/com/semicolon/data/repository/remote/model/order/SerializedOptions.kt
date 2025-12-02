package com.semicolon.data.repository.remote.model.order

import kotlinx.serialization.Serializable

@Serializable
data class SerializedOptions(
    val choices: List<Choice>,
    val color: Int
)