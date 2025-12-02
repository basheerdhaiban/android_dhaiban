package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.coupon.Product
import com.semicolon.data.repository.remote.model.coupon.ResponseOfCoupon
import com.semicolon.data.repository.remote.model.send_new_message.Message
import com.semicolon.domain.entity.CouponModel
import com.semicolon.domain.entity.CouponProduct


fun ResponseOfCoupon.toCouponModel() = CouponModel(
    discount, message, products = products.map { it.toCouponModel()},success,


)
fun Product.toCouponModel() = CouponProduct(discount, id, price, variation)