package com.semicolon.domain.entity

data class Account(
    val username: String,
    val email: String,
    val phone: String,
    val password: String,
    val verificationMethod: Int,
)
