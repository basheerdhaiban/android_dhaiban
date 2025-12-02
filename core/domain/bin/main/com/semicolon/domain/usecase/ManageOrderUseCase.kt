package com.semicolon.domain.usecase

import androidx.paging.PagingData
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.ProductOFCoupon
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.repository.OrderRepository
import kotlinx.coroutines.flow.Flow

class ManageOrderUseCase(private val orderRepository: OrderRepository) {
    suspend fun makeOrder(currencyId: Int, addressId: Int, products: List<CartItem> , couponCode: String,wallet:Double,
                          couponDiscount:Double, paymentType:String,paymentStatus:Boolean=false) =
        orderRepository.makeOrder(wallet, couponCode, couponDiscount, currencyId, addressId, products ,paymentType,paymentStatus)
    suspend fun makeDiscountCoupon( couponCode: String,
                                    products: List<ProductOFCoupon>) =
        orderRepository.getCoubonDiscount(couponCode, products)

    suspend fun getCurrentOrders() = orderRepository.getCurrentOrders()
    suspend fun getOrderWithID(id :Int) = orderRepository.getOrderWithId(id)
    suspend fun trackTranscationOfMomo(id :String,        partyID: String="",
                                       partyIDType: String="",
                                       amount: Double=0.0,
                                       concurency: String="",
                                       payerMessage: String="",
                                       payerNote: String="") = orderRepository.trackTransaction(id, partyID, partyIDType, amount, concurency, payerMessage, payerNote)
    suspend fun makePayment(
        partyID: String,
        partyIDType: String,
        amount: Double,
        concurency: String,
        payerMessage: String,
        payerNote: String
    )= orderRepository.makePayment(partyID, partyIDType, amount, concurency, payerMessage, payerNote)

    fun getPreviousOrders(): Flow<PagingData<OrderModel>> {
        println("ManageOrderUseCase getPreviousOrders")
        return orderRepository.getPreviousOrders()
    }
    suspend fun setRateOrder(
        orderId: Int,
        productId: Int,
        rate: Int,
        review: String
    ) =orderRepository.setRateOrder(orderId,productId,rate,review)
}