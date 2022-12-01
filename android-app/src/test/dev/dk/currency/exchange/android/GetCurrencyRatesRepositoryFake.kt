package dev.dk.currency.exchange.android

import kotlinx.coroutines.flow.*
import dev.dk.currency.exchange.data.CurrencyRates
import dev.dk.currency.exchange.data.model.OnResultObtained
import dev.dk.currency.exchange.data.remote.ApiResult
import dev.dk.currency.exchange.data.remote.makeRequestToApi
import dev.dk.currency.exchange.data.repository.GetCurrencyRatesRepositoryInterface

/**
 * Get Currency Rates repository for testing Sample Json
 * see [latestTestRatesJson] and [currenciesTestJson]
 * */
class GetCurrencyRatesRepositoryFake :GetCurrencyRatesRepositoryInterface {

    override suspend fun getLatestRates(): ApiResult<String> {
       return makeRequestToApi {
           latestTestRatesJson
       }
    }

    override suspend fun getCurrencyNames(): ApiResult<String> {
        return makeRequestToApi {
            currenciesTestJson
        }
    }

    override fun loadData() {
    }

    private val _currencyRates = MutableStateFlow(
        OnResultObtained<CurrencyRates>(
            result = null,
            isLoaded = false,
            error = ""
        )
    )

    override fun currencyRates(): StateFlow<OnResultObtained<CurrencyRates>> = _currencyRates.asStateFlow()
}



