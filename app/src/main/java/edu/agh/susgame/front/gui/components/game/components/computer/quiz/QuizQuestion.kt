package edu.agh.susgame.front.gui.components.game.components.computer.quiz

import edu.agh.susgame.dto.socket.ServerSocketMessage


data class QuizQuestion(
    val id: QuizQuestionId,
    val question: String,
    val answers: List<QuizAnswer>,
) {
    data class QuizQuestionId(val value: Int)

    data class QuizAnswerId(val value: Int)

    data class QuizAnswer(val id: QuizAnswerId, val answer: String, val isCorrect: Boolean)

    companion object {
        fun fromDto(dto: ServerSocketMessage.QuizQuestionDTO): QuizQuestion {
            require(dto.answers.size in 2..4)
            require(dto.correctAnswer in 0..<dto.answers.size)

            return QuizQuestion(
                id = QuizQuestionId(dto.questionId),
                question = dto.question,
                answers = dto.answers.mapIndexed { index, answer ->
                    QuizAnswer(
                        id = QuizAnswerId(index),
                        answer = answer,
                        isCorrect = dto.correctAnswer == index,
                    )
                }
            )
        }
    }
}
