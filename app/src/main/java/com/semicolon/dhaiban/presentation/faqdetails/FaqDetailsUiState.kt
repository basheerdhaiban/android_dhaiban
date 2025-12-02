package com.semicolon.dhaiban.presentation.faqdetails

import com.semicolon.domain.entity.FaqItemModel

data class FaqDetailsUiState(
    val isLoading: Boolean = false,
    val faqDetails: List<FaqDetailsItemUiState> = emptyList()
)

data class FaqDetailsItemUiState(
    val answer: String = "",
    val id: Int = 0,
    val question: String = ""
)

fun FaqItemModel.toFaqDetailsItemUiState() = FaqDetailsItemUiState(
    answer, id, question
)