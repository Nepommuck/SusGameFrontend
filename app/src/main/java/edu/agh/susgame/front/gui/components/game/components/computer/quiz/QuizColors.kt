package edu.agh.susgame.front.gui.components.game.components.computer.quiz

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color


object QuizColors {
    val TEXT = Color.White
    val SELECTED_ANSWER_TEXT = Color.Black

    private val BLOCK_TOP = Color(0xff032f7e)
    private val BLOCK_BOTTOM = Color(0xff081e4e)

    private val BLOCK_SELECTED_ANSWER_LEFT = Color(0xffa75a07)
    private val BLOCK_SELECTED_ANSWER_RIGHT = Color(0xffffb503)

    private val BLOCK_CORRECT_ANSWER_LEFT = Color(0xff5ed414)
    private val BLOCK_CORRECT_ANSWER_RIGHT = Color(0xff38790f)

    val QUIZ_BLOCK_GRADIENT = Brush.verticalGradient(
        colors = listOf(BLOCK_TOP, BLOCK_BOTTOM),
    )

    val SELECTED_ANSWER_GRADIENT = Brush.horizontalGradient(
        colors = listOf(BLOCK_SELECTED_ANSWER_LEFT, BLOCK_SELECTED_ANSWER_RIGHT),
    )

    val CORRECT_ANSWER_GRADIENT = Brush.horizontalGradient(
        colors = listOf(BLOCK_CORRECT_ANSWER_LEFT, BLOCK_CORRECT_ANSWER_RIGHT),
    )
}
