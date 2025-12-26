package com.vladusecho.everyweatherpro.presentation.favourite

import com.vladusecho.everyweatherpro.domain.entities.City
import kotlinx.coroutines.flow.StateFlow

interface FavouriteComponent {

    val model: StateFlow<FavouriteStore.State>

    fun onClickSearch()

    fun onClickAddFavourite()

    fun onCityItemClick(city: City)
}