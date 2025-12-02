package com.semicolon.data.repository.remote.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserDataDto(
    @SerializedName("user") val user: UserDto? = null,
    @SerializedName("token") val token: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("active") val active: Int? = null,
    @SerializedName("verification_code") val verificationCode: Int? = null
)

@Serializable
data class UserDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("birth_date") val birthDate: String? = null,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("subscribtion_list") val subscriptionList: Int = 0,
    @SerializedName("fax") val fax: String? = null,
    @SerializedName("provider_id") val providerId: String? = null,
    @SerializedName("user_type") val userType: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("f_name") val fName: String = "",
    @SerializedName("l_name") val lName: String = "",
    @SerializedName("balance") val balance: Double = 0.0,
    @SerializedName("city_id") val cityId: String? = null,
    @SerializedName("email") val email: String = "",
    @SerializedName("phone") val phone: String = "",
    @SerializedName("address") val address: String? = null,
    @SerializedName("token") val userToken: String = "",
    @SerializedName("device_token") val deviceToken: String? = null,
    @SerializedName("photo") val photo: String? = null,
    @SerializedName("last_login") val lastLogin: String? = null,
    @SerializedName("last_logout") val lastLogout: String? = null,
    @SerializedName("role_id") val roleId: String? = null,
)

@Serializable
data class ChangePasswordUserDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("birth_date") val birthDate: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("subscribtion_list") val subscriptionList: Int? = null,
    @SerializedName("fax") val fax: String? = null,
    @SerializedName("provider_id") val providerId: String? = null,
    @SerializedName("user_type") val userType: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("f_name") val fName: String? = null,
    @SerializedName("l_name") val lName: String? = null,
    @SerializedName("balance") val balance: Int? = null,
    @SerializedName("city_id") val cityId: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("token") val userToken: String? = null,
    @SerializedName("device_token") val deviceToken: String? = null,
    @SerializedName("note") val note: String? = null,
    @SerializedName("photo") val photo: String? = null,
    @SerializedName("last_login") val lastLogin: String? = null,
    @SerializedName("last_logout") val lastLogout: String? = null,
    @SerializedName("role_id") val roleId: String? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("oldPassword") val oldPassword: String? = null,
)
data class ChangePhoneUserDto(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("birth_date") val birthDate: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("subscribtion_list") val subscriptionList: Int? = null,
    @SerializedName("fax") val fax: String? = null,
    @SerializedName("provider_id") val providerId: String? = null,
    @SerializedName("user_type") val userType: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("f_name") val fName: String? = null,
    @SerializedName("l_name") val lName: String? = null,
    @SerializedName("balance") val balance: Int? = null,
    @SerializedName("city_id") val cityId: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("phone") val phone: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("token") val userToken: String? = null,
    @SerializedName("device_token") val deviceToken: String? = null,
    @SerializedName("note") val note: String? = null,
    @SerializedName("photo") val photo: String? = null,
    @SerializedName("last_login") val lastLogin: String? = null,
    @SerializedName("last_logout") val lastLogout: String? = null,
    @SerializedName("role_id") val roleId: String? = null,
    @SerializedName("error") val error: String? = null,
)

@Serializable
data class ChangePhoneResponseDto(
    @SerializedName("virification_code") val verificationCode: Int? = null,
    @SerializedName("error") val error: String? = null,
)