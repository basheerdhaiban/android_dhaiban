package com.semicolon.domain.repository

import com.semicolon.domain.entity.Strings
import kotlinx.coroutines.flow.Flow

interface LocalizationRepository {
    suspend fun getLocalStringsFromDb(): Flow<Strings>

    suspend fun saveLocalizationStringsToDb(url: String = "", lang: String)
}