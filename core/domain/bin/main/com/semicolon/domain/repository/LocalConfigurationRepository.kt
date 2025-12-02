package com.semicolon.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocalConfigurationRepository {

    suspend fun saveOnBoardingState(completed: Boolean)

    suspend fun readOnBoardingState(): Boolean

    suspend fun saveUserCountry(country: String)
    suspend fun saveIsActive(isActive: Boolean)

    suspend fun saveUserLanguage(language: String)

    suspend fun saveUserCurrency(currency: String)
    suspend fun saveNameUserCurrency(currency: String)
    suspend fun getNameUserCurrency(): String


    suspend fun getUserCountry(): String

    suspend fun getUserLanguageAsFlow(): Flow<String>

    suspend fun getUserLanguage(): String

    suspend fun getUserCurrency(): String

    suspend fun saveLayoutDirection(direction: String)

    suspend fun getLayoutDirection(): String

    suspend fun saveToken(token: String)
    suspend fun getIsFromCart(): Boolean
    suspend fun saveIsFromCart(flag: Boolean=false)
    suspend fun getUserToken(): String
    suspend fun saveUserEmail(token: String)
    suspend fun getUserEmail():String
    suspend fun saveUserid(token: String)

    suspend fun getUserid():String
    suspend fun clearToken()

    suspend fun saveUserLanguageTemp(language: String)

    suspend fun getUserLanguageTemp(): String

    suspend fun saveStringResourceState(saved: Boolean)

    suspend fun readStringResourceState(): Boolean

    suspend fun saveUsername(username: String)
    suspend fun getUsername(): String
    suspend fun saveCurrencyId(currencyId: Int)
    suspend fun getCurrencyId(): Int
    suspend fun saveCountryId(countryId: Int)
    suspend fun getCountryId(): Int
    suspend fun saveContactNumber(contactNumber: String)
    suspend fun getContactNumber(): String

    suspend fun saveEmail(email: String)
    suspend fun getEmail(): String
    suspend fun saveFcmToken(token: String)
    suspend fun getFcmToken(): String
    suspend fun saveTransactionCode(token: String)
    suspend fun getTransactionCode(): String
    suspend fun setIsTherePaymentMomo(token: String)
    suspend fun getIsTherePaymentMomo(): String
    suspend fun saveImageUrl(image: String)
    suspend fun getImageUrl(): String
    suspend fun getIsActive():Boolean

    suspend fun saveCurrentIDOfCart(id: Int)
    suspend fun saveAddressIDOfCart(addressID: Int)
    suspend fun savePromoCodeOfCart(promoCode: String)
    suspend fun savePaidFromWalletOfCart(addressID: Double)
    suspend fun savePromoOfDiscountCart(promo: Double)
    suspend fun getCurrentIDOfCart():Int
    suspend fun getAddressIdOfCart():Int
    suspend fun getPromoCodeFromCart() :String
    suspend fun getPaidFromWallet():Double
    suspend fun getPromoOfCartDiscount() :Double


}