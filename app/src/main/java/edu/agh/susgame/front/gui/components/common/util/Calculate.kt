package edu.agh.susgame.front.gui.components.common.util

object Calculate {
    fun getFloatProgress(packetsReceived: Int, packetsToWin: Int): Float =
        if (packetsToWin == 0) 0f else packetsReceived.toFloat() / packetsToWin.toFloat()

    fun incrementAnimIndex(currentIndex: Int, maxIndex: Int): Int {
        return (currentIndex + 1) % maxIndex
    }

    fun getAlpha(isVisible: Boolean): Float {
        return if (isVisible) 1f else 0.5f
    }
}