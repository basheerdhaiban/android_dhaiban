package com.semicolon.domain.repository

import com.semicolon.domain.entity.RefundModel
import com.semicolon.domain.entity.RefundReasonModel

interface RefundRepository {
    suspend fun getRefundReasons(): List<RefundReasonModel>
    suspend fun makeRefundRequest(
        addressId: Int,
        orderId: Int,
        orderProductId: Int,
        reasonId: Int,
        customerReason: String
    ) : Boolean
    suspend fun getCurrentRefunds() : List<RefundModel>
    suspend fun getPreviousRefunds() : List<RefundModel>
    suspend fun getRefundByID(refundID :Int): RefundModel
}