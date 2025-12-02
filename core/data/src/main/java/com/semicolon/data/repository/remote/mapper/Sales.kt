package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.product.ProductDto
import com.semicolon.domain.entity.Product

fun ProductDto.toEntity() =
    Product(
        advancedProduct = advancedProduct ?: "",
        averageRating = averageRating?.toInt() ?: 0,
        country = country ?: "",
        currentStock = currentStock ?: 0,
        discount = discount ?: 0,
        discountType = discountType ?: "",
        id = id ?: 0,
        labelColor = labelColor ?: "",
        labelText = labelText ?: "",
        new = new ?: false,
        photo = photo ?: "",
        productDate = productDate ?: "",
        shortDescription = shortDescription ?: "",
        title = title ?: "",
        unitPrice = unitPrice ?: 0.0,
        userFavorite = userFavorite ?: false, lowerPrice = 0.0, higherprice = 0.0
    )

fun List<ProductDto>.toEntity()=
    this.map { it.toEntity() }