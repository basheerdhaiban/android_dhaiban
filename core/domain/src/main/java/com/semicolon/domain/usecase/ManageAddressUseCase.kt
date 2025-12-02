package com.semicolon.domain.usecase

import com.semicolon.domain.entity.AddressModel
import com.semicolon.domain.repository.AddressRepository

class ManageAddressUseCase(private val addressRepository: AddressRepository) {
    suspend fun getAvailableCountries() = addressRepository.getAvailableCountries()

    suspend fun getGovernmentsByCountryId(countryId: Int) =
        addressRepository.getAvailableGovernments(countryId)

    suspend fun getCitiesByGovernment(governmentId: Int) =
        addressRepository.getAvailableCities(governmentId)

    suspend fun getRegionsByCity(cityId: Int) =
        addressRepository.getAvailableRegions(cityId)

    suspend fun addAddress(address: AddressModel) =
        addressRepository.addAddress(address)

    suspend fun getAddresses() = addressRepository.getUserAddresses()

    suspend fun editAddress(address: AddressModel) =
        addressRepository.editAddress(address)

    suspend fun deleteAddress(addressId: Int) = addressRepository.deleteAddress(addressId)
}