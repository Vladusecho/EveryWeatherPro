package com.vladusecho.everyweatherpro.domain.entities

data class Forecast(
    val currentWeather: Weather,
    val upcoming: List<Weather>
)
