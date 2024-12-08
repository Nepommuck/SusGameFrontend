package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizColors


@Composable
fun QuizQuestionBlock(question: String) {
    QuizBlock(
        width = 350.dp,
        height = 80.dp,
    ) {
        Text(question, color = QuizColors.TEXT)
    }
}
