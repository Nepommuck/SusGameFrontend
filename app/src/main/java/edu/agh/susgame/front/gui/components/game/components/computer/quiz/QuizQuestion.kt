package edu.agh.susgame.front.gui.components.game.components.computer.quiz

import edu.agh.susgame.dto.socket.ServerSocketMessage


data class QuizQuestion(
    val question: String,
    val backendId: QuizQuestionId,
    val answers: List<QuizAnswer>,
) {
    data class QuizQuestionId(val value: Int)

    data class QuizAnswer(val index: Int, val answer: String, val isCorrect: Boolean)

    companion object {
        fun fromDto(dto: ServerSocketMessage.QuizQuestionDTO): QuizQuestion {
            require(dto.answers.size in 2..4)
            require(dto.correctAnswer in 0..<dto.answers.size)

            return QuizQuestion(
                question = dto.question,
                backendId = QuizQuestionId(dto.questionId),
                answers = dto.answers.mapIndexed { index, answer ->
                    QuizAnswer(index, answer, isCorrect = dto.correctAnswer == index)
                }
            )
        }
    }
}
