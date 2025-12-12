package com.vladusecho.everyweatherpro.presentation.favourite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vladusecho.everyweatherpro.presentation.details.DetailsStore
import com.vladusecho.everyweatherpro.presentation.favourite.FavouriteStoreFactory.ExecutorImpl

internal interface FavouriteStore : Store<FavouriteStore.Intent, FavouriteStore.State, FavouriteStore.Label> {

    sealed interface Intent {}

    data class State(val todo: Unit)

    sealed interface Label {}


}

internal class FavouriteStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): FavouriteStore =
        object : FavouriteStore, Store<FavouriteStore.Intent, FavouriteStore.State, FavouriteStore.Label>
                by storeFactory.create(
                    name = "FavouriteStore",
                    initialState = FavouriteStore.State(Unit),
                    bootstrapper = BootstrapperImpl(),
                    executorFactory = ::ExecutorImpl,
                    reducer = ReducerImpl
                ) {}

    private sealed interface Action {}

    private sealed interface Msg {}

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private class ExecutorImpl :
        CoroutineExecutor<FavouriteStore.Intent, Action, FavouriteStore.State, Msg, FavouriteStore.Label>() {
        override fun executeAction(action: Action) {

        }

        override fun executeIntent(intent: FavouriteStore.Intent) {

        }
    }

    private object ReducerImpl : Reducer<FavouriteStore.State, Msg> {
        override fun FavouriteStore.State.reduce(msg: Msg): FavouriteStore.State =
            FavouriteStore.State(Unit)
    }
}