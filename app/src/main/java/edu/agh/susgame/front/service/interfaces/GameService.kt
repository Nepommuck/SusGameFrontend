package edu.agh.susgame.front.service.interfaces

import androidx.compose.ui.graphics.Color
import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.LobbyPin
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion.QuizAnswerId
import edu.agh.susgame.front.gui.components.game.components.computer.quiz.QuizQuestion.QuizQuestionId
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.LobbyManager
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.CompletableFuture

interface GameService {
    data class SimpleMessage(val author: PlayerNickname, val message: String)

    val messagesFlow: SharedFlow<SimpleMessage>

    fun initGameManager(gameManager: GameManager)

    fun initLobbyManager(lobbyManager: LobbyManager)

    // SOCKETS
    fun sendLeavingRequest(playerId: PlayerId)

    fun sendChangePlayerReadinessRequest(playerId: PlayerId, status: PlayerStatus)

    fun sendSimpleMessage(message: String)

    fun sendHostRouteUpdate(hostId: NodeId, packetPath: List<NodeId>)

    fun sendHostFlow(hostId: NodeId, packets: Int)

    fun sendStartGame()

    fun sendPlayerChangeColor(playerId: PlayerId, color: Color)

    fun sendUpgradeRouter(routerId: NodeId)

    fun sendFixRouter(routerId: NodeId)

    fun sendQuizQuestionAnswer(questionId: QuizQuestionId, answerId: QuizAnswerId)

    // REST
    fun isPlayerInLobby(lobbyId: LobbyId): Boolean

    fun joinLobby(
        lobbyId: LobbyId,
        lobbyPin: LobbyPin?,
        nickname: PlayerNickname,
    ): CompletableFuture<Unit>

    fun rejoinLobby(playerId: PlayerId): CompletableFuture<Unit>

    fun leaveLobby(): CompletableFuture<Unit>
}
