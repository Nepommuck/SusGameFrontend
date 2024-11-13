package edu.agh.susgame.front.service.web.socket

import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.ui.components.common.managers.GameManager

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
        // TODO Change to single server
        gameManager.packetsReceived.value = decodedMessage.server.packetsReceived
    }

    fun handleServerError(decodedMessage: ServerSocketMessage.ServerError) {
        println("Server error: ${decodedMessage.errorMessage}")
    }

    fun handlerQuizQuestion(decodedMessage: ServerSocketMessage.QuizQuestionDTO){

    }
}