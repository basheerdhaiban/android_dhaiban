package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.favorite.FavoriteDataItem
import com.semicolon.domain.entity.FavoriteProduct

fun FavoriteDataItem.toFavoriteProduct() =
    FavoriteProduct(
        advancedProduct ?: "",
        avgRating ?: 0.0,
        country ?: "",
        currentStock ?: 0,
        discount ?: 0,
        discountType ?: "",
        id ?: 0,
        labelColor ?: "",
        labelText ?: "",
        measuringUnit ?: "",
        new ?: false,
        photo ?: "",
        productDate ?: "",
        shortDesc ?: "",
        title ?: "",
        unitPrice ?: 0.0,
        userFavorite ?: false
    )