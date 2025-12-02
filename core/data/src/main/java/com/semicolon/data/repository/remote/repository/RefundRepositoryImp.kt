package com.semicolon.data.repository.remote.repository

import com.semicolon.data.repository.remote.mapper.toRefundModel
import com.semicolon.data.repository.remote.mapper.toRefundReasonModel
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.refund.CurrentRefundRequestsResponse
import com.semicolon.data.repository.remote.model.refund.RefundReasonItemDto
import com.semicolon.data.repository.remote.model.refund.RefundRequestResponse
import com.semicolon.data.repository.remote.model.refund.trackrefund.TrackRefundResponse
import com.semicolon.domain.entity.RefundModel
import com.semicolon.domain.entity.RefundReasonModel
import com.semicolon.domain.repository.RefundRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters

class RefundRepositoryImp(client: HttpClient) : RefundRepository, BaseRepository(client) {

    companion object {
        const val REFUND_REASONS = "getRefundResons"
        const val REFUND_REQUEST = "refundProduct"
        const val ADDRESS_ID = "adress_id"
        const val ORDER_ID = "order_id"
        const val ORDER_PRODUCT_ID = "order_product_id"
        const val REASON_ID = "resone_id"
        const val CUSTOMER_REASON = "customer_reson"
        const val CURRENT_REFUNDS = "currentRefunds"
        const val PREVIOUS_REFUNDS = "prevRefunds"
    }

    override suspend fun getRefundReasons(): List<RefundReasonModel> {
        val result = tryToExecute<BaseResponse<List<RefundReasonItemDto>>> {
            client.get(REFUND_REASONS)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.map { it.toRefundReasonModel() }
    }


    override suspend fun getRefundByID(refundID :Int): RefundModel{
        val result = tryToExecute<BaseResponse<TrackRefundResponse>> {
            client.get("trackRefund/$refundID")
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toRefundModel()
    }

    override suspend fun makeRefundRequest(
        addressId: Int,
        orderId: Int,
        orderProductId: Int,
        reasonId: Int,
        customerReason: String
    ): Boolean {
        val result = tryToExecute<BaseResponse<RefundRequestResponse>> {
            client.submitForm(
                url = REFUND_REQUEST,
                formParameters = parameters {
                    append(ADDRESS_ID, addressId.toString())
                    append(ORDER_ID, orderId.toString())
                    append(ORDER_PRODUCT_ID, orderProductId.toString())
                    append(REASON_ID, reasonId.toString())
                    append(CUSTOMER_REASON, customerReason)
                }
            )
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return true
    }

    override suspend fun getCurrentRefunds(): List<RefundModel> {
        val result = tryToExecute<BaseResponse<CurrentRefundRequestsResponse>> {
            client.get(CURRENT_REFUNDS)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.data.map { it.toRefundModel() }
    }

    override suspend fun getPreviousRefunds(): List<RefundModel> {
        val result = tryToExecute<BaseResponse<CurrentRefundRequestsResponse>> {
            client.get(PREVIOUS_REFUNDS)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.data.map { it.toRefundModel() }
    }
}