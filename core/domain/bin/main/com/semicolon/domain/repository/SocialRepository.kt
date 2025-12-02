package com.semicolon.domain.repository

import com.semicolon.domain.entity.ContactUsModel
import com.semicolon.domain.entity.SocialModel

interface SocialRepository {
   suspend fun getSocialContact() :SocialModel
   suspend fun contactWithCustomerService(
      name: String,
      subject: String,
      contact: String,
      email: String,

      ): ContactUsModel

}