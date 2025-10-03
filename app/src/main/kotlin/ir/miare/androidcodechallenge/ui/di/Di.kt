package ir.miare.androidcodechallenge.ui.di

import ir.miare.androidcodechallenge.BuildConfig
import ir.miare.androidcodechallenge.core.Logger
import ir.miare.androidcodechallenge.core.LoggerImpl
import ir.miare.androidcodechallenge.ui.screens.followed.FollowedListViewModel
import ir.miare.androidcodechallenge.ui.screens.home.HomeViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module
import timber.log.Timber

internal val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::FollowedListViewModel)

    single {
        if (BuildConfig.DEBUG) {
            Timber.DebugTree()
        } else {
            // replace with crashlytics service tree
            object : Timber.Tree() {
                override fun log(
                    priority: Int,
                    tag: String?,
                    message: String,
                    t: Throwable?
                ) = Unit
            }
        }
    }
    factoryOf(::LoggerImpl) bind Logger::class
}
