package edu.agh.susgame.front.gui.components.game.components.computer.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import edu.agh.susgame.front.gui.components.common.theme.PaddingL
import edu.agh.susgame.front.gui.components.common.theme.PaddingS
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
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = PaddingL),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        QuizQuestionBlock(question = quizQuestion.question)

        Spacer(modifier = Modifier.height(PaddingL))

        QuizAnswersRow(
            answers = Pair(quizQuestion.answers[0], quizQuestion.answers[1]),
            answerState, quizManager,
        )

        Spacer(modifier = Modifier.height(PaddingS))

        QuizAnswersRow(
            answers = Pair(quizQuestion.answers[2], quizQuestion.answers[3]),
            answerState, quizManager,
        )
    }
}


@Composable
private fun QuizAnswersRow(
    answers: Pair<QuizAnswer, QuizAnswer>,
    answerState: QuizAnswerState,
    quizManager: QuizManager,
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween) {
        QuizAnswerBlock(answer = answers.first, answerState, quizManager)
        QuizAnswerBlock(answer = answers.second, answerState, quizManager)
    }
}
