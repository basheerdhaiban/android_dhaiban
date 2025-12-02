package com.semicolon.domain.entity

data class SerializedOptionsModel(
    val choices: List<CartChoice>,
    val color: Int,
    val colorCode:String,
    val colorTitle:String
)
