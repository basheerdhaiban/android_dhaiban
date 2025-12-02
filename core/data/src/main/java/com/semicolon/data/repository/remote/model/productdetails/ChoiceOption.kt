package com.semicolon.data.repository.remote.model.productdetails

import kotlinx.serialization.Serializable

@Serializable
data class ChoiceOption(
    val name: String?,
    val options: List<Option>?,
    val title: String?
)