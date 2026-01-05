package com.vladusecho.everyweatherpro.data.repository

import android.util.Log
import com.vladusecho.everyweatherpro.data.local.db.FavouriteCitiesDao
import com.vladusecho.everyweatherpro.data.mappers.toDbModel
import com.vladusecho.everyweatherpro.data.mappers.toEntities
import com.vladusecho.everyweatherpro.domain.entities.City
import com.vladusecho.everyweatherpro.domain.repository.FavouriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavouriteRepositoryImpl @Inject constructor(
    private val favouriteCitiesDao: FavouriteCitiesDao
): FavouriteRepository {

    override val favouriteCities: Flow<List<City>> = favouriteCitiesDao.getFavouriteCities()
        .map { it.toEntities() }

    override fun observeIsFavourite(id: Int): Flow<Boolean> = favouriteCitiesDao.observeIsFavourite(id)

    override suspend fun checkIsFavourite(id: Int): Boolean = favouriteCitiesDao.checkIsFavourite(id)

    override suspend fun addToFavourite(city: City) = favouriteCitiesDao
        .addToFavourite(city.toDbModel()).also { Log.d("AddToFavourite", "work") }

    override suspend fun removeFromFavourite(id: Int) = favouriteCitiesDao
        .deleteFromFavourite(id)
}