package com.semicolon.data.repository.remote.model.send_new_message

data class Data(
    val messages: List<Message>,
    val pagination: Pagination
)