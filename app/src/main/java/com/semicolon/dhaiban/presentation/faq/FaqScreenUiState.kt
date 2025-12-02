package com.semicolon.dhaiban.presentation.faq

import com.semicolon.domain.entity.FaqModel

data class FaqScreenUiState(
    val isLoading: Boolean = false,
    val faqTypes: List<FaqTypeUiState> = emptyList()
)

data class FaqTypeUiState(
    val id: Int = 0,
    val type: String = ""
)

fun FaqModel.toFaqTypeUsState() = FaqTypeUiState(id, type)
