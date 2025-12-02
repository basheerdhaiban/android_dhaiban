package com.semicolon.data.repository.remote.repository

import android.content.res.Resources.NotFoundException
import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.brand.BrandData
import com.semicolon.domain.entity.Brand
import com.semicolon.domain.repository.BrandsRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class BrandsRepositoryImp(client: HttpClient): BaseRepository(client), BrandsRepository {
    companion object{
        const val FEATURED_BRANDS = "featuredBrands"
    }
    override suspend fun getFeaturedBrands(): List<Brand> {
        val result = tryToExecute<BaseResponse<BrandData>> {
            client.get(FEATURED_BRANDS)
        }.data?: throw NotFoundException()
        return result.featuredBrands.toEntity()
    }
}