package com.vladusecho.everyweatherpro.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vladusecho.everyweatherpro.domain.entities.City
import com.vladusecho.everyweatherpro.domain.usecases.GetCurrentWeatherUseCase
import com.vladusecho.everyweatherpro.domain.usecases.GetFavouriteCitiesUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavouriteStore :
    Store<FavouriteStore.Intent, FavouriteStore.State, FavouriteStore.Label> {

    sealed interface Intent {

        object ClickSearch : Intent

        object ClickAddToFavorite : Intent

        data class CityItemClicked(val city: City) : Intent
    }

    data class State(
        val cityItems: List<CityItem>
    ) {

        data class CityItem(
            val city: City,
            val weatherState: WeatherState
        )

        sealed interface WeatherState {

            object Initial : WeatherState

            object Loading : WeatherState

            object Error : WeatherState

            data class Content(
                val tempC: Float,
                val iconUrl: String
            ) : WeatherState
        }
    }

    sealed interface Label {

        object ClickSearch : Label

        object ClickAddToFavorite : Label

        data class CityItemClicked(val city: City) : Label
    }


}

class FavouriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavouriteCitiesUseCase: GetFavouriteCitiesUseCase,
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) {

    fun create(): FavouriteStore =
        object : FavouriteStore,
            Store<FavouriteStore.Intent, FavouriteStore.State, FavouriteStore.Label>
            by storeFactory.create(
                name = "FavouriteStore",
                initialState = FavouriteStore.State(listOf()),
                bootstrapper = BootstrapperImpl(),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed interface Action {

        data class FavouriteCitiesLoaded(val cities: List<City>): Action
    }

    private sealed interface Msg {

        data class FavouriteCitiesLoaded(val cities: List<City>): Msg

        data class WeatherLoaded(
            val cityId: Int,
            val tempC: Float,
            val descriptionIcon: String
        ): Msg

        data class WeatherLoadingError(
            val cityId: Int
        ): Msg

        data class WeatherIsLoading(
            val cityId: Int
        ) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavouriteCitiesUseCase().collect {
                    dispatch(Action.FavouriteCitiesLoaded(it))
                }
            }
        }
    }

    private inner class ExecutorImpl :
        CoroutineExecutor<FavouriteStore.Intent, Action, FavouriteStore.State, Msg, FavouriteStore.Label>() {
        override fun executeAction(action: Action) {
            when(action) {
                is Action.FavouriteCitiesLoaded -> {
                    val cities = action.cities
                    dispatch(Msg.FavouriteCitiesLoaded(cities))
                    cities.forEach {
                        scope.launch {
                            loadWeatherForCity(it)
                        }
                    }
                }
            }
        }

        private suspend fun loadWeatherForCity(city: City) {
            dispatch(Msg.WeatherIsLoading(city.id))
            try {
                val weather = getCurrentWeatherUseCase(city.id)
                dispatch(Msg.WeatherLoaded(
                    city.id,
                    weather.tempC,
                    weather.descriptionIcon
                ))
            } catch (e: Exception) {
                dispatch(Msg.WeatherLoadingError(city.id))
            }
        }

        override fun executeIntent(intent: FavouriteStore.Intent) {
            when(intent) {
                is FavouriteStore.Intent.CityItemClicked -> {
                    publish(FavouriteStore.Label.CityItemClicked(intent.city))
                }
                FavouriteStore.Intent.ClickSearch -> {
                    publish(FavouriteStore.Label.ClickSearch)
                }
                FavouriteStore.Intent.ClickAddToFavorite -> {
                    publish(FavouriteStore.Label.ClickAddToFavorite)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<FavouriteStore.State, Msg> {
        override fun FavouriteStore.State.reduce(msg: Msg): FavouriteStore.State =
            when(msg) {
                is Msg.FavouriteCitiesLoaded -> {
                    copy(
                        msg.cities.map {
                            FavouriteStore.State.CityItem(
                                it,
                                FavouriteStore.State.WeatherState.Initial
                            )
                        }
                    )
                }
                is Msg.WeatherIsLoading -> {
                    copy(
                        cityItems.map {
                            if (it.city.id == msg.cityId) {
                                it.copy(weatherState = FavouriteStore.State.WeatherState.Loading)
                            } else {
                                it
                            }
                        }
                    )
                }
                is Msg.WeatherLoaded -> {
                    copy(
                        cityItems.map {
                            if (it.city.id == msg.cityId) {
                                it.copy(weatherState = FavouriteStore.State.WeatherState.Content(
                                    msg.tempC,
                                    msg.descriptionIcon
                                ))
                            } else {
                                it
                            }
                        }
                    )
                }
                is Msg.WeatherLoadingError -> {
                    copy(
                        cityItems.map {
                            if (it.city.id == msg.cityId) {
                                it.copy(weatherState = FavouriteStore.State.WeatherState.Error)
                            } else {
                                it
                            }
                        }
                    )
                }
            }
    }
}