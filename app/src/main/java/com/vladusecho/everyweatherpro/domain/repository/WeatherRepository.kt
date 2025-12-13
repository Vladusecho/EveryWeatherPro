package com.vladusecho.everyweatherpro.domain.repository

import com.vladusecho.everyweatherpro.domain.entities.City
import com.vladusecho.everyweatherpro.domain.entities.Forecast
import com.vladusecho.everyweatherpro.domain.entities.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    suspend fun getWeather(id: Int): Weather

    suspend fun getForecast(id: Int): Forecast

}