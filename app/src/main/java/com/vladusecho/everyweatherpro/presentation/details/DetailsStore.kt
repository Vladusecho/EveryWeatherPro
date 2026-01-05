package com.vladusecho.everyweatherpro.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vladusecho.everyweatherpro.domain.entities.City
import com.vladusecho.everyweatherpro.domain.entities.Forecast
import com.vladusecho.everyweatherpro.domain.usecases.ChangeFavouriteStateUseCase
import com.vladusecho.everyweatherpro.domain.usecases.CheckIsFavouriteUseCase
import com.vladusecho.everyweatherpro.domain.usecases.GetForecastUseCase
import com.vladusecho.everyweatherpro.domain.usecases.ObserveFavouriteStateUseCase
import com.vladusecho.everyweatherpro.presentation.details.DetailsStoreFactory.ExecutorImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {

    sealed interface Intent {


        object ClickBack : Intent

        object ClickChangeFavouriteStatus : Intent
    }

    data class State(
        val city: City,
        val isFavourite: Boolean,
        val forecastState: ForecastState
    ) {

        sealed interface ForecastState {

            object Initial : ForecastState

            object Error : ForecastState

            object Loading : ForecastState

            data class Content(val forecast: Forecast): ForecastState
        }
    }

    sealed interface Label {


        object ClickBack : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val observeFavouriteStateUseCase: ObserveFavouriteStateUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase,
    private val checkIsFavouriteUseCase: CheckIsFavouriteUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label>
                by storeFactory.create(
                    name = "DetailsStore",
                    initialState = DetailsStore.State(
                        city,
                        false,
                        DetailsStore.State.ForecastState.Initial
                    ),
                    bootstrapper = BootstrapperImpl(city),
                    executorFactory = { ExecutorImpl(city) },
                    reducer = ReducerImpl
                ) {}

    private sealed interface Action {

        data class FavouriteStatusChanged(val isFavourite: Boolean) : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        object ForecastLoadingStart : Action

        object ForecastLoadingError : Action
    }

    private sealed interface Msg {

        data class FavouriteStatusChanged(val isFavourite: Boolean) : Msg

        data class ForecastLoaded(val forecast: Forecast) : Msg

        object ForecastLoadingStart : Msg

        object ForecastLoadingError : Msg
    }

    private inner class BootstrapperImpl(
        private val city: City
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavouriteStateUseCase(city.id).collect {
                    dispatch(Action.FavouriteStatusChanged(it))
                }
            }
            scope.launch {
                dispatch(Action.ForecastLoadingStart)
                try {
                    val forecast = getForecastUseCase(city.id)
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl(
        private val city: City
    ) : CoroutineExecutor<DetailsStore.Intent, Action, DetailsStore.State, Msg, DetailsStore.Label>() {
        override fun executeIntent(intent: DetailsStore.Intent) {
            when(intent) {
                DetailsStore.Intent.ClickBack -> {
                    publish(DetailsStore.Label.ClickBack)
                }
                DetailsStore.Intent.ClickChangeFavouriteStatus -> {
                    scope.launch(Dispatchers.IO) {
                        val isFavourite = checkIsFavouriteUseCase(city.id)
                        if (isFavourite) {
                            changeFavouriteStateUseCase.removeFromFavourite(city.id)
                        } else {
                            changeFavouriteStateUseCase.addToFavourite(city)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action) {
            when(action) {
                is Action.FavouriteStatusChanged -> {
                    dispatch(Msg.FavouriteStatusChanged(action.isFavourite))
                }
                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }
                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }
                Action.ForecastLoadingStart -> {
                    dispatch(Msg.ForecastLoadingStart)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<DetailsStore.State, Msg> {
        override fun DetailsStore.State.reduce(msg: Msg): DetailsStore.State = when(msg) {
            is Msg.FavouriteStatusChanged -> {
                copy(isFavourite = msg.isFavourite)
            }
            is Msg.ForecastLoaded -> {
                copy(forecastState = DetailsStore.State.ForecastState.Content(msg.forecast))
            }
            Msg.ForecastLoadingError -> {
                copy(forecastState = DetailsStore.State.ForecastState.Error)
            }
            Msg.ForecastLoadingStart -> {
                copy(forecastState = DetailsStore.State.ForecastState.Loading)
            }
        }
    }
}