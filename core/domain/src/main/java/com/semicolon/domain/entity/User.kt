package com.semicolon.domain.entity

data class User(
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
    val balance: Double,
    val cityId: String,
    val email: String,
    val phone: String,
    val address: String,
    val userToken: String,
    val deviceToken: String,
    val photo: String,
    val lastLogin: String,
    val lastLogout: String,
    val roleId: String,
    val error: String,
    val active: Int,
    val verificationCode: Int
)
