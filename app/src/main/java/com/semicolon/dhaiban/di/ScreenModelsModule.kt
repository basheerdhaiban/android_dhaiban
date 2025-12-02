package com.semicolon.dhaiban.di

import com.semicolon.dhaiban.presentation.addaddress.AddAddressScreenModel
import com.semicolon.dhaiban.presentation.address.AddressScreenModel
import com.semicolon.dhaiban.presentation.app.AppScreenModel
import com.semicolon.dhaiban.presentation.authuntication.forgetPassword.ForgetPasswordScreenModel
import com.semicolon.dhaiban.presentation.authuntication.login.LoginScreenModel
import com.semicolon.dhaiban.presentation.authuntication.otp.OtpScreenModel
import com.semicolon.dhaiban.presentation.authuntication.resetPassword.ResetPasswordScreenModel
import com.semicolon.dhaiban.presentation.authuntication.signUp.SignUpScreenModel
import com.semicolon.dhaiban.presentation.brand.BrandScreenModel
import com.semicolon.dhaiban.presentation.brandproducts.BrandProductsScreenModel
import com.semicolon.dhaiban.presentation.cart.CartScreenModel
import com.semicolon.dhaiban.presentation.category.CategoryScreenModel
import com.semicolon.dhaiban.presentation.configuration.ConfigurationScreenModel
import com.semicolon.dhaiban.presentation.configurationSelection.ConfigurationSelectionScreenModel
import com.semicolon.dhaiban.presentation.contactus.ContactUsScreenModel
import com.semicolon.dhaiban.presentation.faq.FaqScreenModel
import com.semicolon.dhaiban.presentation.faqdetails.FaqDetailsScreenModel
import com.semicolon.dhaiban.presentation.favorites.FavoritesScreenModel
import com.semicolon.dhaiban.presentation.flashsale.FlashSaleScreenModel
import com.semicolon.dhaiban.presentation.home.HomeScreenModel
import com.semicolon.dhaiban.presentation.main.MainActivityViewModel
import com.semicolon.dhaiban.presentation.map.MapScreenModel
import com.semicolon.dhaiban.presentation.myprofile.MyProfileScreenModel
import com.semicolon.dhaiban.presentation.notification.NotificationScreenModel
import com.semicolon.dhaiban.presentation.onBoarding.OnBoardingScreenModel
import com.semicolon.dhaiban.presentation.orders.OrdersScreenModel
import com.semicolon.dhaiban.presentation.payment.PaymentScreenModel
import com.semicolon.dhaiban.presentation.product.ProductScreenModel
import com.semicolon.dhaiban.presentation.profile.ProfileScreenModel
import com.semicolon.dhaiban.presentation.refund.RefundScreenModel
import com.semicolon.dhaiban.presentation.search.SearchScreenModel
import com.semicolon.dhaiban.presentation.subCategory.SubCategoryScreenModel
import com.semicolon.dhaiban.presentation.trackorder.TrackOrderScreenModel
import com.semicolon.dhaiban.presentation.trackrefund.TrackRefundScreenModel
import com.semicolon.dhaiban.presentation.wallet.WalletScreenModel
import com.semicolon.dhaiban.presentation.welcome.WelcomeScreenModel
import com.semicolon.dhaiban.presentation.chat.ChatScreenModel
import com.semicolon.dhaiban.presentation.chat.NotificationViewModel
import com.semicolon.dhaiban.presentation.list_of_chat.ListOfChatScreenModel
import com.semicolon.dhaiban.presentation.paymentstatus.PaymentScreenStatusModel
import com.semicolon.dhaiban.presentation.customer_service.CustomerServiceScreenModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val screenModelsModule = module {
    factoryOf(::OnBoardingScreenModel)
    factoryOf(::WelcomeScreenModel)
    single { AppScreenModel(get(), get(), get(), get(), get()) }
    factoryOf(::ListOfChatScreenModel)
    factoryOf(::LoginScreenModel)
    factoryOf(::SignUpScreenModel)
    factoryOf(::OtpScreenModel)
    factoryOf(::ForgetPasswordScreenModel)
    factoryOf(::ResetPasswordScreenModel)
    factoryOf(::ConfigurationScreenModel)
    factoryOf(::ConfigurationSelectionScreenModel)
    factoryOf(::HomeScreenModel)
    factoryOf(::CartScreenModel)
    factoryOf(::CategoryScreenModel)
    factoryOf(::ProfileScreenModel)
    factoryOf(::SubCategoryScreenModel)
    factoryOf(::ProductScreenModel)
    factoryOf(::FavoritesScreenModel)
    factoryOf(::BrandScreenModel)
    factoryOf(::FlashSaleScreenModel)
    factoryOf(::BrandProductsScreenModel)
    factoryOf(::AddressScreenModel)
    factoryOf(::AddAddressScreenModel)
    factoryOf(::MapScreenModel)
    factoryOf(::MyProfileScreenModel)
    factoryOf(::PaymentScreenModel)
    factoryOf(::SearchScreenModel)
    factoryOf(::OrdersScreenModel)
    factoryOf(::TrackOrderScreenModel)
    factoryOf(::ContactUsScreenModel)
    factoryOf(::FaqScreenModel)
    factoryOf(::FaqDetailsScreenModel)
    factoryOf(::RefundScreenModel)
    factoryOf(::TrackRefundScreenModel)
    factoryOf(::WalletScreenModel)
    factoryOf(::NotificationScreenModel)
    factoryOf(::ChatScreenModel)
    factoryOf(::ListOfChatScreenModel)
    factoryOf(::ListOfChatScreenModel)
    factoryOf(::PaymentScreenStatusModel)
    viewModel  { NotificationViewModel( ) }


    factoryOf(::CustomerServiceScreenModel)

    single { MainActivityViewModel(get(), get()) }
}