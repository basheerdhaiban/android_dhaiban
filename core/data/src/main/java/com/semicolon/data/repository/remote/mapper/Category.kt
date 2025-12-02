package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.category.MainCategoryDto
import com.semicolon.domain.entity.Category

fun MainCategoryDto.toEntity() =
    Category(
        id = this.id ?: 0,
        title = this.title ?: "",
        imageUrl = image?:""
    )

fun List<MainCategoryDto>.toEntity() =
    this.map { it.toEntity() }