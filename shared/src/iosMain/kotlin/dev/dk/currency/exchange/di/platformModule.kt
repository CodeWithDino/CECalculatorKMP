package dev.dk.currency.exchange.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dev.dk.currency.exchange.data.datastore.createMainDataStore
import dev.dk.currency.exchange.data.datastore.dataStoreFileName
import dev.dk.currency.exchange.data.repository.GetCurrencyRatesRepository
import dev.dk.currency.exchange.data.repository.GetCurrencyRatesRepositoryInterface
import io.ktor.client.engine.darwin.Darwin
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.*

actual fun platformModule() : Module = module {
    single { createDataStore() }
    single {
        val driver = NativeSqliteDriver(CurrencyConversionDatabase.Schema, "currencyconversion.db")
        CurrencyConversionDatabaseWrapper(CurrencyConversionDatabase(driver))
    }
    single<GetCurrencyRatesRepositoryInterface> { GetCurrencyRatesRepository(get()) }
    single { Darwin.create() }
}

fun createDataStore(): DataStore<Preferences> = createMainDataStore(
    producePath = {
        val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(documentDirectory).path + "/$dataStoreFileName"
    }
)
