package edu.agh.susgame.front.service.web.socket

import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.ui.graph.GameManager

class WebManager(val gameManager: GameManager) {
    fun handleChatMessage(decodedMessage: ServerSocketMessage.ChatMessage) {
        gameManager.addMessage(
            GameService.SimpleMessage(
                author = PlayerNickname(decodedMessage.authorNickname),
                message = decodedMessage.message,
            )
        )
    }

    fun handleGameState(decodedMessage: ServerSocketMessage.GameState) {
        gameManager.packetsReceived.value = decodedMessage.servers[0].packetsReceived
    }

    fun handleServerError(decodedMessage: ServerSocketMessage.ServerError) {
        println("Server error: ${decodedMessage.errorMessage}")
    }
}