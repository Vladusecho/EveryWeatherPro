package com.vladusecho.everyweatherpro.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.vladusecho.everyweatherpro.WeatherApp
import com.vladusecho.everyweatherpro.domain.usecases.ChangeFavouriteStateUseCase
import com.vladusecho.everyweatherpro.domain.usecases.GetFavouriteCitiesUseCase
import com.vladusecho.everyweatherpro.domain.usecases.SearchCityUseCase
import com.vladusecho.everyweatherpro.presentation.root.DefaultRootComponent
import com.vladusecho.everyweatherpro.presentation.root.RootContent
import com.vladusecho.everyweatherpro.presentation.ui.theme.EveryWeatherProTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var rootComponentFactory: DefaultRootComponent.Factory

    override fun onCreate(savedInstanceState: Bundle?) {
        (applicationContext as WeatherApp).component.inject(this)
        super.onCreate(savedInstanceState)
        val root = rootComponentFactory.create(defaultComponentContext())
        enableEdgeToEdge()
        setContent {
            RootContent(root)
        }
    }
}