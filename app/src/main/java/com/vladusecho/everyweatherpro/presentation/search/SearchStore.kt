package com.vladusecho.everyweatherpro.presentation.search

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vladusecho.everyweatherpro.presentation.search.SearchStoreFactory.ExecutorImpl

internal interface SearchStore : Store<SearchStore.Intent, SearchStore.State, SearchStore.Label> {

    sealed interface Intent {}

    data class State(val todo: Unit)

    sealed interface Label {}
}

internal class SearchStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): SearchStore =
        object : SearchStore, Store<SearchStore.Intent, SearchStore.State, SearchStore.Label>
                by storeFactory.create(
                    name = "SearchStore",
                    initialState = SearchStore.State(Unit),
                    bootstrapper = BootstrapperImpl(),
                    executorFactory = ::ExecutorImpl,
                    reducer = ReducerImpl
                ) {}

    private sealed interface Action {}

    private sealed interface Msg{}

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private class ExecutorImpl : CoroutineExecutor<SearchStore.Intent, Action, SearchStore.State, Msg, SearchStore.Label>() {
        override fun executeAction(action: Action) {

        }

        override fun executeIntent(intent: SearchStore.Intent) {

        }
    }

    private object ReducerImpl : Reducer<SearchStore.State, Msg> {
        override fun SearchStore.State.reduce(msg: Msg): SearchStore.State = SearchStore.State(Unit)
    }
}