package com.vladusecho.everyweatherpro.domain.usecases

import com.vladusecho.everyweatherpro.domain.repository.FavouriteRepository
import javax.inject.Inject

class ObserveFavouriteStateUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    operator fun invoke(id: Int) = repository.observeIsFavourite(id)
}