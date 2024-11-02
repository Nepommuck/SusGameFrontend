package edu.agh.susgame.front.ui.components.common.util

object Calculate {
    fun getFloatProgress(x: Int, y: Int): Float {
        return (x.toFloat() / y.toFloat()).takeIf { y != 0 } ?: 0.0f
    }

    fun incrementAnimIndex(currentIndex: Int, maxIndex: Int): Int {
        return (currentIndex + 1) % maxIndex
    }
}