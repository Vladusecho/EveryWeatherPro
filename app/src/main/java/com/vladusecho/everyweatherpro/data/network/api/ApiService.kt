package com.vladusecho.everyweatherpro.data.network.api

import com.vladusecho.everyweatherpro.data.network.dto.CityDto
import com.vladusecho.everyweatherpro.data.network.dto.WeatherCurrentDto
import com.vladusecho.everyweatherpro.data.network.dto.WeatherForecastDto
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET("current.json")
    suspend fun loadCurrentWeather(
        @Query("q") query: String,
        @Query("lang") lang: String = "ru"
    ): WeatherCurrentDto

    @GET("forecast.json")
    suspend fun loadForecast(
        @Query("q") query: String,
        @Query("days") daysCount: Int = 4,
        @Query("lang") lang: String = "ru"
    ): WeatherForecastDto

    @GET("search.json")
    suspend fun searchCity(
        @Query("q") query: String
    ): List<CityDto>
}