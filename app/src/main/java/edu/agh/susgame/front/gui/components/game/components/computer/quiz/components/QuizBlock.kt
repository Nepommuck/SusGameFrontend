package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizColors


@Composable
private fun calculateScreenWidth(percentage: Double): Dp =
    (LocalConfiguration.current.screenWidthDp * percentage / 100.0).dp


@Composable
fun QuizBlock(
    widthPercentage: Double,
    gradient: Brush = QuizColors.QUIZ_BLOCK_GRADIENT,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    val width = calculateScreenWidth(widthPercentage)
    val height = width / 4

    Box(
        modifier = Modifier
            .fillMaxSize()
            .let { modifier ->
                if (onClick == null) modifier
                else modifier.clickable(role = Role.Button) { onClick() }
            }
    ) {
        EmeraldShapedBox(
            modifier = Modifier.fillMaxSize(),
            gradient = gradient,
        ) { content() }
    }
}
