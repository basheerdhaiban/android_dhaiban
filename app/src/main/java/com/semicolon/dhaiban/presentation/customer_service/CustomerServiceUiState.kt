package com.semicolon.dhaiban.presentation.customer_service

data class CustomerServiceUiState(
    val isLoading: Boolean = false,
    val countOfUnreadMessage: Int = 0,
    val subject: String = "",
    val content: String = "",
    var successSent:Boolean =false
)
