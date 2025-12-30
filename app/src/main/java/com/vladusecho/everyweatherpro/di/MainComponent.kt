package com.vladusecho.everyweatherpro.di

import android.content.Context
import com.vladusecho.everyweatherpro.presentation.MainActivity
import dagger.BindsInstance
import dagger.Component

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        PresentationModule::class
    ]
)
interface MainComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance context: Context
        ) : MainComponent
    }
}