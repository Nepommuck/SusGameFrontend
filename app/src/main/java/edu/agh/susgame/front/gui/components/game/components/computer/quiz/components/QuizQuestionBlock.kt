package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizColors


@Composable
fun QuizQuestionBlock(question: String) {
    QuizBlock(
        widthPercentage = 46.0,
    ) {
        Text(question, color = QuizColors.TEXT)
    }
}
