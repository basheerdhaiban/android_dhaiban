package com.semicolon.domain.repository

import com.semicolon.domain.entity.Account
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.entity.ChangePasswordUser
import com.semicolon.domain.entity.DefaultCurrency
import com.semicolon.domain.entity.User
import java.io.File

interface UserRepository {

    suspend fun registerNewUser(account: Account): String
    suspend fun activeUserAccount(verificationCode: String): User
    suspend fun loginUserByPhoneNumber(phone: String, password: String): User
    suspend fun loginUserByEmail(email: String, password: String): User
    suspend fun getAvailableAppConfigurations(): AppConfig
    suspend fun checkIfUserExists(phoneOrEmail: String): String
    suspend fun resetUserPassword(
        phoneOrEmail: String,
        otpVerificationCode: String,
        password: String
    ): User
    suspend fun resendOtp(): String
    suspend fun getDefaultCurrency(): DefaultCurrency

    suspend fun editNameAndEmail(name: String, email: String): User
    suspend fun uploadImage (text: String, byteArray: ByteArray?):Boolean

    suspend fun changePassword(oldPassword: String, newPassword: String) : ChangePasswordUser

    suspend fun changePhoneNumberFirstStep(phoneNumber: String, password: String): String
    suspend fun changePhoneNumberSecondStep(phoneNumber: String, otp: String): String

}