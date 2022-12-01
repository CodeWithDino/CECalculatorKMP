package dev.dk.currency.exchange.data.remote

import io.ktor.client.statement.*

class CurrencyExchangeApi(private val apiClient: ApiClient) {

    suspend fun getLatestCurrencyRates(): String {
        return apiClient.GET<String>(
            LATEST_JSON,
            listOf(Pair(APP_ID_QUERY_KEY, OPEN_RATES_API_KEY))
        ).bodyAsText()
    }

    suspend fun getAllCurrencies(): String {
        return apiClient.GET<String>(
            CURRENCY_JSON,
            listOf(Pair(APP_ID_QUERY_KEY, OPEN_RATES_API_KEY))
        ).bodyAsText()
    }


}