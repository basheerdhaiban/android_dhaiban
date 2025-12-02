package com.semicolon.domain.usecase

import com.semicolon.domain.repository.SubCategoryRepository

class ManageSubCategoryUseCase(private val subCategoryRepository: SubCategoryRepository) {
    suspend fun getSubCategories(categoryId: Int) = subCategoryRepository.getSubCategories(categoryId)

    suspend fun getFilterData(categoryId: Int, country:String) = subCategoryRepository.getFilterData(categoryId, country)

    suspend fun getCategorySlider(categoryId: Int) = subCategoryRepository.getCategorySlider(categoryId)
}