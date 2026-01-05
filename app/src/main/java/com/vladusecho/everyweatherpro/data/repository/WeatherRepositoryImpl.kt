package com.vladusecho.everyweatherpro.data.repository

import com.vladusecho.everyweatherpro.data.mappers.toEntity
import com.vladusecho.everyweatherpro.data.network.api.ApiService
import com.vladusecho.everyweatherpro.domain.entities.Forecast
import com.vladusecho.everyweatherpro.domain.entities.Weather
import com.vladusecho.everyweatherpro.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): WeatherRepository {
    override suspend fun getWeather(id: Int): Weather =
        apiService.loadCurrentWeather(PREFIX_CITY_ID + id).toEntity()

    override suspend fun getForecast(id: Int): Forecast =
        apiService.loadForecast(PREFIX_CITY_ID + id).toEntity()

    private companion object {

        private const val PREFIX_CITY_ID = "id: "
    }
}