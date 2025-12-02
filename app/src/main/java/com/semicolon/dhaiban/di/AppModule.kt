package com.semicolon.dhaiban.di

import org.koin.dsl.module

fun appModule() = module {
    includes(
        networkModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        helperModule,
        screenModelsModule
    )
}