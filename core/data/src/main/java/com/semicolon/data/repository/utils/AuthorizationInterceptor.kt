package com.semicolon.data.repository.utils

import android.util.Log
import com.semicolon.domain.repository.LocalConfigurationRepository
import com.semicolon.domain.utils.AuthorizationException
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.plugin
import io.ktor.client.request.headers
import org.koin.core.scope.Scope

fun Scope.authorizationIntercept(client: HttpClient) {

    val localConfigurationGateway: LocalConfigurationRepository by inject()

    client.plugin(HttpSend).intercept { request ->

        val accessToken = localConfigurationGateway.getUserToken()
        val languageCode = localConfigurationGateway.getUserLanguage().lowercase()
        val CurrencyCode = localConfigurationGateway.getNameUserCurrency()
        val isFromCart = localConfigurationGateway.getIsFromCart()
Log.d("asasasasasasasa",CurrencyCode.toString())
Log.d("authorizationIntercept",CurrencyCode.toString())
        request.headers {
            append("Authorization", accessToken)
            append("lang", languageCode.ifEmpty { "en" })
//            append("Accept" , "application/json")
//            if (isFromCart){
            append("currency", CurrencyCode)
        }

        println("Current token = $accessToken")

        val originalCall = execute(request)
        if (originalCall.response.status.value == 401) {
            throw AuthorizationException("You are not logged in")
        } else {
            originalCall
        }
    }
}
