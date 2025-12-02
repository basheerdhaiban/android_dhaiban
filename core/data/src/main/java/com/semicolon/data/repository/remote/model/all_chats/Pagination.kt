package com.semicolon.data.repository.remote.model.all_chats

data class Pagination(
    val current_page: Int,
    val last_page: Int,
    val next_page_url: Any,
    val per_page: Int,
    val prev_page_url: Any,
    val total: Int
)