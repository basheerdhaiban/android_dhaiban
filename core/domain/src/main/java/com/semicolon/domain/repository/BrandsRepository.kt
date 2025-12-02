package com.semicolon.domain.repository

import com.semicolon.domain.entity.Brand

interface BrandsRepository {
    suspend fun getFeaturedBrands(): List<Brand>
}