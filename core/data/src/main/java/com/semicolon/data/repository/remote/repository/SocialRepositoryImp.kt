package com.semicolon.data.repository.remote.repository

import android.util.Log
import com.semicolon.data.repository.remote.mapper.toContactUsModel
import com.semicolon.data.repository.remote.mapper.toOrderModel
import com.semicolon.data.repository.remote.mapper.toPaymentModel
import com.semicolon.data.repository.remote.mapper.toSocialModel
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.contact_as.ContactUs
import com.semicolon.data.repository.remote.model.order.OrdersResponseData
import com.semicolon.data.repository.remote.model.payment.PaymentResponse
import com.semicolon.data.repository.remote.model.social.Data
import com.semicolon.domain.entity.ContactUsModel
import com.semicolon.domain.entity.PaymentModel
import com.semicolon.domain.entity.SocialModel
import com.semicolon.domain.entity.orders.OrderModel
import com.semicolon.domain.repository.SocialRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.http.parameters

class SocialRepositoryImp(client: HttpClient,) :SocialRepository ,BaseRepository(client) {
    override suspend fun getSocialContact() :SocialModel {
        val result = tryToExecute<BaseResponse<Data>> {
            client.get("social")
        }
        if (result.data == null) {
            throw Exception(result.message)
        }

        return result.data.social.toSocialModel()   }

    override suspend fun contactWithCustomerService(
        name: String,
        subject: String,
        contact: String,
        email: String,

    ): ContactUsModel {
        Log.d("OrderRepositoryImp","makePayment")
        val result = tryToExecute<ContactUs> {

            client.submitForm(
                url = "contactUs",
                formParameters = parameters {
                    append("name", name)
                    append("subject", subject)
                    append("email", email.toString())
                    append("content", contact)


        })}


        return result.toContactUsModel()
    }

}