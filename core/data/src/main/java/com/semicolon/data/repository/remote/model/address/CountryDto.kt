package com.semicolon.data.repository.remote.model.address

import kotlinx.serialization.Serializable

@Serializable
data class CountryDto(
    val active: Int?,
    val code: String?,
    val id: Int?,
    val logo: String?,
    val title: String?
)