package edu.agh.susgame.front.gui.components.game.components.computer.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion.QuizAnswer
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.components.QuizAnswerBlock
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.components.QuizQuestionBlock
import edu.agh.susgame.front.managers.QuizManager
import edu.agh.susgame.front.managers.state.util.QuizAnswerState


@Composable
fun QuizQuestionView(
    quizQuestion: QuizQuestion,
    answerState: QuizAnswerState,
    quizManager: QuizManager,
) {
    val answers = quizQuestion.answers

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(PaddingL)
        ) {
            QuizQuestionBlock(question = quizQuestion.question)
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(PaddingL)
        ) {
            QuizAnswersRow(
                answers = Pair(answers[0], answers[1]),
                answerState, quizManager,
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(PaddingL)
        ) {
            QuizAnswersRow(
                answers = Pair(answers[2], answers[3]),
                answerState, quizManager,
            )
        }
    }
}


@Composable
private fun QuizAnswersRow(
    answers: Pair<QuizAnswer, QuizAnswer>,
    answerState: QuizAnswerState,
    quizManager: QuizManager,
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        Box(modifier = Modifier.weight(1f)) {
            QuizAnswerBlock(answer = answers.first, answerState, quizManager)
        }
        Box(modifier = Modifier.weight(1f)) {
            QuizAnswerBlock(answer = answers.second, answerState, quizManager)
        }
    }
}
