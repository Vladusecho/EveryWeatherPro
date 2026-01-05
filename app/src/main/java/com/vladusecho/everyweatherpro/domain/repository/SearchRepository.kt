package com.vladusecho.everyweatherpro.domain.repository

import com.vladusecho.everyweatherpro.domain.entities.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}