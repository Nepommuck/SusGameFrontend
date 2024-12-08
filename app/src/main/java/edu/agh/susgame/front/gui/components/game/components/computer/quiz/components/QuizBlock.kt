package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizColors


@Composable
fun QuizBlock(
    width: Dp,
    height: Dp,
    gradient: Brush = QuizColors.QUIZ_BLOCK_GRADIENT,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
    EmeraldShapedBox(
        modifier = Modifier
            .width(width)
            .height(height)
            .let { modifier ->
                if (onClick == null) modifier
                else modifier.clickable(role = Role.Button) { onClick() }
            }
    ) {
        EmeraldShapedBox(
            modifier = Modifier
                .width(width - 6.dp)
                .height(height - 4.dp),
            gradient = gradient,
        ) { content() }
    }
}
