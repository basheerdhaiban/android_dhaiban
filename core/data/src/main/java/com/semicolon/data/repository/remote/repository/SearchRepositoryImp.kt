package com.semicolon.data.repository.remote.repository

import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.product.ProductSearchData
import com.semicolon.data.repository.remote.repository.ProductRepositoryImp.Companion.SEARCH_BY_IMAGE
import com.semicolon.domain.entity.Product
import com.semicolon.domain.repository.SearchRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders

class SearchRepositoryImp(client: HttpClient) : BaseRepository(client), SearchRepository {
    override suspend fun searchForProducts(query: String?, imageByteArray: ByteArray?): List<Product> {
        val result = tryToExecute<BaseResponse<ProductSearchData>> {
            if (imageByteArray?.isNotEmpty() == true) {
                client.submitFormWithBinaryData(
                    url = SEARCH_BY_IMAGE,
                    formData = formData {
                        append("image", imageByteArray, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=image.png")
                        })
                    })
            } else {
                client.get {
                    url(ProductRepositoryImp.SEARCH)
                    parameter(ProductRepositoryImp.QUERY, query)
                }
            }
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.products.map { it.toEntity() }
    }

    override suspend fun searchForProductsByImage(imageByteArray: ByteArray?): List<Product> {
        val result = tryToExecute<BaseResponse<ProductSearchData>> {
            client.submitFormWithBinaryData(
                url = SEARCH_BY_IMAGE,
                formData = formData {
                    if (imageByteArray != null) {
                        append("image", imageByteArray, Headers.build {
                            append(HttpHeaders.ContentDisposition, "filename=image.png")
                        }
                        )
                    }
                })
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.products.map { it.toEntity() }
    }
}