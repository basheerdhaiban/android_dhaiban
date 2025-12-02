package com.semicolon.domain.entity

data class FavoriteProduct(
    val advancedProduct: String,
    val avgRating: Double,
    val country: String,
    val currentStock: Int,
    val discount: Int,
    val discountType: String,
    val id: Int,
    val labelColor: String,
    val labelText: String,
    val measuringUnit: String,
    val new: Boolean,
    val photo: String,
    val productDate: String,
    val shortDesc: String,
    val title: String,
    val unitPrice: Double,
    val userFavorite: Boolean
){
    val afterDiscount: Double = if (discountType == "amount") {
        unitPrice - discount.toDouble()
    } else {
        unitPrice - unitPrice * discount.toDouble().div(100.0)
    }
}
