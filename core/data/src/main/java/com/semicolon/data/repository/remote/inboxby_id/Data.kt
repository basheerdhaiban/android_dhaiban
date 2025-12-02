package com.semicolon.data.repository.remote.inboxby_id

data class Data(
    val messages: List<Message>,
    val pagination: Pagination,
    val product: Product
)