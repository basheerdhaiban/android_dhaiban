package com.semicolon.data.repository.remote.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.GsonBuilder
import com.semicolon.data.repository.remote.mapper.toCouponModel
import com.semicolon.data.repository.remote.mapper.toOrderModel
import com.semicolon.data.repository.remote.mapper.toOrderResponseModel
import com.semicolon.data.repository.remote.mapper.toPaymentModel
import com.semicolon.data.repository.remote.mapper.toReviewModel
import com.semicolon.data.repository.remote.mapper.toTransactionModel
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.coupon.ResponseOfCoupon
import com.semicolon.data.repository.remote.model.order.MakeOrderResponseData
import com.semicolon.data.repository.remote.model.order.OrdersResponseData
import com.semicolon.data.repository.remote.model.payment.PaymentResponse
import com.semicolon.data.repository.remote.model.review.Order
import com.semicolon.data.repository.remote.model.review.Review
import com.semicolon.data.repository.remote.model.review.ReviewResponse
import com.semicolon.data.repository.remote.model.status_momo.ResponseOfStatus
import com.semicolon.data.repository.remote.paging.PreviousOrdersPagingSource
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.CouponModel
import com.semicolon.domain.entity.PayTabsModel
import com.semicolon.domain.entity.PaymentModel
import com.semicolon.domain.entity.ProductOFCoupon
import com.semicolon.domain.entity.ReviewsModel
import com.semicolon.domain.entity.TransactionModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.entity.orders.OrderResponseModel
import com.semicolon.domain.repository.OrderRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.http.parameters
import kotlinx.coroutines.flow.Flow

