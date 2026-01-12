package com.vladusecho.everyweatherpro.presentation.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.random.Random

object CardGradients {

    val gradient1 = Brush.linearGradient(listOf(Color(0xFFFFDF37), Color(0xFFFF5621)))
    val gradient2 = Brush.linearGradient(listOf(Color(0xFF08203E), Color(0xFF557C93)))
    val gradient3 = Brush.linearGradient(listOf(Color(0xFFF4119E), Color(0xFF4A313E)))
    val gradient4 = Brush.linearGradient(listOf(Color(0xFFFFD78A), Color(0xFFF4762D)))
    val gradient5 = Brush.linearGradient(listOf(Color(0xFFD7EBEB), Color(0xFFF4AFE9)))
    val gradient6 = Brush.linearGradient(listOf(Color(0xFFC9DEF4), Color(0xFFF5CCD4)))
    val gradient7 = Brush.linearGradient(listOf(Color(0xFF61F4DE), Color(0xFF6E78FF)))

    fun getAllCardGradients(): List<Brush> {
        return listOf(
            gradient1,
            gradient2,
            gradient3,
            gradient4,
            gradient5,
            gradient6,
            gradient7
        )
    }

    val gradientsCount
        get() = getAllCardGradients().size

    fun getRandomGradient() = getAllCardGradients().random()
}