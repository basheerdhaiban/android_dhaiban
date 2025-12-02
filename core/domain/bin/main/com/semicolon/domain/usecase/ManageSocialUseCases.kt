package com.semicolon.domain.usecase

import com.semicolon.domain.repository.SocialRepository

class ManageSocialUseCases(private val repository: SocialRepository) {
    suspend fun getSocialContact() = repository.getSocialContact()
    suspend fun contactUs(
        name: String,
        subject: String,
        contact: String,
        email: String,
    ) = repository.contactWithCustomerService(name, subject, contact, email)
}