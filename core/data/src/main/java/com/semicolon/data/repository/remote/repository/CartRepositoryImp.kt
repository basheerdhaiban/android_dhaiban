package com.semicolon.data.repository.remote.repository

import android.content.res.Resources.NotFoundException
import android.util.Log
import com.google.gson.GsonBuilder
import com.semicolon.data.repository.remote.mapper.toCartItem
import com.semicolon.data.repository.remote.mapper.toDefaultAddress
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.cart.CartDataDto
import com.semicolon.data.repository.remote.repository.OrderRepositoryImp.Companion.gson
import com.semicolon.domain.entity.CartItem
import com.semicolon.domain.entity.ChoiceItemModel
import com.semicolon.domain.entity.PayTabsModel
import com.semicolon.domain.entity.productdetails.CartDataModel
import com.semicolon.domain.entity.productdetails.DefaultAddress
import com.semicolon.domain.repository.CartRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.content.TextContent
import io.ktor.http.contentType
import io.ktor.http.parameters

class CartRepositoryImp(client: HttpClient) : CartRepository, BaseRepository(client) {

    companion object {
        const val CART_PRODUCTS = "cartProducts"
        const val ADD_CART = "addEditCart"
        const val REMOVE_CART = "removeFromCart"
        const val IDENTIFIER = "identifier"
        const val PRODUCT_ID = "product_id"
        const val QUANTITY = "quantity"
        const val VARIANT = "variant"
        const val CURRENCY_ID = "currency_id"
        const val IS_UPDATED = "isUpdated"
        const val SELLER_ID = "seller_id"
        const val SERIALIZED_OPTIONS = "serialized_options"
        const val CHOICES = "choices"
        const val COLOR = "color"
        const val PAYTABS_INITIATE = "/paytabs/initiate"
        const val PAYTABS_STATUS = "/paytabs/status"
        const val PAYTABS_RETURN = "/paytabs/return"
        const val PAYTABS_CALLBACK = "/paytabs/callback"
        const val ORDER_ID = "order_id"
        const val TRAN_REF = "tran_ref"
    }

    override suspend fun paytabsInitiate(order_id: Int): PayTabsModel? {
        try {
            Log.d("paytabsInitiate","PaymentMethodsType.PAYTABS")
            val dataString = gson.toJson(mapOf( ORDER_ID to order_id))
            Log.d("paytabsInitiate","PayTabs Initiate: $dataString")
            val result = client.post("https://admin.dhaibantrading.com/api/paytabs/initiate") {
                contentType(ContentType.Application.Json)
                setBody(dataString)
            }.body<PayTabsModel>()

            if (result.redirect_url.isNullOrEmpty() == true) {
                throw Exception(result.toString())
            }
            return result

//            val result = tryToExecute<BaseResponse<PayTabsModel>> {
//                client.post(PAYTABS_INITIATE) {
//                    setBody(TextContent(dataString, ContentType.Application.Json))
//                }
//            }
//            if (result.data == null) {
//                throw Exception(result.message)
//            }
//            return result.data
        } catch (e: Exception) {
            print(e.message)
            throw Exception(e.message)
        }
    }
    override suspend fun paytabsStatus(tran_ref: String?): PayTabsModel? {
        try {
            val result = client.get("https://admin.dhaibantrading.com/api/paytabs/status?tran_ref=$tran_ref") {}
                .body<PayTabsModel>()

            if (result.redirect_url.isNullOrEmpty() == true) {
                throw Exception(result.toString())
            }
            return result

//            val result = tryToExecute<BaseResponse<PayTabsModel>> {
//                client.post(PAYTABS_STATUS) {
//                    setBody(TextContent(dataString, ContentType.Application.Json))
//                }
//            }
//            if (result.data == null) {
//                throw Exception(result.message)
//            }
//            return result.data
        } catch (e: Exception) {
            print(e.message)
            throw Exception(e.message)
        }
    }
    /*override suspend fun paytabsReturn(tran_ref: String?): PayTabsModel? {
        try {
//            Log.d("OrderRepositoryImp",PaymentMethodsType.PAYTABS)
            val dataString = gson.toJson(
                mapOf(
                    tran_ref to "tran_ref",
                )
            )

            val result = tryToExecute<BaseResponse<PayTabsModel>> {
                client.post(PAYTABS_RETURN) {
                    setBody(TextContent(dataString, ContentType.Application.Json))
                }
            }
            if (result.data == null) {
                throw Exception(result.message)
            }
            return result.data
        } catch (e: Exception) {
            print(e.message)
            throw Exception(e.message)
        }
    }
    override suspend fun paytabsCallBack(tran_ref: String, cart_id: String): PayTabsModel? {
        try {
//            Log.d("OrderRepositoryImp",PaymentMethodsType.PAYTABS)
            val dataString = gson.toJson(
                mapOf(
                    tran_ref to "tran_ref",
                    cart_id to "cart_id",
                )
            )

            val result = tryToExecute<BaseResponse<PayTabsModel>> {
                client.post(PAYTABS_CALLBACK) {
                    setBody(TextContent(dataString, ContentType.Application.Json))
                }
            }
            if (result.data == null) {
                throw Exception(result.message)
            }
            return result.data
        } catch (e: Exception) {
            print(e.message)
            throw Exception(e.message)
        }
    }*/

