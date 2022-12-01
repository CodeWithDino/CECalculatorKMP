package dev.dk.currency.exchange

import dev.dk.currency.exchange.data.remote.ApiResult
import dev.dk.currency.exchange.data.repository.resolveCombineApiRequest
import kotlin.test.Test
import kotlin.test.assertTrue


class GetCurrencyRatesRepositoryTest {

    @Test
    fun `test success ResolveCombineResult`(){
        val rates = ApiResult.Success(latestTestRatesJson)
        val currencies = ApiResult.Success(currenciesTestJson)

        resolveCombineApiRequest(rates,currencies, onSuccess = { rate,currency ->
            assertTrue(rate.isNotEmpty())
            assertTrue(currency.isNotEmpty())
        }, onHttpError = {
            assertTrue(it.isEmpty())
        }, onGenericError = {
            assertTrue(it.isEmpty())
        }, onInternetError = {
            assertTrue(it.isEmpty())
        })
    }

    @Test
    fun `test internet error ResolveCombineResult`(){
        val rates = ApiResult.NoInternet
        val currencies = ApiResult.Success("")

        resolveCombineApiRequest(rates,currencies, onSuccess = { rate,currency ->
            //
        }, onHttpError = {
            assertTrue(it.isEmpty())
        }, onGenericError = {
            assertTrue(it.isEmpty())
        }, onInternetError = {
            assertTrue(it.isNotEmpty())
        })
    }

    @Test
    fun `test generic error ResolveCombineResult`(){

        val rates = ApiResult.GenericError(Exception("Generic"))
        val currencies = ApiResult.GenericError(Exception("Generic"))

        resolveCombineApiRequest(rates,currencies, onSuccess = { rate,currency ->
            //
        }, onHttpError = {
            assertTrue(it.isNotEmpty())
        }, onGenericError = {
            assertTrue(it.isNotEmpty())
        }, onInternetError = {
            assertTrue(it.isEmpty())
        })
    }

}