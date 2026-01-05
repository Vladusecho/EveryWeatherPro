package com.vladusecho.everyweatherpro.domain.usecases

import com.vladusecho.everyweatherpro.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(id: Int) = repository.getWeather(id)
}