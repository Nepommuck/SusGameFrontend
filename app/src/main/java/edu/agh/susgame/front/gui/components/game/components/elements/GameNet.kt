package edu.agh.susgame.front.gui.components.game.components.elements


import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import edu.agh.susgame.front.gui.components.game.components.drawers.EdgeDrawer
import edu.agh.susgame.front.gui.components.game.components.drawers.NodeDrawer
import edu.agh.susgame.front.managers.GameManager

@Composable
fun GameNet(
    gameManager: GameManager
) {
    val zoomState by remember { gameManager.gameState.zoomState }

    Box(modifier = Modifier
        .fillMaxSize()
        .pointerInput(Unit) {
            detectTransformGestures { _, pan, zoom, _ ->
                zoomState.scale(zoom)
                zoomState.move(pan)
            }
        }
        .graphicsLayer(
            scaleX = zoomState.scaleValue(),
            scaleY = zoomState.scaleValue(),
            translationX = zoomState.translationX(),
            translationY = zoomState.translationY(),
            clip = false
        )

    ) {
        EdgeDrawer(gameManager)
        NodeDrawer(gameManager)
    }
}