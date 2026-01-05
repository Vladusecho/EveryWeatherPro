package com.vladusecho.everyweatherpro.presentation.search

import com.vladusecho.everyweatherpro.domain.entities.City
import kotlinx.coroutines.flow.StateFlow

interface SearchComponent {

    val model: StateFlow<SearchStore.State>

    fun changeSearchQuery(query: String)

    fun onClickBack()

    fun onClickSearch(query: String)

    fun onClickCity(city: City)
}