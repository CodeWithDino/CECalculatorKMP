package dev.dk.currency.exchange.android

import android.app.Application
import dev.dk.currency.exchange.android.di.presentationModule
import dev.dk.currency.exchange.di.initKoin
import dev.dk.currency.exchange.di.sharedModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class CECalculatorApp : Application() {
    override fun onCreate() {
        super.onCreate()

        //Initialise Koin for Dependency Injection
        initKoin {
            allowOverride(true)
            androidContext(this@CECalculatorApp)
            androidLogger(Level.ERROR)
            modules(appModules, sharedModule)
        }
    }
}

val appModules = presentationModule
