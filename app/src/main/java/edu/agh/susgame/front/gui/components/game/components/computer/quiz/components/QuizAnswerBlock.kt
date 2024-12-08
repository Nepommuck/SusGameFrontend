package edu.agh.susgame.front.gui.components.game.components.computer.quiz.components

//import edu.agh.susgame.front.gui.components.game.components.computer.quiz.components.AnswerButtonState.NotSelected
//import edu.agh.susgame.front.gui.components.game.components.computer.quiz.components.AnswerButtonState.RightAnswer
//import edu.agh.susgame.front.gui.components.game.components.computer.quiz.components.AnswerButtonState.Selected
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizColors
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion.QuizAnswer
import edu.agh.susgame.front.managers.state.util.QuizAnswerState
import edu.agh.susgame.front.managers.state.util.QuizAnswerState.Answered
import edu.agh.susgame.front.managers.state.util.QuizAnswerState.Graded
import edu.agh.susgame.front.managers.state.util.QuizAnswerState.NotAnswered
import edu.agh.susgame.front.managers.state.util.QuizState

//private enum class AnswerButtonState {
//    NOT_SELECTED, SELECTED, RIGHT_ANSWER
//}

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
        blockGradient = QuizColors.RIGHT_ANSWER_GRADIENT,
    )
}

@Composable
fun QuizAnswerBlock(
    answer: QuizAnswer,
    question: QuizQuestion,
    answerState: QuizAnswerState,
    quizState: MutableState<QuizState>,
) {
//    val answerState by remember { derivedStateOf { quizState.value.answerState } }

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
        width = 150.dp,
        height = 40.dp,
        gradient = getButtonState().blockGradient,
        onClick = {
            println(answerState)
            if (answerState is NotAnswered) {
                quizState.value = QuizState.QuestionAvailable(
                    question = question,
                    answerState = Answered(selectedAnswer = answer),
                )
            } else if (answerState is Answered) {
                quizState.value = QuizState.QuestionAvailable(
                    question = question,
                    answerState = Graded(selectedAnswer = answerState.selectedAnswer),
                )
            }
        }
    ) {
        Text(text = answer.answer, color = getButtonState().textColor)
    }
}
