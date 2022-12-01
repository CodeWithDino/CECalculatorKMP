package dev.dk.currency.exchange.di

import dev.dk.currency.exchange.data.repository.GetCurrencyRatesRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RepositoryHelper : KoinComponent {
    private val repository: GetCurrencyRatesRepository by inject()
    fun getRepo() = repository
}

