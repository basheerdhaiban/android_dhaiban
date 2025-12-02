package com.semicolon.domain.repository

import com.semicolon.domain.entity.FavoriteProduct

interface FavoritesRepository {
    suspend fun addToFavorite(productId: Int): String
    suspend fun removeFromFavorite(productId: Int): String
    suspend fun getFavoriteProducts():List<FavoriteProduct>
}