package dev.dk.currency.exchange.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.squareup.sqldelight.android.AndroidSqliteDriver
import dev.dk.currency.exchange.data.database.CurrencyConversionDatabaseWrapper
import dev.dk.currency.exchange.data.datastore.createMainDataStore
import dev.dk.currency.exchange.data.datastore.dataStoreFileName
import dev.dk.currency.exchange.data.repository.GetCurrencyRatesRepository
import dev.dk.currency.exchange.data.repository.GetCurrencyRatesRepositoryInterface
import dev.dk.currency.exchange.db.CECalculatorDatabase
import io.ktor.client.engine.android.Android

import org.koin.core.module.Module
import org.koin.dsl.module

actual fun platformModule() : Module = module {
    single { createDataStore(get()) }
    single {
        val driver =
            AndroidSqliteDriver(CECalculatorDatabase.Schema, get(), "currencyconversion.db")
        CurrencyConversionDatabaseWrapper(CECalculatorDatabase(driver))
    }
    single<GetCurrencyRatesRepositoryInterface> { GetCurrencyRatesRepository(get()) }
    single { Android.create() }
}

fun createDataStore(context: Context): DataStore<Preferences> = createMainDataStore(
    producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
)
