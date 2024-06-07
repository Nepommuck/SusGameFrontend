package edu.agh.susgame.front.ui.util

import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.geometry.Offset
import edu.agh.susgame.front.util.Coordinates
import kotlin.math.max
import kotlin.math.min

class ZoomState(
    private val maxZoomIn: Float,
    private val maxZoomOut: Float,
    private val totalSize: Coordinates,
) {
    init {
        require(maxZoomIn >= 1) { "maxZoomIn must be greater than or equal to 1" }
        require(maxZoomOut <= 1) { "maxZoomOut must be less than or equal to 1" }
        require(maxZoomOut <= maxZoomIn) { "maxZoomOut must be less than or equal to maxZoomIn" }
    }

    private val scale = mutableFloatStateOf(1f)
    private val translationX = mutableFloatStateOf(totalSize.x.toFloat() / 2)
    private val translationY = mutableFloatStateOf(totalSize.y.toFloat() / 2)

    private val centroidX = mutableFloatStateOf(0f)

    fun scaleValue(): Float = scale.floatValue
    fun translationX(): Float = translationX.floatValue
    fun translationY(): Float = translationY.floatValue

    fun scale(zoom: Float) {
        scale.floatValue = max(min(scale.floatValue * zoom, maxZoomIn), maxZoomOut)
    }


    fun move(offset: Offset) { // TODO GAME-47 it doesn't work as it should, right now i don't have any idea how to implement it :(
        val scaledWidth = totalSize.x * scale.floatValue
        val scaledHeight = totalSize.y * scale.floatValue

        translationX.floatValue =
            (translationX.floatValue + offset.x).coerceIn(-scaledWidth, scaledWidth)
        translationY.floatValue =
            (translationY.floatValue + offset.y).coerceIn(-scaledHeight, scaledHeight)
    }

}
