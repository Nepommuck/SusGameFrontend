package edu.agh.susgame.front.managers.state.util

import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion


sealed class QuizAnswerState {
    data object NotAnswered : QuizAnswerState()

    data class Answered(val selectedAnswer: QuizQuestion.QuizAnswer) : QuizAnswerState()

    data class Graded(val selectedAnswer: QuizQuestion.QuizAnswer) : QuizAnswerState()
}

sealed class QuizState {
    data object QuestionNotAvailable : QuizState()

    data class QuestionAvailable(
        val question: QuizQuestion,
        val answerState: QuizAnswerState,
    ) : QuizState()
}
