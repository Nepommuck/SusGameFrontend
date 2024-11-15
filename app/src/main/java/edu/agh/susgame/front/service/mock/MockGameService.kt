package edu.agh.susgame.front.service.mock

import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.dto.rest.model.PlayerREST
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.GameService.SimpleMessage
import edu.agh.susgame.front.managers.GameManager
import edu.agh.susgame.front.managers.LobbyManager
import edu.agh.susgame.front.gui.components.common.util.player.PlayerStatus
import edu.agh.susgame.front.gui.components.common.graph.node.NodeId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture


// IGNORE THIS, ITS GONNA BE DELETED
class MockGameService(private val lobbyService: MockLobbyService) : GameService {
    private val _messagesFlow = MutableSharedFlow<SimpleMessage>()

    private var joinedLobbyInfo: Pair<PlayerNickname, LobbyId>? = null

    override val messagesFlow = _messagesFlow.asSharedFlow()

    override fun addGameManager(gameManager: GameManager) {}
    override fun addLobbyManager(lobbyManager: LobbyManager) {}
    override fun sendJoiningRequest(nickname: PlayerNickname) {

    }

    override fun sendLeavingRequest(playerId: PlayerId) {

    }

    override fun sendChangingStateRequest(playerId: PlayerId, status: PlayerStatus) {

    }

    override fun sendStartGame() {}

    override fun isPlayerInLobby(lobbyId: LobbyId): Boolean =
        when (val lobbyInfo = joinedLobbyInfo) {
            null -> false
            else -> lobbyService.hasPlayerJoinedLobby(lobbyId, playerNickname = lobbyInfo.first)
        }

    override fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit> =
        lobbyService.joinLobby(lobbyId, PlayerREST(nickname, id = PlayerId(0), colorHex = 0x0000FF))

    override fun leaveLobby(): CompletableFuture<Unit> =
        when (val lobbyInfo = joinedLobbyInfo) {
            null -> CompletableFuture.supplyAsync { }
            else -> lobbyService.leaveLobby(
                lobbyId = lobbyInfo.second,
                playerNickname = lobbyInfo.first,
            )
        }

    override fun sendSimpleMessage(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            _messagesFlow.emit(
                SimpleMessage(
                    author = PlayerNickname("Local-Player"),
                    message = message,
                )
            )
        }
    }

    override fun sendHostUpdate(
        hostId: NodeId,
        packetPath: List<NodeId>,
        packetsSentPerTick: Int,
    ) {
    }
}
