package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizColors
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion.QuizAnswer
import edu.agh.susgame.front.managers.QuizManager
import edu.agh.susgame.front.managers.state.util.QuizAnswerState
import edu.agh.susgame.front.managers.state.util.QuizAnswerState.Answered
import edu.agh.susgame.front.managers.state.util.QuizAnswerState.Graded
import edu.agh.susgame.front.managers.state.util.QuizAnswerState.NotAnswered


private sealed class AnswerButtonState(val textColor: Color, val blockGradient: Brush) {
    data object NotSelected : AnswerButtonState(
        textColor = QuizColors.TEXT,
        blockGradient = QuizColors.QUIZ_BLOCK_GRADIENT,
    )

    data object Selected : AnswerButtonState(
        textColor = QuizColors.SELECTED_ANSWER_TEXT,
        blockGradient = QuizColors.SELECTED_ANSWER_GRADIENT,
    )

    data object CorrectAnswer : AnswerButtonState(
        textColor = QuizColors.TEXT,
        blockGradient = QuizColors.CORRECT_ANSWER_GRADIENT,
    )
}


@Composable
fun QuizAnswerBlock(
    answer: QuizAnswer,
    answerState: QuizAnswerState,
    quizManager: QuizManager,
) {
    fun getButtonState(): AnswerButtonState {
        return if (answerState is Graded && answer.isCorrect)
            AnswerButtonState.CorrectAnswer
        else if (
            answerState == Answered(selectedAnswer = answer) ||
            answerState == Graded(selectedAnswer = answer)
        ) AnswerButtonState.Selected
        else
            AnswerButtonState.NotSelected
    }

    QuizBlock(
        gradient = getButtonState().blockGradient,
        onClick = {
            if (answerState is NotAnswered) {
                quizManager.answerCurrentQuestion(answer)
            }
        }
    ) {
        Text(
            text = answer.answer,
            color = getButtonState().textColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 10.dp),
            fontSize = 10.sp
        )
    }
}
