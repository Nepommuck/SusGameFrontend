package edu.agh.susgame.front.service.interfaces

import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.ui.components.common.managers.GameManager
import edu.agh.susgame.front.ui.components.common.managers.LobbyManager
import edu.agh.susgame.front.ui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.ui.graph.node.NodeId
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.CompletableFuture

interface GameService {
    data class SimpleMessage(val author: PlayerNickname, val message: String)

    val messagesFlow: SharedFlow<SimpleMessage>

    fun addGameManager(gameManager: GameManager)

    fun addLobbyManager(lobbyManager: LobbyManager)

    // SOCKETS
    fun sendJoiningRequest(nickname: PlayerNickname)

    fun sendLeavingRequest(playerId: PlayerId)

    fun sendChangingStateRequest(playerId: PlayerId, status: PlayerStatus)

    fun sendSimpleMessage(message: String)

    fun sendHostUpdate(hostId: NodeId, packetPath: List<NodeId>, packetsSentPerTick: Int)

    fun sendStartGame()

    // REST
    fun isPlayerInLobby(lobbyId: LobbyId): Boolean

    fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit>

    fun leaveLobby(): CompletableFuture<Unit>


    // TODO GAME-79 All specific methods like
    //  `sendRouterUpdate(routerId, newRouterParams), `notifyAboutCreditChange(int)` etc.
    //  should be added
}
