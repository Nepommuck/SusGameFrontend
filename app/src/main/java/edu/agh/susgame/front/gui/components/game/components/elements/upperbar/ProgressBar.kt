package edu.agh.susgame.front.gui.components.game.components.elements.upperbar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.style.TextAlign
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Calculate
import edu.agh.susgame.front.managers.GameManager
import kotlin.math.roundToInt

@Composable
fun ProgressBar(gameManager: GameManager) {
    val packetsReceived by remember { gameManager.getServerReceivedPackets() }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val progressWidth =
                size.width * Calculate.getFloatProgress(packetsReceived, gameManager.packetsToWin)

            val cornerRadius = 50f

            val roundedRectPath = Path().apply {
                addRoundRect(
                    RoundRect(
                        rect = Rect(0f, 0f, size.width, size.height),
                        cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                    )
                )
            }
            clipPath(roundedRectPath) {
                drawRoundRect(
                    color = Color.LightGray.copy(alpha = 0.1f),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
                drawRect(
                    color = Color.Blue.copy(alpha = 0.9f),
                    size = Size(width = progressWidth, height = size.height)
                )
            }
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.8f),
                size = size,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                style = Stroke(width = 8f)
            )
        }
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Absolute.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LoadAnim()
            Text(
                text = " ${
                    packetsRatio(
                        packetsReceived,
                        gameManager.packetsToWin
                    )
                }% Critical Network Data",
                style = TextStyler.TerminalM,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun packetsRatio(packetsReceived: Int, packetsToWin: Int): String =
    (Calculate.getFloatProgress(packetsReceived, packetsToWin) * 100)
        .roundToInt()
        .toString()