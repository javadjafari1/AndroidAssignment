package ir.miare.androidcodechallenge.ui.di

import ir.miare.androidcodechallenge.ui.screens.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val viewModelModule = module {
    viewModelOf(::HomeViewModel)
}
