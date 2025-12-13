package com.vladusecho.everyweatherpro.domain.usecases

import com.vladusecho.everyweatherpro.domain.repository.FavouriteRepository
import javax.inject.Inject

class GetFavouriteCitiesUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    operator fun invoke() = repository.favouriteCities
}