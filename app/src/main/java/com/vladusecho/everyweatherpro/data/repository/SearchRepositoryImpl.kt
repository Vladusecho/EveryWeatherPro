package com.vladusecho.everyweatherpro.data.repository

import com.vladusecho.everyweatherpro.data.mappers.toEntities
import com.vladusecho.everyweatherpro.data.network.api.ApiService
import com.vladusecho.everyweatherpro.domain.entities.City
import com.vladusecho.everyweatherpro.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
): SearchRepository {
    override suspend fun search(query: String): List<City> = apiService.searchCity(query).toEntities()
}