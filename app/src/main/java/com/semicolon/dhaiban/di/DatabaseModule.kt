package com.semicolon.dhaiban.di

import android.content.Context
import androidx.room.Room
import com.semicolon.data.repository.local.DhaibanDatabase
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(get<Context>(), DhaibanDatabase::class.java, "dhaiban database")
            .build()
    }

    single {
        get<DhaibanDatabase>().dhaibanDao()
    }
}