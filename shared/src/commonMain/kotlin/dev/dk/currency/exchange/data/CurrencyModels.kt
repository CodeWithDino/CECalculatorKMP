package dev.dk.currency.exchange.data

data class CurrencyRates(
    val base: String = "",
    var rates: List<CurrencyRecord>,
)

data class CurrencyRecord(
    val code: String?,
    var usdRate: Double?,
    var name: String?,
    var conversion: Double? = 0.0
)