class OrderRepositoryImp(
    client: HttpClient,
    private val previousOrdersRemoteSource: PreviousOrdersRemoteSource
) : OrderRepository, BaseRepository(client) {
    companion object {

        const val PAYMENT_TYPE = "payment_type"
        const val ORDER_TYPE = "order_type"
        const val PAYMENT_STATUS = "payment_status"
        const val ADDRESS_ID = "address_id"
        const val WALLET_DISCOUNT = "wallet_discount"
        const val COUPON_DISCOUNT = "coupon_discount"
        const val COUPON_CODE = "coupon_code"
        const val CURRENCY_ID = "currency_id"
        const val PRODUCTS = "products"
        const val PRODUCT_ID = "product_id"
        const val DISCOUNT = "discount"
        const val VARIANT = "variation"
        const val QUANTITY = "quantity"
        const val SERIALIZED_OPTIONS = "serialized_options"
        const val MAKE_ORDER = "makeOrder"
        const val TRACK_ORDER = "trackOrder"
        const val CURRENT_ORDER = "currentOrders"

        private const val PAGE_SIZE = 15

        val gson = GsonBuilder().serializeNulls().create()
    }

    override suspend fun setRateOrder(
        orderId: Int,
        productId: Int,
        rate: Int,
        review: String
    ): ReviewsModel {
        val review = Review(
            product_id = productId,
            rate = rate,
            review = review
        )
        val listOfReview = listOf(review)
        val order = Order(
            order_id = orderId,
            reviews = listOfReview
        )
//        val gson = GsonBuilder().serializeNulls().create()

//        val listOfReviews = listOf(
//            mapOf(
//                "product_id" to productId,
//                "rate" to Int,
//                "review" to review
//            )
//        )

//        val dataString = Gson().toJson(
//            mapOf(
//                "order_id" to orderId,
//                "reviews" to listOfReviews
//            )
//        )
        val result = tryToExecute<BaseResponse<ReviewResponse>> {

            client.post("rateOrder") {
                setBody(order)
                contentType(ContentType.Application.Json)
            }

        }

        if (result.data == null) {
            throw Exception(result.message)
        }

        return result.data.toReviewModel()
    }

    override suspend fun makeOrder(
        wallet: Double,
        couponCode: String,
        couponDiscount: Double,
        currencyId: Int,
        addressId: Int,
        products: List<CartItem>,
        paymentType:String,
        paymentStatus:Boolean
    ): OrderResponseModel {
        val gson = GsonBuilder().serializeNulls().create()

        val dataString = gson.toJson(
            mapOf(
                PAYMENT_TYPE to paymentType,
                ORDER_TYPE to "normal",
                PAYMENT_STATUS to paymentStatus,
                ADDRESS_ID to addressId,
                WALLET_DISCOUNT to wallet,
                COUPON_DISCOUNT to couponDiscount,
                COUPON_CODE to couponCode,
                CURRENCY_ID to currencyId,
                PRODUCTS to products.map {
                    mapOf(
                        PRODUCT_ID to it.product.id,
                        DISCOUNT to 0.0,
                        VARIANT to it.variant,
                        QUANTITY to it.quantity,
                        SERIALIZED_OPTIONS to it.serializedOptions
                    )
                }
            )
        )

        val result = tryToExecute<BaseResponse<MakeOrderResponseData>> {
            client.post(MAKE_ORDER) {
                setBody(TextContent(dataString, ContentType.Application.Json))
            }
        }
        if (result.data == null) {
            throw Exception(result.message)
        }

        return result.data.toOrderResponseModel()
    }

    override suspend fun getCurrentOrders(): List<OrderModel> {
        val result = tryToExecute<BaseResponse<OrdersResponseData>> {
            client.get(CURRENT_ORDER)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }

        return result.data.data.map { it.toOrderModel() }
    }
//    fun decodeQueryParameter(encodedParam: String): String {
//        return URLDecoder.decode(encodedParam, StandardCharsets.UTF_8.toString())
//    }
    override suspend fun makePayment(
        partyID: String,
        partyIDType: String,
        amount: Double,
        concurency: String,
        payerMessage: String,
        payerNote: String
    ): PaymentModel? {
Log.d("OrderRepositoryImp","makePayment")
        val result = tryToExecute<BaseResponse<PaymentResponse>>

        {
//Log.d("/////////////////",amount.toInt().toString())
//            val decodedPayerMessage = URLDecoder.decode(payerMessage, StandardCharsets.UTF_8.toString())
//            val decodedPayeeNote = URLDecoder.decode(payerNote, StandardCharsets.UTF_8.toString())

            client.submitForm(
                url = "pay/momo",
                formParameters = parameters {
                    append("partyId", partyID)
                    append("partyIdType", partyIDType)
                    append("amount", amount.toInt().toString())
                    append("currency", concurency)
                    append("payerMessage", "decodedPayerMessage")
                    append("payeeNote", "decodedPayeeNote")
                }
            )
        }


        return result.data?.toPaymentModel()
    }


    override suspend fun getOrderWithId(id: Int): List<OrderModel> {
        val result = tryToExecute<BaseResponse<OrdersResponseData>> {
            client.get("$TRACK_ORDER/$id")
        }
        if (result.data == null) {
            throw Exception(result.message)
        }

        return result.data.data.map { it.toOrderModel() }
    }
    override suspend fun trackTransaction(id: String,        partyID: String,
                                          partyIDType: String,
                                          amount: Double,
                                          concurency: String,
                                          payerMessage: String,
                                          payerNote: String): TransactionModel {
//        val result = tryToExecute<BaseResponse<ResponseOfStatus>> {
//            client.get("pay/momo/getTransactionStatus/$id")
//        }
        Log.d("aaaaaaaaaaaaa",id)
        val result = tryToExecute<BaseResponse<ResponseOfStatus>>{
            client.get(
               "pay/momo/getTransactionStatus/$id",

            ){

            }
        }

        if (result.data == null) {
            throw Exception(result.message)
        }

        return result.data.transaction.toTransactionModel()
    }

    override fun getPreviousOrders(): Flow<PagingData<OrderModel>> {
        return Pager(
            config = PagingConfig(pageSize = PAGE_SIZE),
            pagingSourceFactory = {
                PreviousOrdersPagingSource(
                    previousOrdersRemoteSource
                )
            }
        ).flow
    }

    override suspend fun getCoubonDiscount(
        couponCode: String,
        products: List<ProductOFCoupon>
    ): CouponModel {
        val gson = GsonBuilder().serializeNulls().create()

        val dataString = gson.toJson(
            mapOf(
                COUPON_CODE to couponCode,
                PRODUCTS to products.map {
                    mapOf(
                        "id" to it.id,
                        "price" to it.price,
                        "variation" to it.variation,

                        )
                }
            )
        )

        val result = tryToExecute<BaseResponse<ResponseOfCoupon>> {
            client.post("getCoubonDiscount") {
                setBody(TextContent(dataString, ContentType.Application.Json))
            }
        }
        if (result.data == null) {
            throw Exception(result.message)
        }

        return result.data.toCouponModel()
    }


}