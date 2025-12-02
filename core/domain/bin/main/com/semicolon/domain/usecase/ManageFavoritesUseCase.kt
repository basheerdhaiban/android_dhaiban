package com.semicolon.domain.usecase

import com.semicolon.domain.repository.FavoritesRepository

class ManageFavoritesUseCase(private val favoritesRepository: FavoritesRepository) {
    suspend fun addProductToFavorite(productId: Int) = favoritesRepository.addToFavorite(productId)
    suspend fun removeProductFromFavorite(productId: Int) =
        favoritesRepository.removeFromFavorite(productId)

    suspend fun getFavoriteProducts() = favoritesRepository.getFavoriteProducts()
}