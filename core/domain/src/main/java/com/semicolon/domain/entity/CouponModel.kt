package com.semicolon.domain.entity

data class CouponModel(    val discount: Double,
                           val message: String,
                           val products: List<CouponProduct>,

                           val success: Boolean)
//data class ProductOfCoupon(
//    val discount: Double,
//    val id: Double,
//    val price: Double,
//    val variation: String
//)