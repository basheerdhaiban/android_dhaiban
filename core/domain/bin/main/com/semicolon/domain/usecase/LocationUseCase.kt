package com.semicolon.domain.usecase

import com.semicolon.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow

class LocationUseCase(private val locationRepository: LocationRepository) {
    operator fun invoke(): Flow<Pair<Double,Double>?> = locationRepository.requestLocationUpdates()
}