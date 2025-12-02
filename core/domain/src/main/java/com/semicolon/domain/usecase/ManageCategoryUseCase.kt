package com.semicolon.domain.usecase

import com.semicolon.domain.entity.Category
import com.semicolon.domain.repository.CategoriesRepository

class ManageCategoryUseCase(private val categoriesRepository: CategoriesRepository) {
    suspend fun getCategories() : List<Category> = categoriesRepository.getMainCategories()
}