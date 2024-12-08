package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path


/**
 * @param gradient this parameter is ignored, when [color] parameter is defined
 */
@Composable
fun EmeraldShapedBox(
    modifier: Modifier,
    color: Color? = null,
    gradient: Brush? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(contentAlignment = Alignment.Center) {
        Canvas(modifier) {
            val width = size.width
            val height = size.height

            val path = Path().apply {
                moveTo(x = width - height / 2, y = 0f)     // Top right
                lineTo(x = width, y = height / 2)          // Right
                lineTo(x = width - height / 2, y = height) // Bottom right
                lineTo(x = height / 2, y = height)         // Bottom left
                lineTo(x = 0f, y = height / 2)             // Left
                lineTo(x = height / 2, y = 0f)             // Top left
                close()
            }
            if (color != null) {
                drawPath(path, color = color)
            } else if (gradient != null) {
                drawPath(path, brush = gradient)
            } else {
                drawPath(path, color = Color.Black)
            }
        }

        content()
    }
}
