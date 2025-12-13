package com.vladusecho.everyweatherpro.domain.usecases

import com.vladusecho.everyweatherpro.domain.repository.WeatherRepository
import javax.inject.Inject

class GetForecastUseCase @Inject constructor(
    private val repository: WeatherRepository
) {

    suspend operator fun invoke(id: Int) = repository.getForecast(id)
}