package edu.agh.susgame.front.gui.components.common.util

import kotlin.random.Random

class ColorProvider {
    private val colors = mutableListOf(
        0xFF5733L, // Pomarańczowy
        0x33FF57L, // Zielony
        0x3357FFL, // Niebieski
        0xFF33A1L, // Różowy
        0xFFD633L, // Żółty
        0x33FFF5L, // Turkusowy
        0x8E33FFL  // Fioletowy
    )

    private fun generateRandomColor(): Long {
        return Random.nextLong(0xFFFFFF + 1L)
    }

    fun getColor(): Long {
        if (colors.isEmpty()) {
            val randomColor = generateRandomColor()
            colors.add(randomColor)
        }
        return colors.removeAt(Random.nextInt(colors.size))
    }
}