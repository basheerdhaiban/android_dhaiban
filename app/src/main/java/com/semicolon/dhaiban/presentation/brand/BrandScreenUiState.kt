package com.semicolon.dhaiban.presentation.brand

import com.semicolon.domain.entity.Brand

data class BrandScreenUiState(
    val isLoading: Boolean = false,
    val brands: List<BrandUiState> = emptyList(),
    val errorMessage: String = ""
)

data class BrandUiState(
    val id: Int = 0,
    val title: String = "",
    val image: String = ""
)


fun Brand.toBrandUiState() =
    BrandUiState(id = this.id, title = this.title, image = logoUrl)