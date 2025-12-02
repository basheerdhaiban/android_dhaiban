package com.semicolon.data.repository.remote.model

import com.semicolon.domain.entity.Account
import kotlinx.serialization.Serializable

@Serializable
data class UserRegistrationDto(
    val username: String,
    val phone: String,
    val email: String,
    val password: String,
)

fun Account.toUserRegistrationDto(): UserRegistrationDto {
    return UserRegistrationDto(
        username = username,
        phone = phone,
        email = email,
        password = password
    )
}