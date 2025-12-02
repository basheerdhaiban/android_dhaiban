package com.semicolon.data.repository.remote.model.faq

import kotlinx.serialization.Serializable

@Serializable
data class FaqTypeDto(
    val id: Int?,
    val title: String?
)