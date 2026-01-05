package com.vladusecho.everyweatherpro.di

import android.content.Context
import com.vladusecho.everyweatherpro.data.local.db.FavouriteCitiesDao
import com.vladusecho.everyweatherpro.data.local.db.FavouriteDatabase
import com.vladusecho.everyweatherpro.data.network.api.ApiFactory
import com.vladusecho.everyweatherpro.data.network.api.ApiService
import com.vladusecho.everyweatherpro.data.repository.FavouriteRepositoryImpl
import com.vladusecho.everyweatherpro.data.repository.SearchRepositoryImpl
import com.vladusecho.everyweatherpro.data.repository.WeatherRepositoryImpl
import com.vladusecho.everyweatherpro.domain.repository.FavouriteRepository
import com.vladusecho.everyweatherpro.domain.repository.SearchRepository
import com.vladusecho.everyweatherpro.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindFavoriteRepository(impl: FavouriteRepositoryImpl): FavouriteRepository

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService {
            return ApiFactory.apiService
        }

        @[ApplicationScope Provides]
        fun provideDatabase(context: Context): FavouriteDatabase {
            return FavouriteDatabase.getInstance(context)
        }

        @[ApplicationScope Provides]
        fun provideDao(database: FavouriteDatabase): FavouriteCitiesDao {
            return database.favouriteCitiesDao()
        }
    }
}