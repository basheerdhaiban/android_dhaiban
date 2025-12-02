package com.semicolon.domain.entity.orders

import com.semicolon.domain.entity.SerializedOptionsModel

data class OrderProductModel(
    val advancedProduct: String,
    val couponDiscount: Int,
    val id: Int,
    val photo: String,
    val price: Double,
    val productId: Int,
    val productRefundable: Boolean,
    val productShortDesc: String,
    val productTitle: String,
    val quantity: Int,
    val serializedOptions: SerializedOptionsModel,
    val tax: Double,
    val productOwner:String,
    val productOwnerImg :String ? ,
    val productOwnerId:Long,
    val rated:Int,

    )
