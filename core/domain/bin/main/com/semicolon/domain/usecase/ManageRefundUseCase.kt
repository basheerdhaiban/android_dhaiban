package com.semicolon.domain.usecase

import com.semicolon.domain.repository.RefundRepository

class ManageRefundUseCase(private val repository: RefundRepository) {
    suspend fun getRefundReasons() = repository.getRefundReasons()
    suspend fun getRefundById(refundId:Int) = repository.getRefundByID(refundId)
    suspend fun makeRefundRequest(
        addressId: Int,
        orderId: Int,
        orderProductId: Int,
        reasonId: Int,
        customerReason: String
    ) = repository.makeRefundRequest(addressId, orderId, orderProductId, reasonId, customerReason)

    suspend fun getCurrentRefunds() = repository.getCurrentRefunds()
    suspend fun getPreviousRefunds() = repository.getPreviousRefunds()
}