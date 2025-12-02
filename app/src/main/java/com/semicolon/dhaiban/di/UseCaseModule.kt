package com.semicolon.dhaiban.di

import com.semicolon.domain.usecase.UserAuthenticationUseCase
import com.semicolon.domain.usecase.LocalConfigurationUseCase
import com.semicolon.domain.usecase.LocationUseCase
import com.semicolon.domain.usecase.ManageAddressUseCase
import com.semicolon.domain.usecase.ManageBrandUseCase
import com.semicolon.domain.usecase.ManageCartUseCase
import com.semicolon.domain.usecase.ManageCategoryUseCase
import com.semicolon.domain.usecase.ManageConnectivityUseCase
import com.semicolon.domain.usecase.ManageCurrencyUseCase
import com.semicolon.domain.usecase.ManageFaqUseCase
import com.semicolon.domain.usecase.ManageFavoritesUseCase
import com.semicolon.domain.usecase.ManageNotificationUseCase
import com.semicolon.domain.usecase.ManageOrderUseCase
import com.semicolon.domain.usecase.ManageProductUseCase
import com.semicolon.domain.usecase.ManageRefundUseCase
import com.semicolon.domain.usecase.ManageSearchUseCase
import com.semicolon.domain.usecase.ManageSubCategoryUseCase
import com.semicolon.domain.usecase.ManageWalletUseCase
import com.semicolon.domain.usecase.UserValidationUseCase
import com.semicolon.domain.usecase.ChatUseCases
import com.semicolon.domain.usecase.ManageSocialUseCases
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val useCaseModule = module {
    singleOf(::UserValidationUseCase)
    singleOf(::LocalConfigurationUseCase)
    singleOf(::UserAuthenticationUseCase)
    singleOf(::ManageCategoryUseCase)
    singleOf(::ManageBrandUseCase)
    singleOf(::ManageProductUseCase)
    singleOf(::ManageConnectivityUseCase)
    singleOf(::ManageSubCategoryUseCase)
    singleOf(::ManageFavoritesUseCase)
    singleOf(::ManageCartUseCase)
    singleOf(::ManageCurrencyUseCase)
    singleOf(::ManageOrderUseCase)
    singleOf(::LocationUseCase)
    singleOf(::ManageAddressUseCase)
    singleOf(::ManageSearchUseCase)
    singleOf(::ManageFaqUseCase)
    singleOf(::ManageRefundUseCase)
    singleOf(::ManageWalletUseCase)
    singleOf(::ManageNotificationUseCase)
    singleOf(::ChatUseCases)
    singleOf(::ManageSocialUseCases)


}