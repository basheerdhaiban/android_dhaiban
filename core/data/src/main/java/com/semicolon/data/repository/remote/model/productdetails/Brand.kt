package com.semicolon.data.repository.remote.model.productdetails

import kotlinx.serialization.Serializable

@Serializable
data class Brand(
    val logo: String?,
    val name: String?
)