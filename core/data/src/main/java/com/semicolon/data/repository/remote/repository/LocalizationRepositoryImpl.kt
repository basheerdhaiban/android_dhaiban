package com.semicolon.data.repository.remote.repository

import com.semicolon.data.repository.local.dao.DhaibanDao
import com.semicolon.data.repository.remote.mapper.toEntity
import com.semicolon.data.repository.remote.mapper.toStringsEntity
import com.semicolon.data.repository.remote.model.localization.StringsDto
import com.semicolon.domain.entity.Strings
import com.semicolon.domain.repository.LocalizationRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalizationRepositoryImpl(
    private val dao: DhaibanDao,
    client: HttpClient
) : BaseRepository(client), LocalizationRepository {

    override suspend fun getLocalStringsFromDb(): Flow<Strings> {
        return dao.getStringEntity().map { it.toEntity() }
    }

    override suspend fun saveLocalizationStringsToDb(url: String, lang: String) {
        val langCode = lang.lowercase()
        val result = tryToExecute<StringsDto> {
            async {
                client.get(
                    urlString = url.ifEmpty { "https://admin.dhaibantrading.com/uploads/languages/mobile/$langCode.json" }
                )
            }.await()
        }
        dao.saveStringFile(result.toStringsEntity().copy(id = 1))
    }
}