package edu.agh.susgame.front.service.web.socket.webmanagers

import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
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
        gameManager.setServerReceivedPackets(decodedMessage.server.packetsReceived)
        gameManager.updatePathsFromServer(decodedMessage)

        decodedMessage.players.forEach { playerDTO ->
            gameManager.setPlayerTokens(
                playerId = PlayerId(playerDTO.id),
                tokens = playerDTO.currentMoney
            )
        }
        decodedMessage.routers.forEach { routerDTO ->
            gameManager.setRouterBufferSize(
                nodeId = NodeId(routerDTO.id),
                size = routerDTO.bufferSize
            )
            gameManager.setRouterCurrentPackets(
                nodeId = NodeId(routerDTO.id),
                packets = routerDTO.bufferSize - routerDTO.spaceLeft
            )
            gameManager.setRouterUpgradeCost(
                nodeId = NodeId(routerDTO.id),
                cost = routerDTO.upgradeCost
            )
            gameManager.updateRouter(
                NodeId(routerDTO.id),
                routerDTO.isWorking,
                routerDTO.overheatLevel
            )
        }
        decodedMessage.hosts.forEach() { host ->
            gameManager.updateHostFlow(NodeId(host.id), host.packetsSentPerTick)
        }

        gameManager.gameStatus.value = decodedMessage.gameStatus
        gameManager.gameTimeLeft.intValue = decodedMessage.remainingSeconds
    }

    fun handlePathUpdate() {
//        gameManager.updatePathsFromServer(decodedMessage)
    }

    fun handleServerError(decodedMessage: ServerSocketMessage.ServerError) {
        println("Server error: ${decodedMessage.errorMessage}")
    }

    fun handlerQuizQuestion(decodedMessage: ServerSocketMessage.QuizQuestionDTO) {

    }
}