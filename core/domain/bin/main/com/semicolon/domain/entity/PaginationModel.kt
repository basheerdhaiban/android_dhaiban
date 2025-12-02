package com.semicolon.domain.entity

data class PaginationModel(
    val currentPage: Int,
    val lastPage: Int?,
    val nextPageUrl: String?,
    val perPage: Int?,
    val previousPageUrl: String?,
    val total: Int?
)

