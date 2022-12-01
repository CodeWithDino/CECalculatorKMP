package dev.dk.currency.exchange.android.di

import dev.dk.currency.exchange.android.ui.home.MainHomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { MainHomeViewModel() }
}