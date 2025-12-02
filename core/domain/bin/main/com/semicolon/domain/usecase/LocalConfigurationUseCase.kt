package com.semicolon.domain.usecase

import com.semicolon.domain.entity.Strings
import com.semicolon.domain.repository.LocalConfigurationRepository
import com.semicolon.domain.repository.LocalizationRepository
import kotlinx.coroutines.flow.Flow

class LocalConfigurationUseCase(
    private val localRepository: LocalConfigurationRepository,
    private val localizationRepository: LocalizationRepository
) {
    suspend fun saveOnBoardingState(isCompleted: Boolean) {
        localRepository.saveOnBoardingState(isCompleted)
    }
    suspend fun saveIsActive(isActive: Boolean ) {
        localRepository.saveIsActive(isActive)
    }

    suspend fun readOnBoardingState(): Boolean {
        return localRepository.readOnBoardingState()
    }

    suspend fun saveUserCountry(country: String) {
        localRepository.saveUserCountry(country)
    }
     suspend fun saveIsFromCart(flag: Boolean) {
        localRepository.saveIsFromCart(flag)

    }

    suspend fun saveUserLanguage(language: String) {
        localRepository.saveUserLanguage(language)
    }

    suspend fun saveUserCurrency(currency: String) {
        localRepository.saveUserCurrency(currency)
    }
    suspend fun saveNameUserCurrency(currency: String) {
        localRepository.saveNameUserCurrency(currency)

    }


    suspend fun getUserLanguageAsFlow(): Flow<String> {
        return localRepository.getUserLanguageAsFlow()
    }

    suspend fun saveLayoutDirection(layoutDirection: String) {
        localRepository.saveLayoutDirection(layoutDirection)
    }

    suspend fun getLayoutDirection(): String {
        return localRepository.getLayoutDirection()
    }

    suspend fun getUserLanguage(): String {
        return localRepository.getUserLanguage()
    }

    suspend fun getIsActive(): Boolean {
        return localRepository.getIsActive()
    }

    suspend fun getUserCountry(): String {
        return localRepository.getUserCountry()
    }
    suspend fun getIsFromCart(): Boolean {
        return localRepository.getIsFromCart()
    }


    suspend fun getUserCurrency(): String {
        return localRepository.getUserCurrency()
    }
    suspend fun getUserNameCurrency(): String {
        return localRepository.getNameUserCurrency()
    }

    suspend fun saveUserToken(token: String) {
        localRepository.saveToken(token)
    }

    suspend fun getUserToken() = localRepository.getUserToken()


    suspend fun clearUserToken(token: String) {
        localRepository.clearToken()
    }

    suspend fun saveLanguageTemp(language: String) {
        localRepository.saveUserLanguageTemp(language)
    }

    suspend fun getLanguageTemp(): String {
        return localRepository.getUserLanguageTemp()
    }

    suspend fun saveLocalizationLanguage(url: String = "", lang: String) {
        return localizationRepository.saveLocalizationStringsToDb(url, lang)
    }

    suspend fun getStrings(): Flow<Strings> {
        return localizationRepository.getLocalStringsFromDb()
    }

    suspend fun saveStringResourceState(saved: Boolean) {
        localRepository.saveStringResourceState(saved)
    }

    suspend fun readStringResourceState(): Boolean {
        return localRepository.readStringResourceState()
    }

    suspend fun saveUserName(userName: String) {
        localRepository.saveUsername(userName)
    }

    suspend fun getUserName(): String = localRepository.getUsername()

    suspend fun getCurrencyId() = localRepository.getCurrencyId()

    suspend fun saveCurrencyId(currencyId: Int) = localRepository.saveCurrencyId(currencyId)

    suspend fun getCountryId() = localRepository.getCountryId()

    suspend fun saveCountryId(countryId: Int) = localRepository.saveCountryId(countryId)

    suspend fun getEmail() = localRepository.getEmail()
    suspend fun saveEmail(email: String) = localRepository.saveEmail(email)

    suspend fun getContactNumber() = localRepository.getContactNumber()
    suspend fun saveContactNumber(contactNumber: String) =
        localRepository.saveContactNumber(contactNumber)

    suspend fun saveFcmToken(token: String) = localRepository.saveFcmToken(token)
    suspend fun getFcmToken() = localRepository.getFcmToken()
    suspend fun saveTranscationCode(token: String) = localRepository.saveTransactionCode(token)
    suspend fun getTransactionCode() = localRepository.getTransactionCode()
    suspend fun setIsTherePaymentMomo(token: String) = localRepository.setIsTherePaymentMomo(token)
    suspend fun getIsTherePaymentMomo() = localRepository.getIsTherePaymentMomo()

    suspend fun saveImageUrl(image: String) = localRepository.saveImageUrl(image)
    suspend fun getImageUrl() = localRepository.getImageUrl()

    suspend fun saveCurrentIDOfCart(id: Int) {
        localRepository.saveCurrentIDOfCart(id)
    }

    suspend fun saveAddressIDOfCart(addressID: Int) {
        localRepository.saveAddressIDOfCart(addressID)
    }
    suspend fun savePromoCodeOfCart(promocode: String) {
        localRepository.savePromoCodeOfCart(promocode)
    }
    suspend fun savePaidFromWalletOfCart(amount: Double) {
        localRepository.savePaidFromWalletOfCart(amount)
    }
    suspend fun savePromoOfDiscountCart(amount: Double) {
        localRepository.savePromoOfDiscountCart(amount)
    }
    suspend fun getCurrentIDOfCart() = localRepository.getCurrentIDOfCart()
    suspend fun getAddressIdOfCart() = localRepository.getAddressIdOfCart()
    suspend fun getPromoCodeFromCart() = localRepository.getPromoCodeFromCart()
    suspend fun getPaidFromWallet() = localRepository.getPaidFromWallet()
    suspend fun getPromoOfDiscountCart() = localRepository.getPromoOfCartDiscount()

}