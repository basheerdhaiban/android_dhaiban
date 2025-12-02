package com.semicolon.dhaiban.presentation.main

data class MessageStatus(
    val inbox_id: Int,
    val message: String,
    val modelable: String,
    val order_id: Int,
    val product_id: Int
)