package edu.agh.susgame.front.gui.components.game.components.computer.quiz

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


object QuizColors {
    val TEXT = Color.White
    val SELECTED_ANSWER_TEXT = Color.Black

    private val BLOCK_TOP = Color(0xFF032F7E)
    private val BLOCK_BOTTOM = Color(0xFF081E4E)

    private val BLOCK_SELECTED_ANSWER = Color.Yellow

    private val BLOCK_RIGHT_ANSWER = Color.Green

    val QUIZ_BLOCK_GRADIENT = Brush.verticalGradient(
        colors = listOf(BLOCK_TOP, BLOCK_BOTTOM),
    )

    val SELECTED_ANSWER_GRADIENT = Brush.horizontalGradient(
        colors = listOf(BLOCK_SELECTED_ANSWER, BLOCK_SELECTED_ANSWER)
    )

    val RIGHT_ANSWER_GRADIENT = Brush.horizontalGradient(
        colors = listOf(BLOCK_RIGHT_ANSWER, BLOCK_RIGHT_ANSWER)
    )
}
