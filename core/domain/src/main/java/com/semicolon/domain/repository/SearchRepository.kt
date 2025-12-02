package com.semicolon.domain.repository

import com.semicolon.domain.entity.Product

interface SearchRepository {
    suspend fun searchForProducts(query: String?, imageByteArray: ByteArray?) : List<Product>
    suspend fun searchForProductsByImage(imageByteArray: ByteArray?) : List<Product>
}