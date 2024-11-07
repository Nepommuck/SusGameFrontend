package edu.agh.susgame.front.service.interfaces

import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.ui.graph.GameMapFront
import edu.agh.susgame.front.ui.graph.node.NodeId
import kotlinx.coroutines.flow.SharedFlow
import java.util.concurrent.CompletableFuture

interface GameService {
    data class SimpleMessage(val author: PlayerNickname, val message: String)

    val messagesFlow: SharedFlow<SimpleMessage>

    fun initGameFront(gameMapFront: GameMapFront)

    fun isPlayerInLobby(lobbyId: LobbyId): Boolean

    fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit>

    fun leaveLobby(): CompletableFuture<Unit>

    fun sendSimpleMessage(message: String)

    fun sendHostUpdate(hostId: NodeId, packetPath: List<NodeId>, packetsSentPerTick: Int)

    fun sendStartGame()


    // TODO GAME-79 All specific methods like
    //  `sendRouterUpdate(routerId, newRouterParams), `notifyAboutCreditChange(int)` etc.
    //  should be added
}
