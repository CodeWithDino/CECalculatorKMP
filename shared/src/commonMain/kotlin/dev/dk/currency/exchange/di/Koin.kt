package dev.dk.currency.exchange.di

import dev.dk.currency.exchange.data.datastore.AppDataStore
import dev.dk.currency.exchange.data.remote.CurrencyExchangeApi
import dev.dk.currency.exchange.data.remote.ApiClient
import kotlinx.coroutines.MainScope
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

fun initKoin(enableNetworkLogs: Boolean = false, appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(sharedModule, platformModule())
    }

val sharedModule = module {
    single { MainScope() }
    single { createJson() }
    single{ ApiClient(get(),get()) }
    single { CurrencyExchangeApi(get()) }
    single { AppDataStore(get()) }
    single { RepositoryHelper() }
}

fun createJson() = Json { isLenient = true; ignoreUnknownKeys = true }

// called by iOS etc
fun initKoin() = initKoin(enableNetworkLogs = false) {

}
