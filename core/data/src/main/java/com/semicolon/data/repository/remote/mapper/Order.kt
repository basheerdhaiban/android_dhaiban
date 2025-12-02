package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.order.MakeOrderResponseData
import com.semicolon.data.repository.remote.model.order.OrderAddressDto
import com.semicolon.data.repository.remote.model.order.OrderDto
import com.semicolon.data.repository.remote.model.order.OrderProductDto
import com.semicolon.data.repository.remote.model.order.SerializedOptions
import com.semicolon.data.repository.remote.model.refund.trackrefund.Address
import com.semicolon.data.repository.remote.model.review.ReviewResponse
import com.semicolon.domain.entity.CartChoice
import com.semicolon.domain.entity.ReviewsModel
import com.semicolon.domain.entity.SerializedOptionsModel
import com.semicolon.domain.entity.orders.OrderAddressModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.entity.orders.OrderProductModel
import com.semicolon.domain.entity.orders.OrderResponseModel
import com.semicolon.domain.entity.productdetails.ReviewModel

fun MakeOrderResponseData.toOrderResponseModel() =
    OrderResponseModel(message, orderId, success)


fun SerializedOptions.toSerializedOptionsModel() = SerializedOptionsModel(
    color = this.color ?: 0,
    choices = this.choices.map {
        CartChoice(
            it.id ?: 0,
            it.parentId ?: 0,
            it.title ?: "",
            it.parentTitle ?: ""
        )
    }, colorCode = "", colorTitle = ""
)

fun OrderProductDto.toOrderProductModel(): OrderProductModel {
    return OrderProductModel(
        advancedProduct = this.advancedProduct ?: "",
        couponDiscount = this.couponDiscount ?: 0,
        id = this.id ?: 0,
        photo = this.photo ?: "",
        price = this.price ?: 0.0,
        productId = this.productId ?: 0,
        productRefundable = this.productRefundable ?: false,
        productShortDesc = this.productShortDesc ?: "",
        productTitle = this.productTitle ?: "",
        quantity = this.quantity ?: 0,
        serializedOptions = this.serializedOptions?.toSerializedOptionsModel()
            ?: SerializedOptionsModel(
                emptyList(), 0, colorCode = "", colorTitle = ""
            ),
        tax = this.tax ?: 0.0 , productOwner = productOwner?.companyName ?:"" , productOwnerImg = productOwner?.image?:"" , productOwnerId = productOwner?.id?:0,rated=rated ?:0
    )
}

fun OrderAddressDto.toOrderAddressModel() =
    OrderAddressModel(
        address = this.address ?: "",
        cityName = this.cityName ?: "",
        id = this.id ?: 0,
        lat = this.lat ?: 0.0,
        lon = this.lon ?: 0.0,
        name = this.name ?: "",
        postalCode = this.postalCode ?: "",
        provinceName = this.provinceName ?: "",
        regionName = this.regionName ?: "",
        workHome = this.workHome ?: ""
    )
fun Address.toOrderAddressModel() =
    OrderAddressModel(

        address = this.address ?: "",
        cityName = this.city_name ?: "",
        id = this.id ?: 0,
        lat = this.lat ?: 0.0,
        lon = this.lon ?: 0.0,
        name = this.name ?: "",
        postalCode = this.postal_code ?: "",
        provinceName = this.province_name ?: "",
        regionName = this.region_name ?: "",
        workHome = this.work_home ?: ""
    )
fun ReviewResponse.toReviewModel() =
    ReviewsModel(success = success ,message =message


    )

fun OrderDto.toOrderModel() =
    OrderModel(
        orderProducts = this.orderProduct?.map { it.toOrderProductModel() } ?: emptyList(),
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
        code = this.code ?: "",
        commercialRegister = this.commercialRegister ?: "",
        couponDiscount = this.couponDiscount ?: 0,
        currency = this.currency ?: "",
        date = this.date ?: "",
        deliveryStatus = this.deliveryStatus ?: "",
        id = this.id ?: 0,
        invoiceLink = this.invoiceLink ?: "",
        isRated = this.isRated ?: 0,
        orderType = this.orderType ?: "",
        paymentStatus = this.paymentStatus ?: 0,
        paymentType = this.paymentType ?: "",
        shippingCost = this.shippingCost ?: 0,
        shippingTax = this.shippingTax ?: 0,
        tax = this.tax ?: 0.0,
        totalPrice = this.totalPrice ?: 0.0,
        user = this.user?.name ?: "",
        viewed = this.viewed ?: 0,
        walletDiscount = this.walletDiscount ?: 0.0
    )