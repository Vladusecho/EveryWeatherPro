package com.vladusecho.everyweatherpro.data.local.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("favourite_cities")
data class CityDbModel(
    @PrimaryKey val id: Int,
    val name: String,
    val country: String
)
