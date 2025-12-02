package com.semicolon.domain.usecase

import com.semicolon.domain.repository.BrandsRepository

class ManageBrandUseCase(private val brandsRepository: BrandsRepository) {
    suspend fun getFeaturedBrands() = brandsRepository.getFeaturedBrands()
}