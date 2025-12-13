package com.vladusecho.everyweatherpro.domain.repository

import com.vladusecho.everyweatherpro.domain.entities.City
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {

    val favouriteCities: Flow<City>

    fun observeIsFavourite(id: Int): Flow<Boolean>

    suspend fun addToFavourite(city: City)

    suspend fun removeFromFavourite(id: Int)
}