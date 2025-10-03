package ir.miare.androidcodechallenge.domain.di

import ir.miare.androidcodechallenge.domain.usecase.ObserveLeaguesWithPlayersUseCase
import ir.miare.androidcodechallenge.domain.usecase.ObserveLeaguesWithPlayersUseCaseImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val domainModule = module {
    factoryOf(::ObserveLeaguesWithPlayersUseCaseImpl) bind ObserveLeaguesWithPlayersUseCase::class
}
