package edu.agh.susgame.front.service.web.socket.webmanagers

import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.service.interfaces.GameService

class WebGameManager(
    val gameManager: GameManager
) {
    fun handleChatMessage(decodedMessage: ServerSocketMessage.ChatMessage) {
        gameManager.addMessage(
            GameService.SimpleMessage(
                author = PlayerNickname(decodedMessage.authorNickname),
                message = decodedMessage.message,
            )
        )
    }

    fun handleGameState(decodedMessage: ServerSocketMessage.GameState) {
        gameManager.packetsReceived.value = decodedMessage.server.packetsReceived
        gameManager.updatePathsFromServer(decodedMessage)
    }

    fun handleServerError(decodedMessage: ServerSocketMessage.ServerError) {
        println("Server error: ${decodedMessage.errorMessage}")
    }

    fun handlerQuizQuestion(decodedMessage: ServerSocketMessage.QuizQuestionDTO) {

    }
}