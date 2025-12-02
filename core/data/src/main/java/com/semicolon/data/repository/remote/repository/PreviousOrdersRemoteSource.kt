package com.semicolon.data.repository.remote.repository

import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.order.PreviousOrdersData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PreviousOrdersRemoteSource(private val client: HttpClient) {
    companion object{
        const val PREVIOUS_ORDERS = "previousOrders"
        const val PAGE = "page"
    }

    suspend fun getPreviousOrders(page: Int): BaseResponse<PreviousOrdersData>{
        return client.get(PREVIOUS_ORDERS){
            parameter(PAGE, page)
        }.body()
    }
}