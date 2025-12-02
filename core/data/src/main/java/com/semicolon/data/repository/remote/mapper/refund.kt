package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.refund.RefundDto
import com.semicolon.data.repository.remote.model.refund.RefundProduct
import com.semicolon.data.repository.remote.model.refund.RefundReasonItemDto
import com.semicolon.data.repository.remote.model.refund.User
import com.semicolon.data.repository.remote.model.refund.trackrefund.Product
import com.semicolon.data.repository.remote.model.refund.trackrefund.TrackRefundResponse
import com.semicolon.domain.entity.RefundModel
import com.semicolon.domain.entity.RefundProductModel
import com.semicolon.domain.entity.RefundReasonModel
import com.semicolon.domain.entity.RefundUser
import com.semicolon.domain.entity.orders.OrderAddressModel

fun RefundReasonItemDto.toRefundReasonModel() =
    RefundReasonModel(reasonId = reasonId ?: 0, reason = reason ?: "")

fun RefundProduct.toRefundProductModel() = RefundProductModel(
    id, photo, title
)
fun Product.toRefundProductModel() = RefundProductModel(
    id, photo, title
)
fun User.toRefundUser() = RefundUser(id, name, phone)
fun com.semicolon.data.repository.remote.model.refund.trackrefund.User.toRefundUser() = RefundUser(id, name, phone)

fun TrackRefundResponse.toRefundModel() = RefundModel(
    product = this.data[0].Product?.toRefundProductModel() ?: RefundProductModel(0, "", ""),
    address = this.data[0]?.address?.toOrderAddressModel() ?: OrderAddressModel(
        "",
        "",
        0,
        0.0,
        0.0,
        "",
        "",
        "",
        "",
        ""
    ),
    adminReason = this.data[0]?.admin_resone ?: "",
    code = this.data[0]?.code ?: "",
    createdAt = this.data[0]?.created_at ?: "",
    customerReason = this.data[0]?.customer_reson ?: "",
    editAddress = this.data[0]?.edit_address ?: false,
    id = this.data[0]?.id ?: 0,
    orderProductId = this.data[0]?.order_product_id ?: 0,
    paymentType = this.data[0]?.payment_type ?: "",
    refundAmount = this.data[0]?.refund_amount ?: 0.0,
    status = this.data[0]?.status ?: "",
    user = this.data[0]?.user?.toRefundUser() ?: RefundUser(0, "", ""),
    viewed = this.data[0]?.viewed ?: 0


)
fun RefundDto.toRefundModel() = RefundModel(
    product = this.product?.toRefundProductModel() ?: RefundProductModel(0, "", ""),
    address = this.address?.toOrderAddressModel() ?: OrderAddressModel(
        "",
        "",
        0,
        0.0,
        0.0,
        "",
        "",
        "",
        "",
        ""
    ),
    adminReason = this.adminReason ?: "",
    code = this.code ?: "",
    createdAt = this.createdAt ?: "",
    customerReason = this.customerReason ?: "",
    editAddress = this.editAddress ?: false,
    id = this.id ?: 0,
    orderProductId = this.orderProductId ?: 0,
    paymentType = this.paymentType ?: "",
    refundAmount = this.refundAmount ?: 0.0,
    status = this.status ?: "",
    user = this.user?.toRefundUser() ?: RefundUser(0, "", ""),
    viewed = this.viewed ?: 0


)