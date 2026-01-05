package com.vladusecho.everyweatherpro.data.mappers

import com.vladusecho.everyweatherpro.data.network.dto.ForecastDto
import com.vladusecho.everyweatherpro.data.network.dto.WeatherCurrentDto
import com.vladusecho.everyweatherpro.data.network.dto.WeatherDto
import com.vladusecho.everyweatherpro.data.network.dto.WeatherForecastDto
import com.vladusecho.everyweatherpro.domain.entities.Forecast
import com.vladusecho.everyweatherpro.domain.entities.Weather
import java.util.Calendar
import java.util.Date

private const val TO_MILLISECONDS = 1000

fun WeatherCurrentDto.toEntity(): Weather = current.toEntity()

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    description = conditionDto.text,
    descriptionIcon = conditionDto.iconUrl.toCorrectImageUrl(),
    date = date.toCalendar()
)

fun WeatherForecastDto.toEntity(): Forecast = Forecast(
    currentWeather = current.toEntity(),
    upcoming = forecastDto.forecastDayDto.drop(1).map { dayDto ->
        val dayWeatherDto = dayDto.dayWeatherDto
        Weather(
            dayWeatherDto.tempC,
            dayWeatherDto.conditionDto.text,
            dayWeatherDto.conditionDto.iconUrl,
            dayDto.date.toCalendar()
        )
    }
)

private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * TO_MILLISECONDS)
}

private fun String.toCorrectImageUrl() = "https:$this".replace("64x64", "128x128")