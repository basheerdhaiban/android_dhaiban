package com.semicolon.domain.usecase

import com.semicolon.domain.repository.SearchRepository

class ManageSearchUseCase(private val searchRepository: SearchRepository) {
    suspend fun searchForProduct(query: String?, imageByteArray: ByteArray?) = searchRepository.searchForProducts(query, imageByteArray)
    suspend fun searchForProductByImage(imageByteArray: ByteArray?) = searchRepository.searchForProductsByImage(imageByteArray)
}