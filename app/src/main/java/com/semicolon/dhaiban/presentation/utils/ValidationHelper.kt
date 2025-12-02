package com.semicolon.dhaiban.presentation.utils

import android.content.Context
import android.util.Log
import com.semicolon.dhaiban.R
import com.semicolon.domain.utils.ValidationState
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat
import com.semicolon.domain.usecase.UserValidationUseCase.Companion.emailRegex

class ValidationHandler(private val context: Context) {

    fun validateEmail(email: String?): String? {
        var validNumber:String? = null
        if (!email.isNullOrEmpty()){
            validNumber = when {
                email.isBlank() -> handleValidation(ValidationState.BLANK_EMAIL)
                !email.matches(emailRegex) -> handleValidation(ValidationState.INVALID_EMAIL)
                else -> handleValidation(ValidationState.SUCCESS)
            }
            return validNumber
        } else validNumber = handleValidation(ValidationState.BLANK_EMAIL)
        return validNumber
    }

    fun validatePhoneNumber(countryCode: String, phoneNumber: String?): String? {
        var validNumber:String? = null
        val phoneUtil = PhoneNumberUtil.getInstance()
        try {
            // Normalize input (e.g., remove extra spaces, dashes)
            val cleanedNumber = phoneNumber?.replace("\\s|-".toRegex(), "")
            val regionCode = phoneUtil.getRegionCodeForCountryCode(countryCode.replace("+", "").toInt())

            val numberProto = phoneUtil.parse(cleanedNumber, regionCode)

            if (phoneUtil.isValidNumber(numberProto)) {
                // Return formatted international number like: +201008812862
                validNumber = phoneUtil.format(numberProto, PhoneNumberFormat.E164)
                println("✅ ${context.getString(R.string.valid_phone)}: $validNumber")
            } else {
                println("❌ ${context.getString(R.string.invalid_phone_number)}: $validNumber")
            }
        } catch (e: Exception) {
            validNumber = null // Parsing failed
            println("❌ Invalid phone number")
        }
        return validNumber
    }

    fun handleValidation(validationStat: ValidationState): String {
        return when (validationStat) {
            ValidationState.BLANK_EMAIL -> context.getString(R.string.blank_email)
            ValidationState.INVALID_EMAIL -> context.getString(R.string.invalid_email)
            ValidationState.BLANK_PHONE -> context.getString(R.string.blank_phone)
            ValidationState.INVALID_PHONE -> context.getString(R.string.invalid_phone)
            ValidationState.BLANK_PASSWORD -> context.getString(R.string.blank_password)
            ValidationState.SHORT_PASSWORD -> context.getString(R.string.short_password)
            ValidationState.BLANK_USER_NAME -> context.getString(R.string.blank_username)
            ValidationState.SHORT_USER_NAME -> context.getString(R.string.short_username)
            ValidationState.BLANK_OTP -> context.getString(R.string.blank_otp)
            ValidationState.SHORT_OTP -> context.getString(R.string.short_otp)
            ValidationState.INVALID_OTP -> context.getString(R.string.invalid_otp)
            ValidationState.NOT_SAME -> context.getString(R.string.not_similar_passwords)
            ValidationState.SUCCESS -> ""
        }
    }
}