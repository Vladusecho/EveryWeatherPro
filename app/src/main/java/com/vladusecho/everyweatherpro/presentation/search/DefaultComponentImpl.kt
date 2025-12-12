package com.vladusecho.everyweatherpro.presentation.search

import com.arkivanov.decompose.ComponentContext

class DefaultComponentImpl(
    componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext