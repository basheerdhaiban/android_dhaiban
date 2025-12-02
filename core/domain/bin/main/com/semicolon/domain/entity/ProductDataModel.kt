package com.semicolon.domain.entity

data class ProductDataModel(
    val products: List<Product>,
    val pagination: PaginationModel
)