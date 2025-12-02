package com.semicolon.domain.entity

data class ChoiceItemModel(
    val id: Int,
    val title: String,
    val parentId: Int,
    val parentTitle: String
)
