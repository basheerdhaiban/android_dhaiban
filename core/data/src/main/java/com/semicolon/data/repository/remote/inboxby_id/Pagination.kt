package com.semicolon.data.repository.remote.inboxby_id

data class Pagination(
    val current_page: Int,
    val last_page: Int,
    val next_page_url: String,
    val per_page: Int,
    val prev_page_url: Any,
    val total: Int
)