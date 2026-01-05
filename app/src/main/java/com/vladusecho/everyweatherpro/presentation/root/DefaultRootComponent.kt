package com.vladusecho.everyweatherpro.presentation.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DelicateDecomposeApi
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.vladusecho.everyweatherpro.domain.entities.City
import com.vladusecho.everyweatherpro.presentation.details.DefaultDetailsComponent
import com.vladusecho.everyweatherpro.presentation.extensions.componentScope
import com.vladusecho.everyweatherpro.presentation.favourite.DefaultFavouriteComponent
import com.vladusecho.everyweatherpro.presentation.search.DefaultSearchComponent
import com.vladusecho.everyweatherpro.presentation.search.OpenReason
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import java.lang.Thread.sleep
import javax.inject.Inject

class DefaultRootComponent @AssistedInject constructor (
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
    private val favouriteComponentFactory: DefaultFavouriteComponent.Factory,
    private val searchComponentFactory: DefaultSearchComponent.Factory,
    @Assisted("componentContext") componentContext: ComponentContext
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Favourite,
        serializer = null,
        handleBackButton = true,
        childFactory = ::child
    )

    @OptIn(DelicateDecomposeApi::class)
    private fun child(
        config: Config,
        componentContext: ComponentContext
    ): RootComponent.Child {
        return when(config) {
            is Config.Details -> {
                val component = detailsComponentFactory.create(
                    config.city,
                    {
                        navigation.pop()
                    },
                    componentContext
                )
                RootComponent.Child.Details(component)
            }
            Config.Favourite -> {
                val component = favouriteComponentFactory.create(
                    {
                        navigation.push(Config.Details(it))
                    },
                    {
                        navigation.push(Config.Search(OpenReason.AddToFavourite))
                    },
                    {
                        navigation.push(Config.Search(OpenReason.RegularSearch))
                    },
                    componentContext
                )
                RootComponent.Child.Favourite(component)
            }
            is Config.Search -> {
                val component = searchComponentFactory.create(
                    config.openReason,
                    {
                        navigation.pop()
                    },
                    {
                        navigation.push(Config.Details(it))
                    },
                    {
                        componentScope().launch {
                            delay(500)
                            navigation.pop()
                        }

                    },
                    componentContext
                )
                RootComponent.Child.Search(component)
            }
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        object Favourite : Config

        @Parcelize
        data class Search(val openReason: OpenReason) : Config

        @Parcelize
        data class Details(val city: City) : Config
    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext
        ) : DefaultRootComponent
    }
}