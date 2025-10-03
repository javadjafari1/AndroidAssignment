package ir.miare.androidcodechallenge

import android.app.Application
import ir.miare.androidcodechallenge.data.di.dataModule
import ir.miare.androidcodechallenge.domain.di.domainModule
import ir.miare.androidcodechallenge.ui.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                viewModelModule,
                domainModule,
                dataModule
            )
        }
    }
}
