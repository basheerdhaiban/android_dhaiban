package com.semicolon.data.repository.remote.repository

import android.content.res.Resources.NotFoundException
import com.semicolon.data.repository.remote.mapper.toDefaultCurrency
import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.model.AppConfigDto
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.ChangePasswordUserDto
import com.semicolon.data.repository.remote.model.ChangePhoneResponseDto
import com.semicolon.data.repository.remote.model.CheckUserDto
import com.semicolon.data.repository.remote.model.CurrencyData
import com.semicolon.data.repository.remote.model.TokenDto
import com.semicolon.data.repository.remote.model.UserDataDto
import com.semicolon.data.repository.remote.model.profile_image.Data
import com.semicolon.domain.entity.Account
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.entity.ChangePasswordUser
import com.semicolon.domain.entity.DefaultCurrency
import com.semicolon.domain.entity.User
import com.semicolon.domain.repository.UserRepository
import com.semicolon.domain.utils.AuthorizationException
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.forms.submitFormWithBinaryData
import io.ktor.client.request.get
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.parameters
import java.io.File

class UserRepositoryImpl(client: HttpClient) : BaseRepository(client), UserRepository {
    override suspend fun registerNewUser(account: Account): String {
        val result = tryToExecute<BaseResponse<TokenDto>> {
            client.submitForm(
                url = "register",
                formParameters = parameters {
                    append("user_name", account.username)
                    append("email", account.email)
                    append("password", account.password)
                    append("phone", account.phone)
                    append("verification_method", account.verificationMethod.toString())
                }
            )
        }

        if (!result.success) {
            val errors = result.errors
            errors?.forEach { error ->
                if (error.key == "user_name") {
                    throw AuthorizationException.InvalidUsernameException(error.value)
                }
                if (error.key == "email") {
                    throw AuthorizationException.InvalidEmailException(error.value)
                }
                if (error.key == "phone") {
                    throw AuthorizationException.InvalidPhoneException(error.value)
                }
                if (error.key == "password") {
                    throw AuthorizationException.InvalidPasswordException(error.value)
                }
            }
        }

        return result.data?.token ?: throw NotFoundException()

    }

    override suspend fun activeUserAccount(verificationCode: String): User {
        val result = tryToExecute<BaseResponse<UserDataDto>> {
            client.submitForm(
                url = "activeAccount",
                formParameters = parameters {
                    append("virification_code", verificationCode)
                }
            )
        }

        if (!result.success) {
            val error = result.data?.error ?: "Invalid Otp Exception"
            throw AuthorizationException.InvalidOtpCodeException(error)
        }

        return result.data?.toEntity() ?: throw NotFoundException()
    }

    override suspend fun loginUserByPhoneNumber(phone: String, password: String): User {
        val result = tryToExecute<BaseResponse<UserDataDto>> {
            client.submitForm(
                url = "login",
                formParameters = parameters {
                    append("phone", phone)
                    append("password", password)
                }
            )
        }

        if (!result.success) {
            val error = result.data?.error ?: "Invalid inputs"
            if (result.data?.active == 0) {
                throw AuthorizationException.AccountActivationException(result.data.token ?: "")

            }
            throw AuthorizationException.InvalidInputsException(error)
        }


        return result.data?.toEntity() ?: throw NotFoundException()
    }

    override suspend fun loginUserByEmail(email: String, password: String): User {
        val result = tryToExecute<BaseResponse<UserDataDto>> {
            client.submitForm(
                url = "login",
                formParameters = parameters {
                    append("email", email)
                    append("password", password)
                }
            )
        }

        if (!result.success) {
            val error = result.data?.error ?: "Invalid inputs"
            if (result.data?.active == 0) {
                throw AuthorizationException.AccountActivationException(error)

            }
            throw AuthorizationException.InvalidInputsException(error)
        }

        return result.data?.toEntity() ?: throw NotFoundException()
    }


    override suspend fun getAvailableAppConfigurations(): AppConfig {
        val result = tryToExecute<BaseResponse<AppConfigDto>> {
            client.get("launch")
        }.data ?: throw NotFoundException()

        return result.toEntity()
    }
    override suspend fun resendOtp(): String {
        val result = tryToExecute<BaseResponse<String>> {
            client.get("reSendCode")
        }.data ?: throw NotFoundException()

        return result
    }

