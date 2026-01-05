package com.vladusecho.everyweatherpro.domain.usecases

import com.vladusecho.everyweatherpro.domain.repository.SearchRepository
import javax.inject.Inject

class SearchCityUseCase @Inject constructor(
    private val repository: SearchRepository
){

    suspend operator fun invoke(query: String) = repository.search(query)
}