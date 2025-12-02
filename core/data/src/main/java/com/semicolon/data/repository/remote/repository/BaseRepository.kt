package com.semicolon.data.repository.remote.repository

import com.semicolon.domain.utils.BadRequestException
import com.semicolon.domain.utils.BadRequestExceptionTest
import com.semicolon.domain.utils.InternetException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

abstract class BaseRepository(val client: HttpClient) {
    suspend inline fun <reified T> tryToExecute(method: HttpClient.() -> HttpResponse): T {
        try {
            val response = client.method()
            if (response.status.isSuccess()) {
                return response.body()
            } else {
                throw InternetException()
            }
        } catch (e: Exception) {
            throw BadRequestExceptionTest(e.message.toString())
        }
    }
}