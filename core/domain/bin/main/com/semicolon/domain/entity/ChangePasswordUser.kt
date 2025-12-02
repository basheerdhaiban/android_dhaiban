package com.semicolon.domain.entity

data class ChangePasswordUser(
    val id: Int,
    val birthDate: String,
    val gender: String,
    val subscriptionList: Int,
    val fax: String,
    val providerId: String,
    val userType: String,
    val name: String,
    val fName: String,
    val lName: String,
    val balance: Int,
    val cityId: String,
    val email: String,
    val note: String,
    val phone: String,
    val address: String,
    val token: String,
    val deviceToken: String,
    val photo: String,
    val lastLogin: String,
    val lastLogout: String,
    val roleId: String,
    val error :String = "",
    val oldPasswordError: String = ""
)
