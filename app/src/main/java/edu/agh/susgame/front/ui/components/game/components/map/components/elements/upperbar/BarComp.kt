package edu.agh.susgame.front.ui.components.game.components.map.components.elements.upperbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import edu.agh.susgame.front.ui.components.common.util.Calculate
import edu.agh.susgame.front.ui.graph.GameManager

@Composable
fun BarComp(gameManager: MutableState<GameManager>) {

    Canvas(modifier = Modifier.fillMaxSize()) {
        val progressWidth = size.width * Calculate.getFloatProgress(gameManager.value.packetsRec.value, gameManager.value.packetsToWin)

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

