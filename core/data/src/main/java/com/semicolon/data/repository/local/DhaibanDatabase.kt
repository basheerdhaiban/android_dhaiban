package com.semicolon.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.semicolon.data.repository.local.dao.DhaibanDao
import com.semicolon.data.repository.local.model.StringsEntity

@Database(entities = [StringsEntity::class], version = 1)
abstract class DhaibanDatabase : RoomDatabase() {
    abstract fun dhaibanDao(): DhaibanDao

}