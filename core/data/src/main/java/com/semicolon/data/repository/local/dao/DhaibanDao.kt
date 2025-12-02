package com.semicolon.data.repository.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.semicolon.data.repository.local.model.StringsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DhaibanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveStringFile(stringFile: StringsEntity)

    @Query("SELECT * FROM STRING_TABLE")
    fun getStringEntity(): Flow<StringsEntity>
}