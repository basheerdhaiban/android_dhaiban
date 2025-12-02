package com.semicolon.dhaiban

import android.app.Application
import android.os.Handler
import com.semicolon.dhaiban.di.appModule
import com.semicolon.dhaiban.presentation.payment.PaymentScreenModel
import com.semicolon.dhaiban.utils.createOrdersNotificationChannel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class DhaibanApp : Application() {
    companion object {
        lateinit var  ScreenModel: PaymentScreenModel
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(applicationContext).modules(
                appModule(),
            )
        }
        createOrdersNotificationChannel(this)
    }
}