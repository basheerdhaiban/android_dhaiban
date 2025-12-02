package com.semicolon.dhaiban.di

import com.semicolon.dhaiban.presentation.utils.ValidationHandler
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val helperModule = module {
    factoryOf(::ValidationHandler)
}