package com.semicolon.data.repository.remote.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.semicolon.data.repository.remote.mapper.toMessageItemModel
import com.semicolon.data.repository.remote.mapper.toNewMessageDataModel
import com.semicolon.data.repository.remote.mapper.toNewMessagesDataEntity
import com.semicolon.data.repository.remote.mapper.toProductInMessageItemModel
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.send_new_message.Data
import com.semicolon.data.repository.remote.paging.AllChatsPagingSource
import com.semicolon.data.repository.remote.paging.InboxByIdPagingSource
import com.semicolon.data.repository.remote.paging.MessagePagingSource
import com.semicolon.data.repository.remote.paging.SendMessagePagingSource
import com.semicolon.domain.entity.ChatModel
import com.semicolon.domain.entity.DataMessagesItemModel
import com.semicolon.domain.entity.InboxsModel
import com.semicolon.domain.entity.MessagesItemModel
import com.semicolon.domain.entity.ProductXModel
import com.semicolon.domain.repository.MessageRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImp(
    client: HttpClient,
    private val messageRemoteSource: MessageRemoteSource
) : BaseRepository(client), MessageRepository {
    override fun getFromsendNewMessage(
        productId: Int,
        orderId: Int,
        user: Int,
        message: String
    ): Flow<PagingData<ChatModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                MessagePagingSource(
                    messageRemoteSource = messageRemoteSource,
                    productId = productId,
                    orderId = orderId,
                    user = user,
                    message = message
                )
            }
        ).flow
    }

    override suspend fun sendNewMessage(
        productId: Int,
        orderId: Int,
        user: Int,
        message: String
    ): ChatModel {
        val result = tryToExecute<BaseResponse<Data>> {

            client.submitForm(
                url = "pm/new_conversation",
                formParameters = parameters {
                    append("user", 128.toString())
                    append("order_id", orderId.toString())
                    append("product_id", productId.toString())
                    append("message", message.toString())

                })


        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toNewMessageDataModel()
    }

    override suspend fun sendMessage(inboxId: Int, message: String): Flow<PagingData<ChatModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                SendMessagePagingSource(
                    messageRemoteSource = messageRemoteSource,
                    message = message,
                    inboxId = inboxId
                )
            }
        ).flow
    }

    override suspend fun getMessage(inboxId: Int): List<ChatModel> {
        val result = tryToExecute<BaseResponse<Data>> {
            client.get("pm/chat/$inboxId")
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.messages.toNewMessagesDataEntity()
    }

    override suspend fun getAllMessage(): Flow<PagingData<InboxsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                AllChatsPagingSource(messageRemoteSource = messageRemoteSource)
            }
        ).flow
    }

    override suspend fun getInboxById(inboxId: Int): Flow<PagingData<MessagesItemModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 2,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                InboxByIdPagingSource(messageRemoteSource = messageRemoteSource,inboxId)
            }
        ).flow    }

    override suspend fun getMetaDataForMessageByID(inboxId: Int): ProductXModel {
        val result = tryToExecute<BaseResponse<com.semicolon.data.repository.remote.inboxby_id.Data>> {
            client.get("pm/chat/$inboxId")
        }
        result.data?.product?.title?.let { Log.d("getMetaDataForMessageByID", it) }
        if (result.data == null) {
            throw Exception(result.message)

        }
    return result.data.product.toProductInMessageItemModel()}

}