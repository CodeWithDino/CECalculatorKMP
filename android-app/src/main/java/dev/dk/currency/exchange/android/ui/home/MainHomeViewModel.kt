package dev.dk.currency.exchange.android.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import dev.dk.currency.exchange.data.CurrencyRecord
import dev.dk.currency.exchange.data.CurrencyRates
import dev.dk.currency.exchange.data.model.OnResultObtained
import dev.dk.currency.exchange.data.repository.GetCurrencyRatesRepositoryInterface

class MainHomeViewModel : ViewModel(), KoinComponent {

    private val getRatesRepository: GetCurrencyRatesRepositoryInterface by inject()

    private val _currencyRates = MutableStateFlow(OnResultObtained<CurrencyRates>(null, false))
    val currencyRates: StateFlow<OnResultObtained<CurrencyRates>> get() = _currencyRates

    private val _baseCurrency = MutableStateFlow(CurrencyRecord("USD", 1.0, "United States Dollar", 0.0))
    val baseCurrency: StateFlow<CurrencyRecord> get() = _baseCurrency

    private val _baseFactor = MutableStateFlow(1.00)
    val baseFactor: StateFlow<Double> get() = _baseFactor

    fun setBaseCurrency(currency: CurrencyRecord) {
        _baseCurrency.value = currency
        _baseFactor.value = 1.00 / currency.usdRate!!
    }

    fun calculateConversion(amount: Double = 1.0) {
        _currencyRates.value =
            OnResultObtained(_currencyRates.value.result?.copy(rates = _currencyRates.value.result!!.rates.map {
                if (it.conversion == 0.0) {
                    it.conversion = amount * _baseFactor.value * it.usdRate!!
                } else {
                    it.conversion = it.usdRate!! * amount * _baseFactor.value
                }
                it
            }), true)
    }

    fun getCurrencyRates() {
        getRatesRepository.loadData()
        viewModelScope.launch {
            getRatesRepository.currencyRates().collectLatest { result ->
                _currencyRates.value = result
                if (result.result?.rates?.isNotEmpty() == true) {
                    parseRateResults(result.result!!)
                }
            }
        }
    }

    fun parseRateResults(result: CurrencyRates) {
        val rates = result.apply {
            rates = rates.map {
                it.conversion = _baseFactor.value * it.usdRate!!
                it
            }
        }
        _currencyRates.value = OnResultObtained(rates, true)
        if (result.rates.firstOrNull { it.code == _baseCurrency.value.code } != null) {
            _baseCurrency.value =
                result.rates.firstOrNull { it.code == _baseCurrency.value.code }!!
        }
    }

}

