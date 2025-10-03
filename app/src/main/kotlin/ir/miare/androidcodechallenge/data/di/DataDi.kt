package ir.miare.androidcodechallenge.data.di

import androidx.room.Room
import ir.miare.androidcodechallenge.data.db.AppDatabase
import ir.miare.androidcodechallenge.data.local.LocalDataSource
import ir.miare.androidcodechallenge.data.local.LocalDataSourceImpl
import ir.miare.androidcodechallenge.data.remote.RemoteDataSource
import ir.miare.androidcodechallenge.data.remote.RemoteDataSourceImpl
import ir.miare.androidcodechallenge.data.repository.RepositoryImpl
import ir.miare.androidcodechallenge.domain.repository.Repository
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

internal val dataModule = module {
    single {
        Room.databaseBuilder(
            context = get(),
            AppDatabase::class.java,
            name = "app_db"
        )
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }
    single { get<AppDatabase>().getDao() }
    factoryOf(::LocalDataSourceImpl) bind LocalDataSource::class
    factoryOf(::RemoteDataSourceImpl) bind RemoteDataSource::class
    factoryOf(::RepositoryImpl) bind Repository::class
}
