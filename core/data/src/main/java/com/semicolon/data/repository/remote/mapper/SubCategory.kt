package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.product.AllColor
import com.semicolon.data.repository.remote.model.product.Attribute
import com.semicolon.data.repository.remote.model.product.OptionDto
import com.semicolon.data.repository.remote.model.subcategory.SubCategoryDto
import com.semicolon.domain.entity.AttributeModel
import com.semicolon.domain.entity.SubCategory
import com.semicolon.domain.entity.productdetails.ColorModel
import com.semicolon.domain.entity.productdetails.OptionModel

fun SubCategoryDto.toSubCategoryModel() = SubCategory(
    id = this.id ?: 0, imageUrl = this.imageUrl ?: "", title = this.title ?: ""
)

fun List<SubCategoryDto>.toSubCategoryData() = this.map { it.toSubCategoryModel() }

fun OptionDto.toEntity() = OptionModel(
    id, title
)

fun Attribute.toAttributeModel() =
    AttributeModel(
        id = this.id ?: 0,
        options = this.options?.map { it.toEntity() } ?: emptyList(),
        title = this.title ?: ""
    )

fun List<Attribute>.toAttributeData() = this.map { it.toAttributeModel() }

fun AllColor.toColorModel() = ColorModel(
    colorCode = this.colorCode ?: "",
    id = this.id ?: 0
)

fun List<AllColor>.toColorData() = this.map { it.toColorModel() }

