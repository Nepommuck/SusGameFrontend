package edu.agh.susgame.front.ui.components.game.components.map.components.elements.upperbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.ui.components.common.util.Calculate

@Composable
fun BarComp(packetsReceived: Int, packetsToWin: Int) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val progressWidth = size.width * Calculate.getFloatProgress(packetsReceived, packetsToWin)

        drawRect(
            color = Color.LightGray.copy(alpha = 0.1f),
            size = size
        )

        drawRect(
            color = Color.Blue,
            size = Size(width = progressWidth, height = size.height)
        )
    }
}
