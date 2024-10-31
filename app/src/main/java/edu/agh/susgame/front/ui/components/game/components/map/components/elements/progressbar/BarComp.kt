package edu.agh.susgame.front.ui.components.game.components.map.components.elements.progressbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color

@Composable
fun BarComp(packetsReceived: Int, packetsToWin: Int) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val progressWidth = size.width * getFloatProgress(packetsReceived, packetsToWin)

        drawRect(
            color = Color.LightGray.copy(alpha = 0.1f),
            size = size
        )

        drawRect(
            color = Color.Blue,
            size = Size(width = progressWidth, height = size.height)
        )
    }
    Text(getFloatProgress(packetsReceived, packetsToWin).toString())
}

private fun getFloatProgress(x: Int, y: Int): Float {
    return (x.toFloat() / y.toFloat()).takeIf { y != 0 } ?: 0.0f
}