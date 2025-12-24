package com.vladusecho.everyweatherpro

import android.app.Application
import com.vladusecho.everyweatherpro.di.DaggerMainComponent

class WeatherApp : Application() {

    val component by lazy {
        DaggerMainComponent.factory().create(this)
    }
}