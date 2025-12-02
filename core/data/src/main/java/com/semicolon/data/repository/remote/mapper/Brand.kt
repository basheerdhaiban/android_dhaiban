package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.brand.BrandDto
import com.semicolon.domain.entity.Brand

fun BrandDto.toEntity() = Brand(
    id = this.id ?: 0,
    title = this.title ?: "",
    logoUrl = logo ?: ""
)

fun List<BrandDto>.toEntity() =
    this.map { it.toEntity() }