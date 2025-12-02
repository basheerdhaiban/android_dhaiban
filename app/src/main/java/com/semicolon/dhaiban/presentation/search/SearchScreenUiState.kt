package com.semicolon.dhaiban.presentation.search

import com.semicolon.dhaiban.presentation.home.ProductUiState

data class SearchScreenUiState(
    val isLoading : Boolean = false,
    val queryValue: String = "",
    val searchList: List<ProductUiState> = emptyList(),

)
