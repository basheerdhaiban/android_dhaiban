package com.semicolon.domain.repository

import com.semicolon.domain.entity.Status
import kotlinx.coroutines.flow.Flow

interface NetworkConnectivity {
    fun observe(): Flow<Status>
    fun isNetworkAvailable(): Boolean

}

