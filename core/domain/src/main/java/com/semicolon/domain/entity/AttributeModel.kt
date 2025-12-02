package com.semicolon.domain.entity

import com.semicolon.domain.entity.productdetails.OptionModel

data class AttributeModel(
    val id: Int,
    val options: List<OptionModel>,
    val title: String
)