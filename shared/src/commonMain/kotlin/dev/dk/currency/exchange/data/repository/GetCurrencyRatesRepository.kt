package dev.dk.currency.exchange.data.repository

import co.touchlab.kermit.Logger
import com.rickclephas.kmp.nativecoroutines.NativeCoroutineScope
import dev.dk.currency.exchange.data.CurrencyRates
import dev.dk.currency.exchange.data.CurrencyRecord
import dev.dk.currency.exchange.data.database.CurrencyConversionDatabaseWrapper
import dev.dk.currency.exchange.data.datastore.AppDataStore
import dev.dk.currency.exchange.data.model.OnResultObtained
import dev.dk.currency.exchange.data.remote.ApiResult
import dev.dk.currency.exchange.data.remote.CurrencyExchangeApi
import dev.dk.currency.exchange.data.remote.makeRequestToApi
import dev.dk.currency.exchange.utils.convertToCurrencyRates
import dev.dk.currency.exchange.db.CECalculatorDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface GetCurrencyRatesRepositoryInterface {
    suspend fun getLatestRates(): ApiResult<String>
    suspend fun getCurrencyNames(): ApiResult<String>
    fun loadData()
    fun currencyRates(): StateFlow<OnResultObtained<CurrencyRates>>
}

class GetCurrencyRatesRepository(private val dataStore: AppDataStore) :
    GetCurrencyRatesRepositoryInterface, KoinComponent {

    @NativeCoroutineScope
    internal val coroutineScope = CoroutineScope(Dispatchers.Default)

    private val currencyConversionDatabaseWrapper: CurrencyConversionDatabaseWrapper by inject()
    private val currencyConversionDatabaseQueries =
        currencyConversionDatabaseWrapper.instance.cECalculatorDatabaseQueries

    private val api: CurrencyExchangeApi by inject()

    private val _currencyRates = MutableStateFlow(
        OnResultObtained<CurrencyRates>(
            result = null,
            isLoaded = false,
            error = ""
        )
    )

    override fun currencyRates(): StateFlow<OnResultObtained<CurrencyRates>> =
        _currencyRates.asStateFlow()

    override fun loadData() {

        coroutineScope.launch {

            val lastTimeStamp = dataStore.lasSuccessTimeStamp.first()
            val lastSuccessTimeStamp = dataStore.lasSuccessTimeStamp.first()

            val difference = Clock.System.now().toEpochMilliseconds() - lastSuccessTimeStamp

            Logger.e { "lasSuccessTimeStamp $lastSuccessTimeStamp --- difference $difference --- lastTimestamp $lastTimeStamp" }

            if (lastTimeStamp != 0L && difference < lastTimeStamp) {
//                val currencyConversionList: List<CECalculator> =
//                    currencyConversionDatabaseWrapper.instance.currencyConversionDatabaseQueries.transactionWithResult {
//                        currencyConversionDatabaseQueries.selectAll().executeAsList()
//                    }
//                val rates = mutableListOf<CurrencyRecord>()
//                currencyConversionList.forEach {
//                    rates.add(CurrencyRecord(it.code, it.usdRate, it.name, it.conversion))
//                }
//                val currencyRates = CurrencyRates(currencyConversionList.first().base, rates)
//                _currencyRates.value = OnResultObtained(
//                    currencyRates,
//                    true,
//                    null
//                )
            } else {
                val getRates = async {
                    return@async getLatestRates()
                }
                val getCurrencies = async {
                    return@async getCurrencyNames()
                }

                val awaitAll = awaitAll(getRates, getCurrencies)

                val first = awaitAll.first()
                val second = awaitAll[1]
                resolveCombineApiRequest(first, second,
                    onSuccess = { rates, currencies ->
                        val convertToCurrencyRates = convertToCurrencyRates(rates, currencies)
                        _currencyRates.value = OnResultObtained(
                            convertToCurrencyRates!!,
                            true,
                            null
                        )
                        currencyConversionDatabaseQueries.transaction {
                            currencyConversionDatabaseQueries.deleteAll()
                            val base = convertToCurrencyRates.base
                            convertToCurrencyRates.rates.forEach { currencyRecord ->
                                currencyRecord.code?.let {
                                    currencyConversionDatabaseQueries.insertItem(
                                        it,
                                        base,
                                        currencyRecord.name,
                                        currencyRecord.usdRate,
                                        currencyRecord.conversion
                                    )
                                }
                            }
                        }
                        dataStore.updateLastSuccessTimeStamp(
                            Clock.System.now().toEpochMilliseconds()
                        )
                    }, onInternetError = { _ ->
                        _currencyRates.value = OnResultObtained(
                            null,
                            true,
                            "error"
                        )
                    }, onGenericError = { error ->
                        _currencyRates.value = OnResultObtained(
                            null,
                            true,
                            error
                        )
                    }, onHttpError = { error ->
                        _currencyRates.value = OnResultObtained(
                            null,
                            true,
                            error
                        )
                    })
            }
        }
    }

    override suspend fun getLatestRates(): ApiResult<String> {
        return makeRequestToApi {
            api.getLatestCurrencyRates()
        }
    }

    override suspend fun getCurrencyNames(): ApiResult<String> {
        return makeRequestToApi {
            api.getAllCurrencies()
        }
    }

}

fun <First : Any, Second : Any> resolveCombineApiRequest(
    first: ApiResult<First>,
    second: ApiResult<Second>,
    onSuccess: (First, Second) -> Unit,
    onInternetError: (String) -> Unit,
    onHttpError: (String) -> Unit,
    onGenericError: (String) -> Unit
) {
    try {
        when {
            first is ApiResult.Success && second is ApiResult.Success           -> {
                onSuccess(first.response, second.response)
            }
            first is ApiResult.NoInternet || second is ApiResult.NoInternet     -> {
                onInternetError("No Internet Connection")
            }
            first is ApiResult.GenericError || second is ApiResult.GenericError -> {
                if (first is ApiResult.GenericError) {
                    onGenericError(first.error.message ?: "")
                }

                if (second is ApiResult.GenericError) {
                    onGenericError(second.error.message ?: "")
                }
            }
            first is ApiResult.HttpError || second is ApiResult.HttpError       -> {
                if (first is ApiResult.HttpError) {
                    onHttpError(first.error.message)
                }
                if (second is ApiResult.HttpError) {
                    onHttpError(second.error.message)
                }
            }
        }
    } catch (e: Exception) {
        onGenericError(e.message ?: "")
    }
}

