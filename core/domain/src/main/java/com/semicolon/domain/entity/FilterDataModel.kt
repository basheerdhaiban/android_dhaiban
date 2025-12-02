package com.semicolon.domain.entity

import com.semicolon.domain.entity.productdetails.ColorModel

data class FilterDataModel(
    val attributes: List<AttributeModel>,
    val colors: List<ColorModel>,
    val maxPrice: Double,
    val minPrice: Double
)