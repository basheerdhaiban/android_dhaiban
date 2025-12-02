package com.semicolon.data.repository.remote.mapper

import com.semicolon.data.repository.remote.model.address.AddressDto
import com.semicolon.data.repository.remote.model.address.CityDto
import com.semicolon.data.repository.remote.model.address.CountryDto
import com.semicolon.data.repository.remote.model.address.ProvinceDto
import com.semicolon.data.repository.remote.model.address.RegionDto
import com.semicolon.domain.entity.AddressModel
import com.semicolon.domain.entity.AppConfig
import com.semicolon.domain.entity.City
import com.semicolon.domain.entity.Province
import com.semicolon.domain.entity.Region

fun CountryDto.toCountry() = AppConfig.Country(
    id = this.id ?: 0,
    code = this.code ?: "",
    logo = this.logo ?: "",
    title = this.title ?: "",
    active = this.active ?: 0
)

fun List<CountryDto>.toEntity() = this.map { it.toCountry() }

fun ProvinceDto.toProvince() = Province(
    code ?: "", id ?: 0, title ?: ""
)

fun List<ProvinceDto>.toProvinceList() = this.map { it.toProvince() }

fun CityDto.toCity() = City(
    code ?: "", id ?: 0, title ?: ""
)

fun List<CityDto>.toCityList() = this.map { it.toCity() }

fun RegionDto.toRegion() = Region(
    code ?: "", id ?: 0, title ?: ""
)

fun List<RegionDto>.toRegionList() = this.map { it.toRegion() }

fun AddressDto.toAddressModel() =
    AddressModel(
        address = this.address ?: "",
        cityId = this.cityId ?: 0,
        cityName = this.cityName ?: "",
        defaultAddress = this.defaultAddress ?: 0,
        id = this.id ?: 0,
        lat = this.lat ?: 0.0,
        lon = this.lon ?: 0.0,
        name = this.name ?: "",
        phone = this.phone ?: "",
        postalCode = this.postalCode ?: "",
        provinceId = this.provinceId ?: 0,
        provinceName = this.provinceName ?: "",
        regionId = this.regionId ?: 0,
        regionName = this.regionName ?: "",
        workHome = this.workHome ?: ""
    )
fun List<AddressDto>.toAddressModelList() = this.map { it.toAddressModel() }