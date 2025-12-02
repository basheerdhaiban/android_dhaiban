package com.semicolon.domain.entity

data class Product(
    val advancedProduct: String,
    val averageRating: Int,
    val country: String,
    val currentStock: Int,
    val discount: Int,
    val discountType: String,
    val id: Int,
    val labelColor: String,
    val labelText: String,
    val new: Boolean,
    val photo: String,
    val productDate: String,
    val shortDescription: String,
    val title: String,
    val unitPrice: Double,
    val userFavorite: Boolean,
    val lowerPrice :Double
    ,val higherprice:Double
) {
    val afterDiscount: Double = if (discountType == "amount") {
        unitPrice - discount.toDouble()
    } else {
        unitPrice - unitPrice * discount.toDouble().div(100.0)
    }
    val higherpriceAfterDiscount: Double = if (discountType == "amount") {
        higherprice - discount.toDouble()
    } else {
        higherprice - higherprice * discount.toDouble().div(100.0)
    }
    val lowerPriceAfterDiscount: Double = if (discountType == "amount") {
        lowerPrice - discount.toDouble()
    } else {
        lowerPrice - lowerPrice * discount.toDouble().div(100.0)
    }


}