package com.vladusecho.everyweatherpro.di

import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import dagger.Module
import dagger.Provides

@Module
interface PresentationModule {

    companion object {

        @Provides
        fun provideStoreFactory() : StoreFactory {
            return DefaultStoreFactory()
        }
    }
}