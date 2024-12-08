package edu.agh.susgame.front.service.web.socket.webmanagers

import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.socket.ServerSocketMessage
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.managers.GameManager

class WebGameManager(
    val gameManager: GameManager
) {
    fun handleChatMessage(decodedMessage: ServerSocketMessage.ChatMessage) {
        gameManager.updateChat(
            author = PlayerNickname(decodedMessage.authorNickname),
            message = decodedMessage.message,
        )
    }

    fun handleGameState(decodedMessage: ServerSocketMessage.GameState) {
        gameManager.updateServerReceivedPackets(decodedMessage.server.packetsReceived)

        decodedMessage.hosts.forEach { host ->
            gameManager.updatePath(
                hostId = NodeId(host.id),
                route = host.packetRoute
            )
            gameManager.updateHostFlow(
                hostId = NodeId(host.id),
                flow = host.packetsSentPerTick
            )
        }

        decodedMessage.players.forEach { playerDTO ->
            gameManager.updatePlayerTokens(
                playerId = PlayerId(playerDTO.id),
                tokens = playerDTO.currentMoney
            )
        }

        decodedMessage.routers.forEach { routerDTO ->
            gameManager.updateRouterBufferSize(
                nodeId = NodeId(routerDTO.id),
                size = routerDTO.bufferSize
            )
            gameManager.updateRouterCurrentPackets(
                nodeId = NodeId(routerDTO.id),
                packets = routerDTO.bufferSize - routerDTO.spaceLeft
            )
            gameManager.updateRouterUpgradeCost(
                nodeId = NodeId(routerDTO.id),
                cost = routerDTO.upgradeCost
            )
            gameManager.updateRouterState(
                nodeId = NodeId(routerDTO.id),
                isWorking = routerDTO.isWorking,
            )
            gameManager.updateRouterOverheat(
                nodeId = NodeId(routerDTO.id),
                overheat = routerDTO.overheatLevel
            )
        }
        gameManager.updateGameStatus(decodedMessage.gameStatus)

        gameManager.updateGameTime(decodedMessage.remainingSeconds)

    }

    fun handlePathUpdate() {
//        gameManager.updatePathsFromServer(decodedMessage)
    }

    fun handleQuizQuestion(quizQuestion: ServerSocketMessage.QuizQuestionDTO) {

    }

    fun handleServerError(decodedMessage: ServerSocketMessage.ServerError) {
        println("Server error: ${decodedMessage.errorMessage}")
    }
}