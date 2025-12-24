package com.vladusecho.everyweatherpro.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class
    ]
)
interface MainComponent {

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ) : MainComponent
    }
}