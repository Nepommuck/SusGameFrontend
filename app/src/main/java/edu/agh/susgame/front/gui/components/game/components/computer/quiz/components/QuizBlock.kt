package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.Role
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizColors


@Composable
fun QuizBlock(
    gradient: Brush = QuizColors.QUIZ_BLOCK_GRADIENT,
    onClick: (() -> Unit)? = null,
    content: @Composable BoxScope.() -> Unit,
) {
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
