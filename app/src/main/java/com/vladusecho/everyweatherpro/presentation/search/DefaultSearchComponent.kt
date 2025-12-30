package com.vladusecho.everyweatherpro.presentation.search

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.vladusecho.everyweatherpro.domain.entities.City
import com.vladusecho.everyweatherpro.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DefaultSearchComponent @AssistedInject constructor(
    private val searchStoreFactory: SearchStoreFactory,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("onOpenForecastClicked") private val onOpenForecastClicked: (City) -> Unit,
    @Assisted("onSavedToFavouriteClicked") private val onSavedToFavouriteClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { searchStoreFactory.create(openReason) }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when(it) {
                    SearchStore.Label.ClickBack -> onBackClicked()
                    is SearchStore.Label.OpenForecast -> onOpenForecastClicked(it.city)
                    SearchStore.Label.SavedToFavourite -> onSavedToFavouriteClicked()
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State> = store.stateFlow

    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onClickSearch(query: String) {
        store.accept(SearchStore.Intent.ClickSearch(query))
    }

    override fun onClickCity(city: City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onOpenForecastClicked") onOpenForecastClicked: (City) -> Unit,
            @Assisted("onSavedToFavouriteClicked") onSavedToFavouriteClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ) : DefaultSearchComponent
    }
}