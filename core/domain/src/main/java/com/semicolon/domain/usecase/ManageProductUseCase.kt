package com.semicolon.domain.usecase

import com.semicolon.domain.repository.ProductRepository

class ManageProductUseCase(private val productRepository: ProductRepository) {
    suspend fun getSliderItems() = productRepository.getSliderProducts()

    suspend fun getSaleProducts() = productRepository.getSalesProducts()

    suspend fun getNewProducts(country:String) = productRepository.getNewProducts(country)

    suspend fun getNewProducts1(country:String) = productRepository.getNewProducts1(country)

    suspend fun getProductTypes() = productRepository.getProductTypes()

    fun getProducts(
        offerGroupId: Int? = null,
        countryId: String? = null,
        color: Int? = null,
        brandId: Int? = null,
        sortBy: Int? = null,
        searchQuery: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        categoryId: Int? = null,
        sellerId: Int? = null,
        attributes: Map<Int, List<String>>? = null
    ) = productRepository.getProducts(
        offerGroupId,
        countryId,
        color,
        brandId,
        sortBy,
        searchQuery,
        minPrice,
        maxPrice,
        categoryId,
        sellerId,
        attributes
    )

    suspend fun getProductDetails(productId: Int) = productRepository.getProductDetails(productId)

    suspend fun getRecommendedProducts(productId: Int) =
        productRepository.getRelatedProducts(productId)

    suspend fun getProductReviews(productId: Int) = productRepository.getProductReviews(productId)

    suspend fun getVariantPrice(productId: Int, colorId: Int?, variantList: List<Int>) =
        productRepository.getVariantPrice(productId, colorId, variantList)

    suspend fun searchForProduct(query: String?, imageByteArray: ByteArray?) = productRepository.searchForProducts(query, imageByteArray)

    suspend fun searchForProductByImage(byteArray: ByteArray?) = productRepository.searchForProductsByImage(byteArray)
}