    override suspend fun checkIfUserExists(phoneOrEmail: String): String {
        val result = tryToExecute<BaseResponse<CheckUserDto>> {

            client.submitForm(
                url = "checNumberOrEmailExistance",
                formParameters = parameters { append("phone_email", phoneOrEmail) }
            )
        }

        if (!result.success) {
            val errorMessage = result.data?.error ?: "user not found"
            throw AuthorizationException.UserNotFoundException(errorMessage)
        }
        return result.data?.code.toString()
    }

    override suspend fun resetUserPassword(
        phoneOrEmail: String,
        otpVerificationCode: String,
        password: String
    ): User {
        val result = tryToExecute<BaseResponse<UserDataDto>> {
            client.submitForm(
                url = "resetPassword",
                formParameters = parameters {
                    append("phone_email", phoneOrEmail)
                    append("password", password)
                    append("virification_code", otpVerificationCode)
                }
            )
        }

        if (!result.success) {
            val errorMessage = result.data?.error ?: "Invalid data"
            throw AuthorizationException.UserNotFoundException(errorMessage)
        }

        return result.data?.toEntity() ?: throw NotFoundException()

    }

    override suspend fun getDefaultCurrency(): DefaultCurrency {
        val result = tryToExecute<BaseResponse<CurrencyData>> {
            client.get("currencies")
        }.data ?: throw NotFoundException()

        return result.toDefaultCurrency()
    }

    override suspend fun editNameAndEmail(name: String, email: String): User {
        val result = tryToExecute<BaseResponse<UserDataDto>> {
            client.submitForm(
                url = "editAccount",
                formParameters = parameters {
                    append("name", name)
                    append("email", email)
                }
            )
        }
        if (result.data == null) {
            throw Exception(result.errors?.get("name") ?: "Unknown")
        }
        return result.data.toEntity()
    }

    override suspend fun uploadImage(text: String, byteArray: ByteArray?):Boolean {
        val result = tryToExecute<BaseResponse<Data>> {
            client.submitFormWithBinaryData(
                url = "update_profil_image",
                formData = formData {
                    if (byteArray != null) {
                        append("photo", byteArray, Headers.build {

                            append(HttpHeaders.ContentDisposition, "filename=image.png")
                        }
                        )
                    }
        })
        }
        if (result.data == null) {
            throw Exception(result.errors?.get("name") ?: "Unknown")
        }
        return result.data.message == NotificationRepositoryImp.UPDATED

    }

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String
    ): ChangePasswordUser {
        val result = tryToExecute<BaseResponse<ChangePasswordUserDto>> {
            client.submitForm(
                url = "changePassword",
                formParameters = parameters {
                    append("oldPassword", oldPassword)
                    append("password", newPassword)
                }
            )
        }
        if (result.data == null) {
            throw Exception(
                result.errors?.get("oldPassword") ?: result.errors?.get("password") ?: "Unknown"
            )
        }
        return result.data.toEntity()
    }

    override suspend fun changePhoneNumberFirstStep(phoneNumber: String, password: String): String {
        val result = tryToExecute<BaseResponse<ChangePhoneResponseDto>> {
            client.submitForm(
                url = "changePhoneFirstStep",
                formParameters = parameters {
                    append("phone", phoneNumber)
                    append("password", password)
                }
            )
        }
        if (result.data == null) {
            throw Exception(
                result.errors?.get("phone") ?: result.errors?.get("password") ?: "Unknown"
            )
        }
        if (result.success.not()) {
            throw Exception(
                result.data.error
            )
        } else {
            return "Success"
        }
    }

    override suspend fun changePhoneNumberSecondStep(phoneNumber: String, otp: String): String {
        val result = tryToExecute<BaseResponse<ChangePhoneResponseDto>> {
            client.submitForm(
                url = "changePhoneLastStep",
                formParameters = parameters {
                    append("phone", phoneNumber)
                    append("virification_code", otp)
                }
            )
        }
        if (result.data == null) {
            throw Exception(
                result.errors?.get("phone") ?: result.errors?.get("virification_code") ?: "Unknown"
            )
        }

        if (result.success.not()) {
            throw Exception(
                result.data.error
            )
        } else {
            return "Success"
        }
    }
}