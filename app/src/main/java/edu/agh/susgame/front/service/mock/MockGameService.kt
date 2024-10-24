package edu.agh.susgame.front.service.mock

import edu.agh.susgame.dto.rest.model.LobbyId
import edu.agh.susgame.dto.rest.model.Player
import edu.agh.susgame.dto.rest.model.PlayerId
import edu.agh.susgame.dto.rest.model.PlayerNickname
import edu.agh.susgame.front.service.interfaces.GameService
import edu.agh.susgame.front.service.interfaces.GameService.SimpleMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.concurrent.CompletableFuture

class MockGameService(private val lobbyService: MockLobbyService) : GameService {
    private val _messagesFlow = MutableSharedFlow<SimpleMessage>()

    private var joinedLobbyInfo: Pair<PlayerNickname, LobbyId>? = null

    override val messagesFlow = _messagesFlow.asSharedFlow()

    override fun isPlayerInLobby(lobbyId: LobbyId): Boolean =
        when (val lobbyInfo = joinedLobbyInfo) {
            null -> false
            else -> lobbyService.hasPlayerJoinedLobby(lobbyId, playerNickname = lobbyInfo.first)
        }

    override fun joinLobby(lobbyId: LobbyId, nickname: PlayerNickname): CompletableFuture<Unit> =
        lobbyService.joinLobby(lobbyId, Player(nickname, id = PlayerId(0), colorHex = 0x0000FF))

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
}
