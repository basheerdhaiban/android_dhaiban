package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.product.Pagination
import com.semicolon.data.repository.remote.model.product.ProductData
import com.semicolon.data.repository.remote.model.product.ProductTypeDto
import com.semicolon.data.repository.remote.model.product.SliderItemDto
import com.semicolon.data.repository.remote.model.product.VariantPriceDto
import com.semicolon.domain.entity.PaginationModel
import com.semicolon.domain.entity.ProductDataModel
import com.semicolon.domain.entity.ProductType
import com.semicolon.domain.entity.SliderItem
import com.semicolon.domain.entity.productdetails.VariantPrice

fun SliderItemDto.toEntity() =
    SliderItem(
        id = this.id ?: 0,
        title = this.title ?: "",
        description = this.description ?: "",
        photoUrl = photo ?: "",
        offerGroupId = this.offerGroupId ?: 0,
        type = this.type ?: "",
        typeId = this.typeId ?: 0
    )

fun List<SliderItemDto>.toEntity() =
    this.map { it.toEntity() }

fun ProductTypeDto.toProductTypeEntity() = ProductType(
    id = this.id ?: 0,
    title = this.title?: ""
)

fun List<ProductTypeDto>.toProductTypesEntity() = this.map { it.toProductTypeEntity() }

fun Pagination.toEntity() = PaginationModel(
    currentPage = this.currentPage?:0,
    lastPage = this.lastPage?:0,
    nextPageUrl = this.nextPageUrl?:"",
    perPage = this.perPage?:0,
    previousPageUrl = this.previousPageUrl?:"",
    total = this.total?:0
)
fun ProductData.toEntity() = ProductDataModel(
    products = this.products.toEntity(),
    pagination = this.pagination?.toEntity()?: PaginationModel(0,0,"",0,"",0)
)

fun VariantPriceDto.toVariantPrice() = VariantPrice(
    stockCount = inStock?:0,
    measuringUnit = measuringUnit?:"",
    price = this.price?:0.0,
    productId = this.productId?:0,
    variant = this.variant?:"",
    weight = this.weight?:0
)