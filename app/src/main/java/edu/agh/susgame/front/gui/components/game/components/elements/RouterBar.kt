package edu.agh.susgame.front.gui.components.game.components.elements

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import edu.agh.susgame.front.gui.components.common.graph.node.Router
import edu.agh.susgame.front.gui.components.common.theme.TextStyler
import edu.agh.susgame.front.gui.components.common.util.Calculate

@Composable
fun RouterBar(
    router: Router,
    width: Float,
    padding: Dp,
    textStyle: TextStyle
) {
    val bufferCurrentPackets by remember { router.bufferCurrentPackets }
    val bufferSize by remember { router.bufferSize }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth(width)
            .padding(padding),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val progressWidth =
                size.width * Calculate.getFloatProgress(bufferCurrentPackets, bufferSize)

            val dynamicColor = lerp(
                start = Color.Green,
                stop = Color.Red,
                fraction = Calculate.getFloatProgress(bufferCurrentPackets, bufferSize)
            )

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
                    color = Color.LightGray.copy(alpha = 0.2f),
                    size = size,
                    cornerRadius = CornerRadius(cornerRadius, cornerRadius)
                )
                drawRect(
                    color = dynamicColor.copy(alpha = 0.8f),
                    size = Size(width = progressWidth, height = size.height)
                )
            }
            drawRoundRect(
                color = Color.Black.copy(alpha = 0.8f),
                size = size,
                cornerRadius = CornerRadius(cornerRadius, cornerRadius),
                style = Stroke(width = 4f)
            )
        }
        Text(text = router.getBuffer(), style = textStyle)
    }
}