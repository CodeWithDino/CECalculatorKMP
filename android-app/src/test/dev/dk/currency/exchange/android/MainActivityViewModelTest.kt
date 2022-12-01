package dev.dk.currency.exchange.android

import dev.dk.currency.exchange.android.ui.home.MainHomeViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.test.KoinTest
import dev.dk.currency.exchange.data.remote.ApiResult
import dev.dk.currency.exchange.data.repository.GetCurrencyRatesRepositoryInterface
import dev.dk.currency.exchange.utils.convertToCurrencyRates
import kotlin.math.roundToInt
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

@OptIn(ExperimentalCoroutinesApi::class)
class MainActivityViewModelTest : KoinTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val scope = TestScope()

    private lateinit var mainActivityViewModel: MainHomeViewModel
    private lateinit var getRatesRepository: GetCurrencyRatesRepositoryInterface

    @Before
    fun setup() {

        getRatesRepository = GetCurrencyRatesRepositoryFake()
        mainActivityViewModel = MainHomeViewModel()

        runBlocking {
            val combinedResult = mutableListOf<ApiResult<String>>()

            getRatesRepository.getLatestRates().apply {
                combinedResult.add(this)
            }

            getRatesRepository.getCurrencyNames().apply {
                combinedResult.add(this)
            }

            assertTrue(combinedResult.isNotEmpty())
            val first = combinedResult.first()
            val second = combinedResult[1]
            assertTrue(first is ApiResult.Success)
            assertTrue(second is ApiResult.Success)

            if (first is ApiResult.Success && second is ApiResult.Success) {
                val currencyRate = convertToCurrencyRates(first.response, second.response)
                kotlin.test.assertTrue(currencyRate?.rates!!.isNotEmpty())
                mainActivityViewModel.parseRateResults(currencyRate)
            }
        }
    }

    @Test
    fun getCurrencyRatesTest() = runBlocking {

        val combinedResult = mutableListOf<ApiResult<String>>()
        getRatesRepository.getLatestRates().apply {
            combinedResult.add(this)
        }
        getRatesRepository.getCurrencyNames().apply {
            combinedResult.add(this)
        }
        assertTrue(combinedResult.isNotEmpty())
        val first = combinedResult.first()
        val second = combinedResult[1]
        assertTrue(first is ApiResult.Success)
        assertTrue(second is ApiResult.Success)

        if (first is ApiResult.Success && second is ApiResult.Success) {
            val currencyRate = convertToCurrencyRates(first.response, second.response)
            kotlin.test.assertTrue(currencyRate?.rates!!.isNotEmpty())
            mainActivityViewModel.parseRateResults(currencyRate)
        }

    }

    @Test
    fun testCorrectCurrencyConversion() {
        mainActivityViewModel.calculateConversion(1.0)
        assertEquals(mainActivityViewModel.baseCurrency.value.code, "USD")
        assertEquals(
            mainActivityViewModel.currencyRates.value.result!!.rates.first().conversion,
            3.67301
        )
        //Change base currency and check conversion again
        mainActivityViewModel.setBaseCurrency(mainActivityViewModel.currencyRates.value.result!!.rates.last()) // ANG
        mainActivityViewModel.calculateConversion(1.0)
        assertEquals(
            mainActivityViewModel.currencyRates.value.result!!.rates.first().conversion?.to2Dp(),
            2.04
        ) // Rounded to 2 decimal place
    }

    @Test
    fun testWrongCurrencyConversion() {
        mainActivityViewModel.setBaseCurrency(mainActivityViewModel.currencyRates.value.result!!.rates.last()) // Base set to ANG
        mainActivityViewModel.calculateConversion(1.0)
        assertNotEquals(
            mainActivityViewModel.currencyRates.value.result!!.rates.first().conversion,
            3.67301
        ) // Conversion should not be same
    }
}

fun Double.to2Dp(): Double {
    return (this * 100.0).roundToInt() / 100.0
}

