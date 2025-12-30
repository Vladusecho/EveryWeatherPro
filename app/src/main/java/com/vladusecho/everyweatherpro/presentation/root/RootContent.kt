package com.vladusecho.everyweatherpro.presentation.root

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.vladusecho.everyweatherpro.presentation.details.DetailsContent
import com.vladusecho.everyweatherpro.presentation.favourite.FavouriteContent
import com.vladusecho.everyweatherpro.presentation.search.SearchContent
import com.vladusecho.everyweatherpro.presentation.ui.theme.EveryWeatherProTheme

@Composable
fun RootContent(
    component: RootComponent
) {
    EveryWeatherProTheme {
        Children(
            stack = component.stack
        ) {
            val instance = it.instance
            when(instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(instance.component)
                }
                is RootComponent.Child.Favourite -> {
                    FavouriteContent(instance.component)
                }
                is RootComponent.Child.Search -> {
                    SearchContent(instance.component)
                }
            }
        }
    }
}