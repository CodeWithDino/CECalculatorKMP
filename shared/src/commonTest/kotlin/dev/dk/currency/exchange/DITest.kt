package dev.dk.currency.exchange

import dev.dk.currency.exchange.data.remote.CurrencyExchangeApi
import dev.dk.currency.exchange.di.platformModule
import dev.dk.currency.exchange.di.sharedModule
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.Test
import kotlin.test.assertNotNull

class DITest : KoinTest {
    @Test
    fun `should inject my components`() {
        startKoin {
            modules(sharedModule, platformModule())
        }
        // directly request an instance
        val api = get<CurrencyExchangeApi>()

        assertNotNull(api)
        stopKoin()
    }
}