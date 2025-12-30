package com.vladusecho.everyweatherpro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.vladusecho.everyweatherpro.WeatherApp
import com.vladusecho.everyweatherpro.presentation.root.DefaultRootComponent
import com.vladusecho.everyweatherpro.presentation.root.RootContent
import com.vladusecho.everyweatherpro.presentation.ui.theme.EveryWeatherProTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApp).component.inject(this)
        val root = rootComponentFactory.create(defaultComponentContext())

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RootContent(root)
        }
    }
}