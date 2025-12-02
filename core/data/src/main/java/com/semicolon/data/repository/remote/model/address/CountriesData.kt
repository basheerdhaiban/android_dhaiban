package com.semicolon.data.repository.remote.model.address

import kotlinx.serialization.Serializable

@Serializable
data class CountriesData(
    val countries: List<CountryDto>?
)