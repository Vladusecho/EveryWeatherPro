package com.vladusecho.everyweatherpro.data.network.dto

import com.google.gson.annotations.SerializedName

data class ForecastDto(
    @SerializedName("forecastday") val forecastDayDto: List<DayDto>
)
