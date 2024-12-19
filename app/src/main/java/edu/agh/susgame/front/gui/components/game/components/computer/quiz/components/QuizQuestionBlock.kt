package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizColors


@Composable
fun QuizQuestionBlock(question: String) {
    QuizBlock {
        Text(
            question,
            modifier = Modifier.padding(horizontal = 30.dp),
            color = QuizColors.TEXT,
            textAlign = TextAlign.Center,
        )
    }
}
