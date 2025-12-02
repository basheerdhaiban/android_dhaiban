package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.ChangePasswordUserDto
import com.semicolon.data.repository.remote.model.UserDataDto
import com.semicolon.domain.entity.ChangePasswordUser
import com.semicolon.domain.entity.User

fun UserDataDto.toEntity(): User {
    return User(
        id = user?.id ?: 0,
        birthDate = user?.birthDate ?: "",
        gender = user?.gender ?: "",
        subscriptionList = user?.subscriptionList ?: 0,
        fax = user?.fax ?: "",
        providerId = user?.providerId ?: "",
        userType = user?.userType ?: "",
        name = user?.name ?: "",
        fName = user?.fName ?: "",
        lName = user?.lName ?: "",
        balance = user?.balance ?: 0.0,
        cityId = user?.cityId ?: "",
        email = user?.email ?: "",
        phone = user?.phone ?: "",
        address = user?.address ?: "",
        userToken = token ?: user!!.userToken,
        deviceToken =  user?.deviceToken ?: "",
        photo = user?.photo ?: "",
        lastLogin = user?.lastLogin ?: "",
        lastLogout = user?.lastLogout ?: "",
        roleId = user?.roleId ?: "",
        error = error ?: "",
        active = active ?: 10,
        verificationCode = verificationCode ?: 0
    )
}

fun ChangePasswordUserDto.toEntity(): ChangePasswordUser {
    return ChangePasswordUser(
        id = this.id ?: 0,
        birthDate = this.birthDate ?: "",
        gender = this.gender ?: "",
        subscriptionList = this.subscriptionList ?: 0,
        fax = this.fax ?: "",
        providerId = this.providerId ?: "",
        userType = this.userType ?: "",
        name = this.name ?: "",
        fName = this.fName ?: "",
        lName = this.lName ?: "",
        balance = this.balance ?: 0,
        cityId = this.cityId ?: "",
        email = this.email ?: "",
        note = this.note ?: "",
        phone = this.phone ?: "",
        address = this.address ?: "",
        token = this.userToken ?: "",
        deviceToken = this.deviceToken ?: "",
        photo = this.photo ?: "",
        lastLogin = this.lastLogin ?: "",
        lastLogout = this.lastLogout ?: "",
        roleId = this.roleId ?: "",
        error = this.error ?: "",
        oldPasswordError = this.oldPassword ?: ""
    )
}