    override suspend fun getCartProducts(): CartDataModel {
        val result = tryToExecute<BaseResponse<CartDataDto>> {
            client.get(CART_PRODUCTS)
        }
        if (result.data?.data == null) {
            throw Exception(result.message)
        }
        return CartDataModel(
            result.data.data.map { it.toCartItem() },
            result.data.address?.toDefaultAddress() ?: DefaultAddress(0, "",0.0,0.0)
        )
    }

    override suspend fun addToCart(
        identifier: Long?,
        productId: Int?,
        quantity: Int,
        variant: String?,
        currencyId: Int?,
        isUpdated: Boolean?,
        colorId: Int?,
        variantList: List<ChoiceItemModel>
    ): String {
        println("currencyId: " + currencyId.toString())
        val gson = GsonBuilder().serializeNulls().create()

        val dataString = gson.toJson(
            mapOf(
                IDENTIFIER to identifier,
                IS_UPDATED to false,
                PRODUCT_ID to productId,
                QUANTITY to quantity,
                SELLER_ID to null,
                CURRENCY_ID to currencyId,
                VARIANT to variant,
                SERIALIZED_OPTIONS to if (colorId != null)
                    mapOf(
                        COLOR to colorId,
                        CHOICES to variantList.map {
                            mapOf(
                                "id" to it.id,
                                "title" to it.title,
                                "parentId" to it.parentId,
                                "parentTitle" to it.parentTitle
                            )
                        }
                    )
                else {
                    mapOf(
                        CHOICES to variantList.map {
                            mapOf(
                                "id" to it.id,
                                "title" to it.title,
                                "parentId" to it.parentId,
                                "parentTitle" to it.parentTitle
                            )
                        }
                    )
                }
            )
        )
        val result = tryToExecute<BaseResponse<CartDataDto>> {
            client.post(ADD_CART) {
                setBody(TextContent(dataString, ContentType.Application.Json))
            }
        }
        if (result.data == null)
            throw Exception(result.message)
        return if (result.success) "Added" else "Not Added"
    }

    override suspend fun removeFromCart(cartItemId: Int): List<CartItem> {
        val result = tryToExecute<BaseResponse<CartDataDto>> {
            client.submitForm(
                url = REMOVE_CART,
                formParameters = parameters {
                    append("id", cartItemId.toString())
                }
            )
        }.data?.data ?: throw NotFoundException()
        return result.map { it.toCartItem() }
    }
}