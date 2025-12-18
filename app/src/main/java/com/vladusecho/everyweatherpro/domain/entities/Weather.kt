package com.vladusecho.everyweatherpro.domain.entities

import java.util.Calendar

data class Weather(
    val tempC: Float,
    val description: String,
    val descriptionIcon: String,
    val date: Calendar
)
