package com.semicolon.dhaiban.di

import android.R.attr.level
import com.semicolon.data.repository.utils.authorizationIntercept
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.serialization.gson.gson
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import okhttp3.OkHttp
import org.koin.dsl.module

val networkModule = module {
    single {
        val client = HttpClient(CIO) {
            defaultRequest {
                header(
                    key = "Content-Type",
                    value = "multipart/form-data; boundary=<calculated when request is sent>"
                )
                url("https://admin.dhaibantrading.com/api/")
//                url("http://192.168.1.16:8000/api/")
//                url("https://dhaiban.gostcode.com/api/")
//                url(" https://ef5f39ca1f569a6ad72d85f22eca13a8.serveo.net/api/")
            }
            expectSuccess = true
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println("HTTP Client: $message")
                    }
                }
            }
            install(ContentNegotiation) {
                gson()
            }
        }
        authorizationIntercept(client)
        client
    }
}

