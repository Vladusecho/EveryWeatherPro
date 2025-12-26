package com.vladusecho.everyweatherpro.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vladusecho.everyweatherpro.domain.entities.City
import com.vladusecho.everyweatherpro.domain.usecases.ChangeFavouriteStateUseCase
import com.vladusecho.everyweatherpro.domain.usecases.SearchCityUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import javax.inject.Inject

interface SearchStore : Store<SearchStore.Intent, SearchStore.State, SearchStore.Label> {

    sealed interface Intent {

        data class ChangeSearchQuery(val query: String) : Intent

        object ClickBack : Intent

        data class ClickSearch(val query: String) : Intent

        data class ClickCity(val city: City) : Intent
    }

    data class State(
        val searchQuery: String,
        val searchState: SearchState
    ) {

        sealed interface SearchState {

            object Initial : SearchState

            object Error : SearchState

            object Loading : SearchState

            object EmptyResult : SearchState

            data class Content(val cities: List<City>) : SearchState
        }
    }

    sealed interface Label {

        object ClickBack : Label

        object SavedToFavourite : Label

        data class OpenForecast(val city: City) : Label
    }
}

class SearchStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val searchCityUseCase: SearchCityUseCase,
    private val changeFavouriteStateUseCase: ChangeFavouriteStateUseCase
) {

    fun create(openReason: OpenReason): SearchStore =
        object : SearchStore, Store<SearchStore.Intent, SearchStore.State, SearchStore.Label>
        by storeFactory.create(
            name = "SearchStore",
            initialState = SearchStore.State(
                "",
                SearchStore.State.SearchState.Initial
            ),
            bootstrapper = BootstrapperImpl(),
            executorFactory = { ExecutorImpl(openReason) },
            reducer = ReducerImpl
        ) {}

    private sealed interface Action

    private sealed interface Msg {

        data class ChangeSearchQuery(val query: String) : Msg

        object LoadingSearchResult : Msg

        object SearchResultError : Msg

        data class Content(val cities: List<City>) : Msg
    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private inner class ExecutorImpl(private val openReason: OpenReason) :
        CoroutineExecutor<SearchStore.Intent, Action, SearchStore.State, Msg, SearchStore.Label>() {

        private var searchJob: Job? = null

        override fun executeIntent(intent: SearchStore.Intent) {
            when (intent) {
                is SearchStore.Intent.ChangeSearchQuery -> {
                    dispatch(Msg.ChangeSearchQuery(intent.query))
                }

                SearchStore.Intent.ClickBack -> {
                    publish(SearchStore.Label.ClickBack)
                }

                is SearchStore.Intent.ClickCity -> {
                    when (openReason) {
                        OpenReason.RegularSearch -> {
                            publish(SearchStore.Label.OpenForecast(city = intent.city))
                        }

                        OpenReason.AddToFavourite -> {
                            scope.launch {
                                changeFavouriteStateUseCase.addToFavourite(intent.city)
                            }
                            publish(SearchStore.Label.SavedToFavourite)
                        }
                    }
                }

                is SearchStore.Intent.ClickSearch -> {
                    searchJob?.cancel()
                    searchJob = scope.launch {
                        dispatch(Msg.LoadingSearchResult)
                        val cities = searchCityUseCase(intent.query)
                        dispatch(Msg.Content(cities))
                    }
                }
            }
        }
    }

    private object ReducerImpl : Reducer<SearchStore.State, Msg> {
        override fun SearchStore.State.reduce(msg: Msg): SearchStore.State =
            when (msg) {
                is Msg.ChangeSearchQuery -> {
                    copy(searchQuery = msg.query)
                }

                is Msg.Content -> {
                    val searchState = if (msg.cities.isEmpty()) {
                        SearchStore.State.SearchState.EmptyResult
                    } else {
                        SearchStore.State.SearchState.Content(msg.cities)
                    }
                    copy(searchState = searchState)
                }

                Msg.LoadingSearchResult -> {
                    copy(searchState = SearchStore.State.SearchState.Loading)
                }

                Msg.SearchResultError -> {
                    copy(searchState = SearchStore.State.SearchState.Error)
                }
            }
    }
}