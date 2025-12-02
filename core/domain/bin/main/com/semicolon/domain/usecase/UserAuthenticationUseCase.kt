package com.semicolon.domain.usecase

import com.semicolon.domain.entity.Account
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.entity.User
import com.semicolon.domain.repository.LocalConfigurationRepository
import com.semicolon.domain.repository.UserRepository
import java.io.File

class UserAuthenticationUseCase(
    private val userRepository: UserRepository,
    private val localRepository: LocalConfigurationRepository,
    private val validation: UserValidationUseCase,
) {

    suspend fun signUpUser(account: Account): String {
        return userRepository.registerNewUser(account)
    }

    suspend fun activateUserAccount(verificationCode: String): String {
        val result = userRepository.activeUserAccount(verificationCode)
        saveUsername(result.name)
        saveUserImage(result.photo)
        saveContactNumber(result.phone)
        saveEmail(result.email)
        saveUserToken(result.userToken)
        return result.userToken
    }

    suspend fun loginUserByPhoneNumber(phone: String, password: String): User {
        val result = userRepository.loginUserByPhoneNumber(phone, password)
        val active= result.active
        if (active!=0){
            saveUserToken(result.deviceToken)
            saveUsername(result.name)
            saveUserImage(result.photo)
            saveContactNumber(result.phone)
            saveEmail(result.email)
            result.deviceToken
            return result

        }
        else{
            // return 0 mean no device token
        return result

        }
    }

    suspend fun loginUserByEmail(email: String, password: String): User {
        val result = userRepository.loginUserByEmail(email, password)
       val active= result.active
        if (active!=0){
        saveUserToken(result.deviceToken)
        saveUsername(result.name)
        saveUserImage(result.photo)
        saveContactNumber(result.phone)
        saveEmail(result.email)
            return result
        }
        else{
            // return 0 mean no device token
            return result

        }
    }

    private suspend fun saveUserToken(token: String) {
        localRepository.saveToken(token)
    }

     suspend fun uploadImage(text: String, byteArray: ByteArray?):Boolean {
        return userRepository.uploadImage(text, byteArray)


    }

    private suspend fun saveUsername(username: String) {
        localRepository.saveUsername(username)
    }

    private suspend fun saveUserImage(image: String) {
        localRepository.saveImageUrl(image)
    }

    private suspend fun saveContactNumber(contactNumber: String) {
        localRepository.saveContactNumber(contactNumber)
    }

    private suspend fun saveEmail(email: String) {
        localRepository.saveEmail(email)
    }

    suspend fun getAppConfig(): AppConfig {
        return userRepository.getAvailableAppConfigurations()
    }

    suspend fun getLayoutDirection(lang: String): String {
        val configs = userRepository.getAvailableAppConfigurations()
        return configs.languages.find { it.code == lang }?.dir ?: ""
    }

    suspend fun checkIfUserExists(phoneOrEmail: String): String {
        return userRepository.checkIfUserExists(phoneOrEmail)
    }
    suspend fun saveProfileImage (){

    }

    suspend fun resetUserPassword(
        phoneOrEmail: String,
        otpVerificationCode: String,
        password: String
    ): User {
        return userRepository.resetUserPassword(
            phoneOrEmail = phoneOrEmail,
            otpVerificationCode = otpVerificationCode,
            password = password
        )
    }
    suspend fun resendOtp(

    ): String {
        return userRepository.resendOtp(

        )
    }

    suspend fun changeNameAndEmail(name: String, email: String) =
        userRepository.editNameAndEmail(name, email)

    suspend fun changeUserPassword(oldPassword: String, newPassword: String) =
        userRepository.changePassword(oldPassword, newPassword)

    suspend fun changePhoneNumberFirstStep(newNumber: String, password: String) =
        userRepository.changePhoneNumberFirstStep(newNumber, password)

    suspend fun changePhoneNumberSecondStep(number: String, otp: String) =
        userRepository.changePhoneNumberSecondStep(number, otp)
}