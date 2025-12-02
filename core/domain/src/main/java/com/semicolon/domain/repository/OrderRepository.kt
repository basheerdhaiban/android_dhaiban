package com.semicolon.domain.repository

import androidx.paging.PagingData
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.CouponModel
import com.semicolon.domain.entity.PayTabsModel
import com.semicolon.domain.entity.PaymentModel
import com.semicolon.domain.entity.ProductOFCoupon
import com.semicolon.domain.entity.ReviewsModel
import com.semicolon.domain.entity.TransactionModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.entity.orders.OrderResponseModel
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun setRateOrder (orderId :Int, productId :Int, rate :Int, review :String): ReviewsModel
    suspend fun makeOrder(
        wallet:Double,
        couponCode: String,
        couponDiscount:Double,
        currencyId: Int,
        addressId: Int,
        products: List<CartItem>, paymentType:String
        ,paymentStatus:Boolean
    ): OrderResponseModel
    suspend fun makePayment(        partyID: String,
                                    partyIDType: String,
                                    amount: Double,
                                    concurency: String,
                                    payerMessage: String,
                                    payerNote: String): PaymentModel?
    suspend fun getCurrentOrders(): List<OrderModel>
    suspend fun getOrderWithId(id :Int):List<OrderModel>
    suspend fun trackTransaction(id: String,        partyID: String,
                                 partyIDType: String,
                                 amount: Double,
                                 concurency: String,
                                 payerMessage: String,
                                 payerNote: String): TransactionModel
    fun getPreviousOrders(): Flow<PagingData<OrderModel>>
    suspend fun getCoubonDiscount(
        couponCode: String,
        products: List<ProductOFCoupon>
    ): CouponModel


}