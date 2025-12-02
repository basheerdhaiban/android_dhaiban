package com.semicolon.domain.repository

import com.semicolon.domain.entity.Category

interface CategoriesRepository {
    suspend fun getMainCategories(): List<Category>
}