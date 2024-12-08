package edu.agh.susgame.front.gui.components.game.components.computer.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.components.QuizAnswerBlock
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.components.QuizQuestionBlock
import edu.agh.susgame.front.managers.state.util.QuizAnswerState
import edu.agh.susgame.front.managers.state.util.QuizState


@Composable
fun QuizQuestionView(
    quizQuestion: QuizQuestion,
    answerState: QuizAnswerState,
    quizState: MutableState<QuizState>,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizQuestionBlock(question = quizQuestion.question)

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            QuizAnswerBlock(answer = quizQuestion.answers[0], quizQuestion, answerState, quizState)
            QuizAnswerBlock(answer = quizQuestion.answers[1], quizQuestion, answerState, quizState)
        }

        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            QuizAnswerBlock(answer = quizQuestion.answers[2], quizQuestion, answerState, quizState)
            QuizAnswerBlock(answer = quizQuestion.answers[3], quizQuestion, answerState, quizState)
        }
    }
}
