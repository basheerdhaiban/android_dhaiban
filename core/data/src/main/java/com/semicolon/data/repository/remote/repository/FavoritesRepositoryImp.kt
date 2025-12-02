package com.semicolon.data.repository.remote.repository

import android.content.res.Resources.NotFoundException
import com.semicolon.data.repository.remote.favorite.FavoriteData
import com.semicolon.data.repository.remote.favorite.FavoriteDataItem
import com.semicolon.data.repository.remote.mapper.toFavoriteProduct
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.domain.entity.FavoriteProduct
import com.semicolon.domain.repository.FavoritesRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class FavoritesRepositoryImp(client: HttpClient) : BaseRepository(client), FavoritesRepository {

    companion object{
        const val ADD_TO_FAVORITE = "addToFavorite"
        const val REMOVE_FROM_FAVORITE = "removeFromFavorite"
        const val USER_FAVORITE = "userFavorite"
    }
    override suspend fun addToFavorite(productId: Int): String {
        val result = tryToExecute<BaseResponse<FavoriteData>> {
            client.get("$ADD_TO_FAVORITE/$productId")
        }.data?: FavoriteData(message = "")

        return result.message
    }

    override suspend fun removeFromFavorite(productId: Int) : String{
        val result = tryToExecute<BaseResponse<FavoriteData>> {
            client.get("$REMOVE_FROM_FAVORITE/$productId")
        }.data?: FavoriteData(message = "")

        return result.message
    }

    override suspend fun getFavoriteProducts(): List<FavoriteProduct> {
        val result = tryToExecute<BaseResponse<List<FavoriteDataItem>>> {
            client.get(USER_FAVORITE)
        }.data?: throw NotFoundException()

        return result.map { it.toFavoriteProduct() }
    }
}