package com.semicolon.domain.repository

import com.semicolon.domain.entity.AddressModel
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.entity.City
import com.semicolon.domain.entity.Province
import com.semicolon.domain.entity.Region

interface AddressRepository {
    suspend fun getAvailableCountries(): List<AppConfig.Country>
    suspend fun getAvailableGovernments(countryId: Int): List<Province>
    suspend fun getAvailableCities(governmentId: Int): List<City>
    suspend fun getAvailableRegions(cityId: Int): List<Region>
    suspend fun getUserAddresses(): List<AddressModel>
    suspend fun addAddress(address: AddressModel): Boolean
    suspend fun editAddress(address: AddressModel): List<AddressModel>
    suspend fun deleteAddress(addressId: Int): List<AddressModel>
}