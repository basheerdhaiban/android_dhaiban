package com.semicolon.data.repository.remote.repository

import com.semicolon.data.repository.remote.mapper.toFaqItemModel
import com.semicolon.data.repository.remote.mapper.toFaqModel
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.faq.FaqItemDto
import com.semicolon.data.repository.remote.model.faq.FaqTypeDto
import com.semicolon.domain.entity.FaqItemModel
import com.semicolon.domain.entity.FaqModel
import com.semicolon.domain.repository.FaqRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class FaqRepositoryImp(client: HttpClient) : BaseRepository(client), FaqRepository {
    companion object {
        const val FAQ_TYPES = "getFaqTypes"
        const val GET_FAQS = "getFaqs"

    }

    override suspend fun getFaqTypes(): List<FaqModel> {
        val result = tryToExecute<BaseResponse<List<FaqTypeDto>>> {
            client.get(FAQ_TYPES)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.map { it.toFaqModel() }
    }

    override suspend fun getFaqItems(id: Int): List<FaqItemModel> {
        val result = tryToExecute<BaseResponse<List<FaqItemDto>>> {
            client.get("${GET_FAQS}/$id")
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.map { it.toFaqItemModel() }
    }
}