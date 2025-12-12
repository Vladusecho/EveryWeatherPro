package com.vladusecho.everyweatherpro.presentation.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.vladusecho.everyweatherpro.presentation.details.DetailsStoreFactory.ExecutorImpl

internal interface DetailsStore : Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label> {

    sealed interface Intent {

    }

    data class State(val todo: Unit)

    sealed interface Label {

    }
}

internal class DetailsStoreFactory(
    private val storeFactory: StoreFactory
) {

    fun create(): DetailsStore =
        object : DetailsStore, Store<DetailsStore.Intent, DetailsStore.State, DetailsStore.Label>
                by storeFactory.create(
                    name = "DetailsStore",
                    initialState = DetailsStore.State(Unit),
                    bootstrapper = BootstrapperImpl(),
                    executorFactory = ::ExecutorImpl,
                    reducer = ReducerImpl
                ) {}

    private sealed interface Action {

    }

    private sealed interface Msg {

    }

    private class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {

        }
    }

    private class ExecutorImpl : CoroutineExecutor<DetailsStore.Intent, Action, DetailsStore.State, Msg, DetailsStore.Label>() {
        override fun executeIntent(intent: DetailsStore.Intent) {

        }

        override fun executeAction(action: Action) {

        }
    }

    private object ReducerImpl : Reducer<DetailsStore.State, Msg> {
        override fun DetailsStore.State.reduce(msg: Msg): DetailsStore.State = DetailsStore.State(Unit)
    }
}