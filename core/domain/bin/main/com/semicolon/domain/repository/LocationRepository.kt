package com.semicolon.domain.repository

import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    fun requestLocationUpdates(): Flow<Pair<Double,Double>?>
}