package dev.dk.currency.exchange

import dev.dk.currency.exchange.utils.CurrencyResponseStringParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CurrencyJsonParserTest {
    @Test
    fun testParseCurrenciesJson(){
        val mapOfCodeAndName = CurrencyResponseStringParser.parseToCurrencyMapName(
            currenciesTestJson
        )
        assertEquals(mapOfCodeAndName.size,8)
        assertEquals(mapOfCodeAndName.keys.first(),"AED")
        assertEquals(mapOfCodeAndName.values.first(),"United Arab Emirates Dirham")
        assertEquals(mapOfCodeAndName["AUD"],"Australian Dollar")
        assertTrue(mapOfCodeAndName["Argentine Peso"] == null)
        assertTrue(mapOfCodeAndName["ANG"] != null)
    }

    @Test
    fun testParseRatesToObject(){
        val parsedObject = CurrencyResponseStringParser.parseAsCurrencyRates(latestTestRatesJson) // CurrencyRates
        assertTrue(parsedObject != null)
        assertTrue(parsedObject.rates.isNotEmpty())
        assertTrue(parsedObject.base == "USD")
        assertEquals(parsedObject.rates.size,5)
        assertTrue(parsedObject.rates.first().usdRate == 3.67301)
    }
}