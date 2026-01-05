package com.vladusecho.everyweatherpro.domain.usecases

import com.vladusecho.everyweatherpro.domain.repository.FavouriteRepository
import javax.inject.Inject

class CheckIsFavouriteUseCase @Inject constructor(
    private val repository: FavouriteRepository
) {

    suspend operator fun invoke(id: Int) = repository.checkIsFavourite(id)
}