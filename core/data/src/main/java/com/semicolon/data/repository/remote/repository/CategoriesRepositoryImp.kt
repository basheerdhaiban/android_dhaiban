package com.semicolon.data.repository.remote.repository

import android.content.res.Resources.NotFoundException
import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.category.CategoryData
import com.semicolon.domain.entity.Category
import com.semicolon.domain.repository.CategoriesRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class CategoriesRepositoryImp(client: HttpClient) : BaseRepository(client), CategoriesRepository {
    companion object {
        const val MAIN_CATEGORIES = "mainCategory"
        const val SLIDER = "slider"
    }

    override suspend fun getMainCategories(): List<Category> {
        val result = tryToExecute<BaseResponse<CategoryData>> {
            client.get(MAIN_CATEGORIES)
        }.data ?: throw NotFoundException()
        return result.mainCategories.toEntity()
    }
}
