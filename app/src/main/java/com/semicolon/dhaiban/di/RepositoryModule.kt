package com.semicolon.dhaiban.di

import com.google.android.gms.location.LocationServices
import com.semicolon.data.network.NetworkConnectivityObserver
import com.semicolon.data.repository.local.LocalConfigurationRepositoryImpl
import com.semicolon.data.repository.remote.repository.AddressRepositoryImp
import com.semicolon.data.repository.remote.repository.BrandsRepositoryImp
import com.semicolon.data.repository.remote.repository.CartRepositoryImp
import com.semicolon.data.repository.remote.repository.CategoriesRepositoryImp
import com.semicolon.data.repository.remote.repository.FaqRepositoryImp
import com.semicolon.data.repository.remote.repository.FavoritesRepositoryImp
import com.semicolon.data.repository.remote.repository.LocalizationRepositoryImpl
import com.semicolon.data.repository.remote.repository.LocationRepositoryImp
import com.semicolon.data.repository.remote.repository.MessageRemoteSource
import com.semicolon.data.repository.remote.repository.NotificationRepositoryImp
import com.semicolon.data.repository.remote.repository.NotificationsRemoteSource
import com.semicolon.data.repository.remote.repository.OrderRepositoryImp
import com.semicolon.data.repository.remote.repository.PreviousOrdersRemoteSource
import com.semicolon.data.repository.remote.repository.ProductRemoteSource
import com.semicolon.data.repository.remote.repository.ProductRepositoryImp
import com.semicolon.data.repository.remote.repository.RefundRepositoryImp
import com.semicolon.data.repository.remote.repository.SearchRepositoryImp
import com.semicolon.data.repository.remote.repository.SubCategoryRepositoryImp
import com.semicolon.data.repository.remote.repository.UserRepositoryImpl
import com.semicolon.data.repository.remote.repository.WalletRepositoryImp
import com.semicolon.data.repository.remote.repository.MessageRepositoryImp
import com.semicolon.data.repository.remote.repository.SocialRepositoryImp
import com.semicolon.domain.repository.AddressRepository
import com.semicolon.domain.repository.BrandsRepository
import com.semicolon.domain.repository.CartRepository
import com.semicolon.domain.repository.CategoriesRepository
import com.semicolon.domain.repository.FaqRepository
import com.semicolon.domain.repository.FavoritesRepository
import com.semicolon.domain.repository.LocalConfigurationRepository
import com.semicolon.domain.repository.LocalizationRepository
import com.semicolon.domain.repository.LocationRepository
import com.semicolon.domain.repository.NetworkConnectivity
import com.semicolon.domain.repository.NotificationRepository
import com.semicolon.domain.repository.OrderRepository
import com.semicolon.domain.repository.ProductRepository
import com.semicolon.domain.repository.RefundRepository
import com.semicolon.domain.repository.SearchRepository
import com.semicolon.domain.repository.SubCategoryRepository
import com.semicolon.domain.repository.UserRepository
import com.semicolon.domain.repository.WalletRepository
import com.semicolon.domain.repository.MessageRepository
import com.semicolon.domain.repository.SocialRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repositoryModule = module {
    single { ProductRemoteSource(get()) }
    single { PreviousOrdersRemoteSource(get()) }
    single { NotificationsRemoteSource(get()) }
    single { MessageRemoteSource(get()) }

    single<LocalConfigurationRepository> { LocalConfigurationRepositoryImpl(androidContext().applicationContext) }
    singleOf(::UserRepositoryImpl) { bind<UserRepository>() }
    singleOf(::LocalizationRepositoryImpl) { bind<LocalizationRepository>() }
    singleOf(::CategoriesRepositoryImp) { bind<CategoriesRepository>() }
    singleOf(::BrandsRepositoryImp) { bind<BrandsRepository>() }
    singleOf(::ProductRepositoryImp) { bind<ProductRepository>() }
    singleOf(::SubCategoryRepositoryImp) { bind<SubCategoryRepository>() }
    singleOf(::FavoritesRepositoryImp) { bind<FavoritesRepository>() }
    singleOf(::CartRepositoryImp) { bind<CartRepository>() }
    singleOf(::OrderRepositoryImp) { bind<OrderRepository>() }
    single<NetworkConnectivity> { NetworkConnectivityObserver(androidContext().applicationContext) }
    single { LocationServices.getFusedLocationProviderClient(androidContext()) }
    singleOf(::LocationRepositoryImp) { bind<LocationRepository>() }
    singleOf(::AddressRepositoryImp) { bind<AddressRepository>() }
    singleOf(::SearchRepositoryImp) { bind<SearchRepository>() }
    singleOf(::FaqRepositoryImp) { bind<FaqRepository>() }
    singleOf(::RefundRepositoryImp) { bind<RefundRepository>() }
    singleOf(::WalletRepositoryImp) { bind<WalletRepository>() }
    singleOf(::NotificationRepositoryImp) { bind<NotificationRepository>() }
    singleOf(::MessageRepositoryImp) { bind<MessageRepository>() }
    singleOf(::SocialRepositoryImp) { bind<SocialRepository>() }
}
