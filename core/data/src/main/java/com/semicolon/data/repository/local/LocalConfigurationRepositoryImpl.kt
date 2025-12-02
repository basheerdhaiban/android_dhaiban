package com.semicolon.data.repository.local

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.semicolon.data.repository.local.LocalConfigurationRepositoryImpl.PreferencesKey.preferenceFile
import com.semicolon.domain.repository.LocalConfigurationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalConfigurationRepositoryImpl(context: Context) : LocalConfigurationRepository {
    override suspend fun saveNameUserCurrency(currency: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.nameOfcurrency] = currency
        }
    }

    override suspend fun getNameUserCurrency(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.nameOfcurrency] ?: ""
        }.first()
    }

    private object PreferencesKey {
        const val preferenceFile = "dhaiban"
        val onBoardingKey = booleanPreferencesKey(name = "on_boarding_completed")
        val countryKey = stringPreferencesKey(name = "country_key")
        val languageKey = stringPreferencesKey(name = "language_key")
        val currencyKey = stringPreferencesKey(name = "currency_key")
        val currencyIdKey = intPreferencesKey(name = "currency_id_key")
        val countryIdKey = intPreferencesKey(name = "country_id_key")
        val tokenKey = stringPreferencesKey(name = "token_key")
        val languageTempKey = stringPreferencesKey(name = "language_temp_key")
        val layoutDirectionKey = stringPreferencesKey(name = "layout_direction_key")
        val stringRes = booleanPreferencesKey(name = "string_res_state")
        val usernameKey = stringPreferencesKey(name = "username_key")
        val mobileNumberKey = stringPreferencesKey(name = "mobile_number_key")
        val emailKey = stringPreferencesKey(name = "email_key")
        val fmcTokenKey = stringPreferencesKey(name = "fcm_token")
        val userImageUrlKey = stringPreferencesKey(name = "user_image_key")
        val userID= stringPreferencesKey(name = "user_image_ID")
        val userEmail = stringPreferencesKey(name = "user_Email")
        val transcationCode = stringPreferencesKey(name = "transcation_code")
//        val momo = stringPreferencesKey(name = PaymentMethodsType.MOMO.name)
        val momo = stringPreferencesKey(name = "MOMO")
        val currentIdOfCart = intPreferencesKey(name = "current_id_of_cart")
        val addressID = intPreferencesKey(name = "address_id_of_cart")
        val promoCode = stringPreferencesKey(name = "promo_code_of_cart")
        val paidFromWallet = doublePreferencesKey(name = "paid_from_wallet")
        val promodiscount = doublePreferencesKey(name = "promo_discount")
        val nameOfcurrency = stringPreferencesKey(name = "nameOfcurrency")
        val isActive = booleanPreferencesKey(name = "isActive")
        val isFromCart = booleanPreferencesKey(name = "isFromCart")
    }

    private val Context.preferencesDataStore: DataStore<Preferences>
            by preferencesDataStore(preferenceFile)

    private val dataStore = context.preferencesDataStore
    override suspend fun saveOnBoardingState(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.onBoardingKey] = completed
        }
    }

    override suspend fun readOnBoardingState(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.onBoardingKey] ?: false
        }.first()
    }

    override suspend fun saveUserCountry(country: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.countryKey] = country
        }
    }

    override suspend fun saveIsActive(isActive: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.isActive] = isActive
        }
    }

    override suspend fun saveUserLanguage(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.languageKey] = language
        }
    }

    override suspend fun saveUserCurrency(currency: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.currencyKey] = currency
        }
    }

    override suspend fun getUserCountry(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.countryKey] ?: ""
        }.first()
    }

    override suspend fun getUserLanguageAsFlow(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.languageKey] ?: ""
        }.distinctUntilChanged()
    }

    override suspend fun getUserLanguage(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.languageKey] ?: ""
        }.first()
    }

    override suspend fun getUserCurrency(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.currencyKey] ?: ""
        }.first()
    }

    override suspend fun saveLayoutDirection(direction: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.layoutDirectionKey] = direction
        }
    }

    override suspend fun getLayoutDirection(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.layoutDirectionKey] ?: ""
        }.first()
    }

    override suspend fun saveToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.tokenKey] = token
        }
    }

    override suspend fun getIsFromCart(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.isFromCart] ?: false
        }.first()
    }

    override suspend fun saveIsFromCart(flag: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.isFromCart] = flag
        }
    }

    override suspend fun getUserToken(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.tokenKey] ?: ""
        }.first()
    }

    override suspend fun saveUserEmail(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userEmail] = token
        }    }

    override suspend fun getUserEmail():String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.userEmail] ?: ""
        }.first()
    }

    override suspend fun saveUserid(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userID] = token
        }
    }

    override suspend fun getUserid():String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.userID] ?: ""
        }.first()
    }

    override suspend fun clearToken() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKey.tokenKey)
        }
    }

    override suspend fun saveUserLanguageTemp(language: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.languageTempKey] = language
        }
    }

    override suspend fun getUserLanguageTemp(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.languageTempKey] ?: ""
        }.first()
    }

    override suspend fun saveStringResourceState(saved: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.stringRes] = saved
        }
    }

    override suspend fun readStringResourceState(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.stringRes] ?: false
        }.first()
    }

    override suspend fun saveUsername(username: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.usernameKey] = username
        }
    }

    override suspend fun getUsername(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.usernameKey] ?: ""
        }.first()
    }

    override suspend fun saveContactNumber(contactNumber: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.mobileNumberKey] = contactNumber
        }
    }

    override suspend fun getContactNumber(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.mobileNumberKey] ?: ""
        }.first()
    }

    override suspend fun saveEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.emailKey] = email
        }
    }

    override suspend fun getEmail(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.emailKey] ?: ""
        }.first()
    }

    override suspend fun saveCurrencyId(currencyId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.currencyIdKey] = currencyId
        }
    }

    override suspend fun getCurrencyId(): Int {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.currencyIdKey] ?: 0
        }.first()
    }

    override suspend fun saveCountryId(countryId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.countryIdKey] = countryId
        }
    }

    override suspend fun getCountryId(): Int {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.countryIdKey] ?: 0
        }.first()
    }

    override suspend fun saveFcmToken(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.fmcTokenKey] = token
        }
    }

    override suspend fun getFcmToken(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.fmcTokenKey] ?: ""
        }.first()
    }

    override suspend fun saveTransactionCode(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.transcationCode] = token
        }
    }

    override suspend fun getTransactionCode(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.transcationCode] ?: ""
        }.first()
    }

    override suspend fun setIsTherePaymentMomo(token: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.momo] = token
        }
    }

    override suspend fun getIsTherePaymentMomo(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.momo] ?: ""
        }.first()    }

    override suspend fun saveImageUrl(image: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.userImageUrlKey] = image
        }
    }

    override suspend fun getImageUrl(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.userImageUrlKey] ?: ""
        }.first()
    }

    override suspend fun getIsActive(): Boolean {


        val isActive=dataStore.data.map { preferences ->
            preferences[PreferencesKey.isActive] ?: false
        }.first()
        return isActive
    }

    override suspend fun saveCurrentIDOfCart(id: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.currentIdOfCart] = id
        }
    }

    override suspend fun saveAddressIDOfCart(addressID: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.addressID] = addressID
        }
    }

    override suspend fun savePromoCodeOfCart(promoCode: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.promoCode] = promoCode
        }
    }

    override suspend fun savePaidFromWalletOfCart(addressID: Double) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.paidFromWallet] = addressID
        }
    }

    override suspend fun savePromoOfDiscountCart(promo: Double) {
        dataStore.edit { preferences ->
            preferences[PreferencesKey.promodiscount] = promo
        }
    }

    override suspend fun getCurrentIDOfCart(): Int {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.currentIdOfCart] ?: 0
        }.first()
    }

    override suspend fun getAddressIdOfCart(): Int {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.addressID] ?: 0
        }.first()
    }

    override suspend fun getPromoCodeFromCart(): String {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.promoCode] ?: ""
        }.first()
    }

    override suspend fun getPaidFromWallet(): Double {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.paidFromWallet] ?: 0.0
        }.first()
    }

    override suspend fun getPromoOfCartDiscount(): Double {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKey.promodiscount] ?: 0.0
        }.first()
    }
}