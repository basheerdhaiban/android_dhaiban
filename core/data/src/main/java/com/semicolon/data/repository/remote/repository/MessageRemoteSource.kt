package com.semicolon.data.repository.remote.repository

import android.util.Log
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.send_new_message.Data
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.parameters
import io.ktor.http.parametersOf

class MessageRemoteSource(private val client: HttpClient) {
    companion object {
        const val NEWCONVERSATION = "new_conversation"
        const val PAGE = "page"
    }

    suspend fun getMessageIfExisted(
        page: Int, productId: Int,
        orderId: Int,
        user: Int,
        message: String
    ): BaseResponse<Data> {

        return client.submitForm (
            url = "pm/new_conversation?page=$page",
            formParameters = parameters {
                append("user", user.toString())
                append("order_id",orderId.toString())
                append("product_id",productId.toString())
                append("message",message.toString())


            },


        ).body()
    }
    suspend fun getAllInboxes(
        page: Int
    ): BaseResponse<com.semicolon.data.repository.remote.model.all_chats.Data> {
        return client.get("pm/user_conversation?page=$page") {
            
        }.body()

    }

    suspend fun getInboxById(
        inboxId: Int ,
        page: Int
    ): BaseResponse<com.semicolon.data.repository.remote.inboxby_id.Data> {
        return client.get("pm/chat/$inboxId?page=$page") {

        }.body()

    }

    suspend fun sendMessage(
        page: Int, inboxId: Int, message: String
    ): BaseResponse<Data> {

        Log.d("getMessageIfExistedmessage",message.toString())
        return client.submitForm (
            url = "pm/chat/send_message?page=$page",
            formParameters = parameters {

                append("inbox_id",inboxId.toString())
                append("message",message.toString())


            },


            ).body()
    }
}