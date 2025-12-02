package com.semicolon.dhaiban.presentation.category

import androidx.compose.runtime.Immutable
import com.semicolon.domain.entity.Category

data class CategoryScreenUiState(
    val isLoading:Boolean = false,
    val categories: List<CategoryUiState> = emptyList(),
    val errorMessage: String = "",
    var countOfUnreadMessage :Int=0,
    val userData: UserDataUiState = UserDataUiState(),
)
@Immutable
data class UserDataUiState(
    val username: String = "",
    val isAuthenticated: Boolean = false
)
data class CategoryUiState(
    val id: Int = 0,
    val title: String = "",
    val image: String = ""
)

fun Category.toEntity() =
    CategoryUiState(
        id = this.id,
        title = this.title,
        image = imageUrl
    )