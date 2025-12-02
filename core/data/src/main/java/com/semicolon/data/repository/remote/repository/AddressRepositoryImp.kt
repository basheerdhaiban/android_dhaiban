package com.semicolon.data.repository.remote.repository

import com.semicolon.data.repository.remote.mapper.toAddressModelList
import com.semicolon.data.repository.remote.mapper.toCityList
import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.mapper.toProvinceList
import com.semicolon.data.repository.remote.mapper.toRegionList
import com.semicolon.data.repository.remote.model.BaseResponse
import com.semicolon.data.repository.remote.model.address.AddressDto
import com.semicolon.data.repository.remote.model.address.CityDto
import com.semicolon.data.repository.remote.model.address.CountriesData
import com.semicolon.data.repository.remote.model.address.ProvinceDto
import com.semicolon.data.repository.remote.model.address.RegionDto
import com.semicolon.domain.entity.AddressModel
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.entity.City
import com.semicolon.domain.entity.Province
import com.semicolon.domain.entity.Region
import com.semicolon.domain.repository.AddressRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.http.parameters

class AddressRepositoryImp(client: HttpClient) : AddressRepository, BaseRepository(client) {

    companion object {
        const val COUNTRIES = "countries"
        const val GET_PROVINCES = "getProvincies"
        const val COUNTRY_ID = "country_id"
        const val CITIES = "getCitiesByProvinceId"
        const val REGIONS = "getRegionsByCityId"
        const val ADDRESSES = "addresses"
        const val ADD_ADDRESS = "addAdress"
        const val EDIT_ADDRESS = "editAdress"
        const val DELETE_ADDRESS = "deleteAdress"
    }

    override suspend fun getAvailableCountries(): List<AppConfig.Country> {
        val result = tryToExecute<BaseResponse<CountriesData>> {
            client.get(COUNTRIES)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.countries?.toEntity() ?: emptyList()
    }

    override suspend fun getAvailableGovernments(countryId: Int): List<Province> {
        val result = tryToExecute<BaseResponse<List<ProvinceDto>>> {
            client.get {
                url(GET_PROVINCES)
                parameter(COUNTRY_ID, countryId)
            }
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toProvinceList()
    }

    override suspend fun getAvailableCities(governmentId: Int): List<City> {
        val result = tryToExecute<BaseResponse<List<CityDto>>> {
            client.get("$CITIES/$governmentId")
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toCityList()
    }

    override suspend fun getAvailableRegions(cityId: Int): List<Region> {
        val result = tryToExecute<BaseResponse<List<RegionDto>>> {
            client.get("$REGIONS/$cityId")
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toRegionList()
    }

    override suspend fun getUserAddresses(): List<AddressModel> {
        val result = tryToExecute<BaseResponse<List<AddressDto>>> {
            client.get(ADDRESSES)
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toAddressModelList()
    }

    override suspend fun addAddress(address: AddressModel): Boolean {
        val result = tryToExecute<BaseResponse<List<AddressDto>>> {
            client.submitForm(
                url = ADD_ADDRESS,
                formParameters = parameters {
                    append("province_id", address.provinceId.toString())
                    append("city_id", address.cityId.toString())
                    append("phone", address.phone)
                    append("address", address.address)
                    append("name", address.name)
                    append("postal_code", address.postalCode)
                    append("work_home", address.workHome)
                    append("lat", address.lat.toString())
                    append("lon", address.lon.toString())
                    append("default_adress", address.defaultAddress.toString())
                    append("region_id", address.regionId.toString())
                }
            )
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return true
    }

    override suspend fun editAddress(address: AddressModel): List<AddressModel> {
        val result = tryToExecute<BaseResponse<List<AddressDto>>> {
            client.submitForm(
                url = EDIT_ADDRESS,
                formParameters = parameters {
                    append("id", address.id.toString())
                    append("province_id", address.provinceId.toString())
                    append("city_id", address.cityId.toString())
                    append("phone", address.phone)
                    append("address", address.address)
                    append("name", address.name)
                    append("postal_code", address.postalCode)
                    append("work_home", address.workHome)
                    append("lat", address.lat.toString())
                    append("lon", address.lon.toString())
                    append("default_adress", address.defaultAddress.toString())
                    append("region_id", address.regionId.toString())
                }
            )
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toAddressModelList()
    }

    override suspend fun deleteAddress(addressId: Int): List<AddressModel> {
        val result = tryToExecute<BaseResponse<List<AddressDto>>> {
            client.get("$DELETE_ADDRESS/$addressId")
        }
        if (result.data == null) {
            throw Exception(result.message)
        }
        return result.data.toAddressModelList()
    }
}
