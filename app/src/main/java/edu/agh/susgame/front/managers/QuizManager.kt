package edu.agh.susgame.front.managers

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion
import edu.agh.susgame.front.managers.state.util.QuizAnswerState
import edu.agh.susgame.front.managers.state.util.QuizState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentLinkedQueue
import kotlin.time.Duration.Companion.seconds

class QuizManager {
    private val _quizState = mutableStateOf<QuizState>(QuizState.QuestionNotAvailable)
    private val availableQuestions = ConcurrentLinkedQueue<QuizQuestion>()

    val quizState = derivedStateOf { _quizState.value }

    fun handleNewQuestion(newQuestion: QuizQuestion) {
        availableQuestions.add(newQuestion)
        loadNewQuestionIfAvailable()
    }

    fun answerCurrentQuestion(answer: QuizQuestion.QuizAnswer) {
        val stateSnapshot = _quizState.value
        if (stateSnapshot is QuizState.QuestionAvailable &&
            stateSnapshot.answerState == QuizAnswerState.NotAnswered
        ) {
            _quizState.value = stateSnapshot.copy(
                answerState = QuizAnswerState.Answered(selectedAnswer = answer)
            )

            CoroutineScope(Dispatchers.Main).launch {
                delay(2.seconds)
                handleQuestionGrading()
            }
        }
    }

    private fun handleQuestionGrading() {
        val stateSnapshot = _quizState.value
        if (stateSnapshot is QuizState.QuestionAvailable &&
            stateSnapshot.answerState is QuizAnswerState.Answered
        ) {
            _quizState.value = stateSnapshot.copy(
                answerState = QuizAnswerState.Graded(
                    selectedAnswer = stateSnapshot.answerState.selectedAnswer,
                )
            )

            CoroutineScope(Dispatchers.Main).launch {
                delay(2.seconds)
                clearGradedQuestion()
                delay(2.seconds)
                loadNewQuestionIfAvailable()
            }
        }
    }

    private fun clearGradedQuestion() {
        val stateSnapshot = _quizState.value
        if (stateSnapshot is QuizState.QuestionAvailable &&
            stateSnapshot.answerState is QuizAnswerState.Graded
        ) {
            _quizState.value = QuizState.QuestionNotAvailable
        }
    }

    private fun loadNewQuestionIfAvailable() {
        if (_quizState.value is QuizState.QuestionAvailable) {
            return
        }

        availableQuestions.poll()?.let { nextQuestion ->
            _quizState.value = QuizState.QuestionAvailable(
                question = nextQuestion,
                answerState = QuizAnswerState.NotAnswered,
            )
        }
    }
}
